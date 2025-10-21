package com.batuaa.transactionservice.service;

import com.batuaa.transactionservice.dto.TransactionDateRangeDto;
import com.batuaa.transactionservice.dto.TransactionRemarkDto;
import com.batuaa.transactionservice.dto.TransactionTypeDto;
import com.batuaa.transactionservice.dto.TransferDto;
import com.batuaa.transactionservice.exception.*;
import com.batuaa.transactionservice.model.*;
import com.batuaa.transactionservice.repository.TransactionRepository;
import com.batuaa.transactionservice.repository.WalletRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
    public Transaction transferWalletToWallet(@Valid TransferDto transferDto) {
        Transaction senderTransaction = null;
        try {
            Wallet walletFrom = walletRepository.findByWalletId(transferDto.getFromWalletId())
                    .orElseThrow(() -> new WalletNotFoundException(
                            "Sender wallet not found: " + transferDto.getFromWalletId()));
            Wallet walletTo = walletRepository.findByWalletId(transferDto.getToWalletId())
                    .orElseThrow(() -> new WalletNotFoundException(
                            "Receiver wallet not found: " + transferDto.getToWalletId()));

            // Check for duplicate transaction
            Optional<Transaction> existingTransaction = transactionRepository
                    .findByFromWalletAndToWalletAndAmountAndStatus(
                            walletFrom, walletTo, transferDto.getAmount(), Status.PROCESSING);

            if (existingTransaction.isPresent()) {
                throw new DuplicateTransactionException(
                        "A similar transaction is already in process from " +
                                transferDto.getFromWalletId() + " to " + transferDto.getToWalletId());
            }

            if (walletFrom.getBalance().compareTo(transferDto.getAmount()) < 0) {
                throw new InsufficientFundsException("Insufficient funds in sender wallet");
            }

//             sender transaction
            senderTransaction = new Transaction(walletFrom, walletTo, walletFrom.getBuyer(), walletTo.getBuyer(),
                    transferDto.getAmount(), LocalDateTime.now(), Status.PROCESSING,
                    "Initiating transfer of Rs " + transferDto.getAmount() + " to wallet " + transferDto.getToWalletId(),
                    Type.WITHDRAWN);
            transactionRepository.save(senderTransaction);
            log.info("Sender transaction created | ID: {}, Amount: {}", senderTransaction.getTransactionId(), transferDto.getAmount());

            //update balances
            walletFrom.setBalance(walletFrom.getBalance().subtract(transferDto.getAmount()));
            walletTo.setBalance(walletTo.getBalance().add(transferDto.getAmount()));
            walletRepository.save(walletFrom);
            walletRepository.save(walletTo);

            // transaction done successfully
            senderTransaction.setStatus(Status.SUCCESS);
            senderTransaction.setRemarks("Transferred Rs " + transferDto.getAmount() + " to wallet " + transferDto.getToWalletId());
            transactionRepository.save(senderTransaction);
            //receiver transaction
            Transaction receiverTransaction = new Transaction( walletFrom, walletTo, walletFrom.getBuyer(), walletTo.getBuyer(),
                    transferDto.getAmount(), LocalDateTime.now(), Status.SUCCESS,
                    "Received Rs " + transferDto.getAmount() + " from wallet " + transferDto.getFromWalletId(),
                    Type.RECEIVED);
            transactionRepository.save(receiverTransaction);
            log.info("Receiver transaction created | ID: {},Amount: {}", receiverTransaction.getTransactionId(), transferDto.getAmount());

            log.info("Wallet transfer successful | From: {}, To: {},Amount: {}", transferDto.getFromWalletId(), transferDto.getToWalletId(), transferDto.getAmount());

            return senderTransaction;

        } catch (WalletNotFoundException | InsufficientFundsException | DuplicateTransactionException e) {
            log.error("Wallet transfer failed | From: {}, To: {},Amount: {} | Reason: {}",
                    transferDto.getFromWalletId(), transferDto.getToWalletId(), transferDto.getAmount(), e.getMessage(), e);

            if (senderTransaction != null) {senderTransaction.setStatus(Status.FAILED);senderTransaction.setRemarks("Transfer failed: " + e.getMessage());
                transactionRepository.save(senderTransaction);
            }
            throw e;

        } catch (Exception e) {
            log.error("Unexpected error during wallet transfer | From: {}, To: {}, Amount: {}, Error: {}",
                    transferDto.getFromWalletId(), transferDto.getToWalletId(), transferDto.getAmount(), e.getMessage(), e);

            if (senderTransaction != null) {senderTransaction.setStatus(Status.FAILED);
                senderTransaction.setRemarks("Unexpected error: " + e.getMessage());
                transactionRepository.save(senderTransaction);
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


//    Filter Transactions By Remarks
    @Transactional
    @Override
    public List<Transaction> filterTransactionsByRemark(TransactionRemarkDto transactionRemarkDto) {
        try {
            List<Transaction> transactions = transactionRepository.findByWalletAndEmailAndRemarkNative(
                    transactionRemarkDto.getWalletId(), transactionRemarkDto.getEmailId(), transactionRemarkDto.getRemark());

            // handling empty results
            if (transactions.isEmpty()) {
                log.warn("No transactions found for wallet {} with remark '{}'", transactionRemarkDto.getWalletId(), transactionRemarkDto.getRemark());
                throw new TransactionNotFoundException(
                        "No transaction found for wallet " + transactionRemarkDto.getWalletId() + " with remark: " + transactionRemarkDto.getRemark());
            }
            log.info("Filtered {} transactions for wallet {} with remark '{}'", transactions.size(), transactionRemarkDto.getWalletId(), transactionRemarkDto.getRemark());
            return transactions;

        } catch (TransactionNotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error filtering transactions for wallet {} with remark '{}'", transactionRemarkDto.getWalletId(), transactionRemarkDto.getRemark(), e);
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
