package com.batuaa.TransactionService.service;

import com.batuaa.TransactionService.dto.TransactionDateRangeDto;
import com.batuaa.TransactionService.dto.TransactionRemarkDto;
import com.batuaa.TransactionService.dto.TransactionTypeDto;
import com.batuaa.TransactionService.dto.TransferDto;
import com.batuaa.TransactionService.model.Transaction;

import java.util.List;

public interface TransactionService {

    // Transfer money from one wallet to another
    Transaction transferWalletToWallet(TransferDto transferDto);

    // Find transactions by walletId within a specific date range
    List<Transaction> findByWalletIdAndDateBetween(TransactionDateRangeDto transactionDateRangeDto);

    // View transactions filtered by type (DEBIT or CREDIT)
    List<Transaction> viewTransactionsByType(TransactionTypeDto transactionTypeDto);

    // Filter transactions by remarks
    List<Transaction> filterTransactionsByRemark(TransactionRemarkDto transactionRemarkDto);

    // Get all transactions for a given wallet and email, sorted by recent date
    List<Transaction> getAllTransactions(String emailId, String walletId);

    // Sort transactions by amount (ascending or descending)
    List<Transaction> sortTransactionsByAmount(String emailId, String walletId);

    // Get a single transaction by walletId and email
    Transaction getTransactionByWalletId(String emailId, String walletId);

}

