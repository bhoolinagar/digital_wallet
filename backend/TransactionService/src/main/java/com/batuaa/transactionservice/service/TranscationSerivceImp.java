package com.batuaa.transactionservice.service;

import com.batuaa.transactionservice.dto.TransactionDateRangeDto;
import com.batuaa.transactionservice.dto.TransactionRemarkDto;
import com.batuaa.transactionservice.dto.TransactionTypeDto;
import com.batuaa.transactionservice.dto.TransferDto;
import com.batuaa.transactionservice.exception.EmptyTransactionListException;
import com.batuaa.transactionservice.exception.WalletNotFoundException;
import com.batuaa.transactionservice.model.Transaction;
import com.batuaa.transactionservice.model.Type;
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

@Autowired
private  final TransactionRepository transactionRepository;
private final WalletRepository walletRepository;

@Autowired
    public TranscationSerivceImp(TransactionRepository transactionRepository, WalletRepository walletRepository) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
    }


    @Override
    public Transaction transferWalletToWallet(TransferDto transferDto) {
        return null;
    }

    // to filter transactions by date

    //@Transactional(readOnly = true)

    @Override
    public List<Transaction> findByWalletIdAndDateBetween(TransactionDateRangeDto dto) {

        // 1. Validate wallet existence
        if (!walletRepository.existsByIdAndEmailId(dto.getWalletId(), dto.getEmailId())) {
            throw new WalletNotFoundException("Wallet not found: " + dto.getWalletId());
        }

        // 2. Validate date range order
        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new DateTimeException("Start date cannot be after end date.");
        }

        // 3. Prepare LocalDateTime boundaries
        LocalDateTime startOfDay = dto.getStartDate().atStartOfDay();
        LocalDateTime now = LocalDateTime.now();
        // If end date is today, end time is now; otherwise end of that day
        LocalDateTime endOfDay = dto.getEndDate().isEqual(LocalDate.now())
                ? now
                : dto.getEndDate().atTime(LocalTime.MAX);
        //  LocalDateTime endOfDay = dto.getEndDate().atTime(LocalTime.MAX).minusSeconds(50);


        // 4. Disallow any future date (start or end)
        if (startOfDay.isAfter(now) || endOfDay.isAfter(now)) {
            throw new DateTimeException("Start and end dates must not be in the future.");
        }

        // 5. Setup pagination (20 records per page)
        Pageable pageable = PageRequest.of(dto.getPage(), 20, Sort.by("timestamp").descending());

        // 6. Fetch transactions within range (for both sent/received)
        Page<Transaction> transactionsPage = transactionRepository.findByWalletAndTimestampRange(
                dto.getWalletId(),
                startOfDay,
                endOfDay,
                pageable
        );

        // 7. Handle empty results
        if (transactionsPage.isEmpty()) {
            throw new EmptyTransactionListException("No transactions found for the given date range.");
        }

        // 8. Return sorted list (descending by timestamp)
        return transactionsPage.getContent()
                .stream()
                .sorted(Comparator.comparing(Transaction::getTimestamp).reversed())
                .collect(Collectors.toList());
    }

    //to view transactions based on wallet id, email id, and type
    @Override
    @Transactional(readOnly = true)
    public List<Transaction> viewTransactionsByType(TransactionTypeDto transactionTypeDto) {

        // 1. Validate wallet existence
        boolean walletExists = walletRepository.existsByIdAndEmailId(
                transactionTypeDto.getWalletId(),
                transactionTypeDto.getEmailId()
        );

        if (!walletExists) {
            throw new WalletNotFoundException("Wallet Id or email not found: " + transactionTypeDto.getWalletId());
        }

        // 2. Fetch transactions based on type
        List<Transaction> result;
        if (Type.WITHDRAWN.equals(transactionTypeDto.getType())) {
            result = transactionRepository.findByFromWalletIdAndType(
                    transactionTypeDto.getWalletId(),
                    transactionTypeDto.getType()
            );
        } else if (Type.RECEIVED.equals(transactionTypeDto.getType())) {
            result = transactionRepository.findByToWalletIdAndType(
                    transactionTypeDto.getWalletId(),
                    transactionTypeDto.getType()
            );
        } else {
            throw new IllegalArgumentException("Invalid transaction type: " + transactionTypeDto.getType());
        }

        // 3. Handle empty list
        if (result.isEmpty()) {
            throw new EmptyTransactionListException(
                    "No transactions found for wallet: " + transactionTypeDto.getWalletId() + " and type: " + transactionTypeDto.getType()
            );
        }
        // 4. Sort by newest first
        return result.stream()
                .sorted(Comparator.comparing(Transaction::getTimestamp).reversed())
                .collect(Collectors.toList());

    }



    @Override
    public List<Transaction> filterTransactionsByRemark(TransactionRemarkDto transactionRemarkDto) {
        return List.of();
    }

    @Override
    public List<Transaction> getAllTransactions(String emailId, String walletId) {
        return List.of();
    }

    @Override
    public List<Transaction> sortTransactionsByAmount(String walletId, String emailId) {
       return transactionRepository.findTransactionAmountByWalletAndEmail(walletId,emailId);
    }

    @Override
    public Transaction getTransactionByWalletId(String emailId, String walletId) {
        return null;
    }
}

