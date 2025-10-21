package com.batuaa.transactionservice.service;

import com.batuaa.transactionservice.dto.TransactionDateRangeDto;
import com.batuaa.transactionservice.dto.TransactionRemarkDto;
import com.batuaa.transactionservice.dto.TransactionTypeDto;
import com.batuaa.transactionservice.dto.TransferDto;
import com.batuaa.transactionservice.exception.*;
import com.batuaa.transactionservice.model.Status;
import com.batuaa.transactionservice.model.Transaction;
import com.batuaa.transactionservice.model.Wallet;
import com.batuaa.transactionservice.repository.TransactionRepository;
import com.batuaa.transactionservice.repository.WalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TranscationSerivceImp implements TransactionService {

// have removed the @Autowired here as we are already doing it via a constructor and is not required at parameter level
private  final TransactionRepository transactionRepository;
private final WalletRepository walletRepository;

@Autowired
    public TranscationSerivceImp(TransactionRepository transactionRepository, WalletRepository walletRepository) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
    }
// wallet to wallet transfer
    @Override
    @Transactional
    public Transaction transferWalletToWallet(TransferDto transferDto) {
        // I want to mark the sender transaction as FAILED even if it was partially created.
        Transaction senderTransaction = null;
        try {
            // get the sender wallet
            Wallet walletFrom = walletRepository.findByWalletId(transferDto.getFromWalletId())
                    .orElseThrow(() -> new WalletNotFoundException(
                            "Sender wallet not found: " + transferDto.getFromWalletId()));

            // get the receiver wallet
            Wallet walletTo = walletRepository.findByWalletId(transferDto.getToWalletId())
                    .orElseThrow(() -> new WalletNotFoundException(
                            "Receiver wallet not found: " + transferDto.getToWalletId()));

            // validate amount
            if (transferDto.getAmount() == null) {
                throw new AmountCanNotBeNullException("Amount cannot be null");
            }

            // validate sender ownership
            if (walletFrom.getBuyer() == null || !walletFrom.getBuyer().getEmailId().equalsIgnoreCase(transferDto.getFromBuyerEmailId())) {
                throw new WalletNotFoundException(
                        "Error authenticating walletId: " + transferDto.getFromWalletId() + " to the logged in user account.");
            }

            // validate sufficient balance
            if (walletFrom.getBalance().compareTo(transferDto.getAmount()) < 0) {
                throw new InsufficientFundsException("Insufficient funds in sender wallet");
            }

            // Create sender transaction with PROCESSING status first
            senderTransaction = new Transaction();
            senderTransaction.setFromWallet(walletFrom);
            senderTransaction.setToWallet(walletTo);
            senderTransaction.setAmount(transferDto.getAmount());
            senderTransaction.setTimestamp(LocalDateTime.now());
            senderTransaction.setStatus(Status.PROCESSING);
            senderTransaction.setRemarks("Initiating transfer of Rs " + transferDto.getAmount() + " to wallet " + transferDto.getToWalletId());
            transactionRepository.save(senderTransaction);

            // update balances
            walletFrom.setBalance(walletFrom.getBalance().subtract(transferDto.getAmount()));
            walletTo.setBalance(walletTo.getBalance().add(transferDto.getAmount()));
            walletRepository.save(walletFrom);
            walletRepository.save(walletTo);

            // update sender transaction to SUCCESS
            senderTransaction.setStatus(Status.SUCCESS);
            senderTransaction.setRemarks("Transferred Rs " + transferDto.getAmount() + " to wallet " + transferDto.getToWalletId());
            transactionRepository.save(senderTransaction);

            // create receiver transaction
            Transaction receiverTransaction = new Transaction();
            receiverTransaction.setFromWallet(walletFrom);
            receiverTransaction.setToWallet(walletTo);
            receiverTransaction.setAmount(transferDto.getAmount());
            receiverTransaction.setTimestamp(LocalDateTime.now());
            receiverTransaction.setStatus(Status.SUCCESS);
            receiverTransaction.setRemarks("Received Rs " + transferDto.getAmount() + " from wallet " + transferDto.getFromWalletId());
            transactionRepository.save(receiverTransaction);

            log.info("Wallet transfer successful: {} transferred from {} to {}",
                    transferDto.getAmount(), transferDto.getFromWalletId(), transferDto.getToWalletId());
            return senderTransaction;

        } catch (WalletNotFoundException | AmountCanNotBeNullException | InsufficientFundsException e) {
            log.error("Wallet transfer failed | From: {}, To: {}, Amount: {} | Reason: {}",
                    transferDto.getFromWalletId(), transferDto.getToWalletId(), transferDto.getAmount(), e.getMessage(), e);

            if (senderTransaction != null) {
                try {
                    senderTransaction.setStatus(Status.FAILED);
                    senderTransaction.setRemarks("Transfer failed: " + e.getMessage());
                    transactionRepository.save(senderTransaction);
                } catch (Exception ex) {
                    log.warn("Failed to update transaction status to FAILED: {}", ex.getMessage());
                }
            }
            throw e;

        } catch (Exception e) {
            log.error("Unexpected error during wallet transfer | From: {}, To: {}, Amount: {}, Error: {}",
                    transferDto.getFromWalletId(), transferDto.getToWalletId(), transferDto.getAmount(), e.getMessage(), e);

            if (senderTransaction != null) {
                try {
                    senderTransaction.setStatus(Status.FAILED);
                    senderTransaction.setRemarks("Unexpected error: " + e.getMessage());
                    transactionRepository.save(senderTransaction);
                } catch (Exception ex) {
                    log.warn("Failed to update transaction status to FAILED: {}", ex.getMessage());
                }
            }
            throw new UnableToAddMoneyException("Error occurred while transferring money", e);
        }
    }

    // to filter transactions by date
    @Override
    @Transactional(readOnly = true)
    public List<Transaction> findByWalletIdAndDateBetween(TransactionDateRangeDto dto) {
        try {
            // 1. Validate wallet existence
            boolean walletExists = walletRepository.existsByIdAndEmailId(dto.getWalletId(), dto.getEmailId());
            if (!walletExists) {
                throw new WalletNotFoundException("Wallet not found: " + dto.getWalletId());
            }

            // 2. Validate date range
            if (dto.getStartDate().isAfter(dto.getEndDate())) {
                throw new DateTimeException("Start date cannot be after end date");
            }

            LocalDate startDate = dto.getStartDate();
            LocalDate endDate = dto.getEndDate();
            LocalDate today = LocalDate.now();

            // 3. Prevent selecting future calendar dates
            if (startDate.isAfter(today) || endDate.isAfter(today)) {
                throw new DateTimeException("Start and end dates cannot be in the future");
            }

            // 4. Convert to time range (full day)
            LocalDateTime startOfDay = startDate.atStartOfDay();
            LocalDateTime endOfDay = endDate.atTime(LocalTime.MAX);

            // 5.Pagination setup (20 records per page)
            Pageable pageable = PageRequest.of(dto.getPage(), 20, Sort.by("timestamp").descending());

            // 6. Fetch transactions from repo
            Page<Transaction> transactionsPage = transactionRepository.findByWalletAndTimestampRange(
                    dto.getWalletId(),
                    startOfDay,
                    endOfDay,
                    pageable
            );

            // [7]. Return results directly
            return transactionsPage.getContent();

        } catch (WalletNotFoundException | DateTimeException e) {
            log.error("Validation error while fetching transactions: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error fetching transactions for wallet {}: {}", dto.getWalletId(), e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    //to view transactions based on wallet id, email id, and type
    @Override
    @Transactional(readOnly = true)
    public List<Transaction> viewTransactionsByType(TransactionTypeDto transactionTypeDto) {
        try {
            // 1 Validate wallet existence
            boolean walletExists = walletRepository.existsByIdAndEmailId(
                    transactionTypeDto.getWalletId(),
                    transactionTypeDto.getEmailId()
            );

            if (!walletExists) {
                throw new WalletNotFoundException("Wallet not found: " + transactionTypeDto.getWalletId());
            }

            // 2.Fetch transactions by walletId and type
            List<Transaction> result = transactionRepository.findByWalletIdAndType(
                    transactionTypeDto.getWalletId(),transactionTypeDto.getWalletId(),
                    transactionTypeDto.getType()
            );

            if (result.isEmpty()) {
                throw new EmptyTransactionListException("No transactions found for wallet: " + transactionTypeDto.getWalletId());
            }

            // 3. Sort by timestamp (newest first)
            return result.stream()
                    .sorted(Comparator.comparing(Transaction::getTimestamp).reversed())
                    .collect(Collectors.toList());

        } catch (WalletNotFoundException e) {
            log.error("Validation error while fetching transactions: {}", e.getMessage());
        } catch (EmptyTransactionListException e) {
            log.warn(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error fetching transactions for wallet {}: {}",
                    transactionTypeDto.getWalletId(), e.getMessage(), e);
        }

        return Collections.emptyList();
    }


    @Transactional(readOnly = true)
    @Override
    public List<Transaction> filterTransactionsByRemark(TransactionRemarkDto transactionRemarkDto) {
        try {
            // validate wallet existence
            Wallet wallet = walletRepository.findByWalletId(transactionRemarkDto.getWalletId())
                    .orElseThrow(() -> new WalletNotFoundException("Wallet not found: " + transactionRemarkDto.getWalletId()));

            // validate remark keyword
            String keyword = transactionRemarkDto.getRemark() != null ? transactionRemarkDto.getRemark() : "";

            // get transactions
            List<Transaction> transactions = transactionRepository.findByWalletAndEmailAndRemarkNative(
                    wallet.getWalletId(), transactionRemarkDto.getEmailId(), keyword);

            // handling empty results
            if (transactions.isEmpty() && !keyword.isBlank()) {
                log.warn("No transactions found for wallet {} with remark '{}'", transactionRemarkDto.getWalletId(), keyword);
                throw new TransactionNotFoundException("No transaction found for wallet " + transactionRemarkDto.getWalletId() + " with remark: " + keyword);
            }
            log.info("Filtered {} transactions for wallet {} with remark containing '{}'", transactions.size(), transactionRemarkDto.getWalletId(), keyword);
            return transactions;

        } catch (WalletNotFoundException e) {
            log.error("Wallet not found or invalid: {}", transactionRemarkDto.getWalletId(), e);
            throw e;
        } catch (TransactionNotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while filtering transactions for wallet {} with remark '{}': {}", transactionRemarkDto.getWalletId(), transactionRemarkDto.getRemark(), e.getMessage(), e);
            throw new UnableToFilterByRemarkException("Error occurred while filtering transactions by remark" + e);
        }
    }

    @Override
    public List<Transaction> getAllTransactions(String emailId, String walletId) {
        return List.of();
    }

    @Override
    public List<Transaction> sortTransactionsByAmount(String emailId, String walletId) {
        return List.of();
    }

    @Override
    public Transaction getTransactionByWalletId(String emailId, String walletId) {
        return null;
    }
}
