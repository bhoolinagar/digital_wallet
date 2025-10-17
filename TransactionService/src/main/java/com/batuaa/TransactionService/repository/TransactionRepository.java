package com.batuaa.TransactionService.repository;


import com.batuaa.TransactionService.model.Transaction;
import com.batuaa.TransactionService.model.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    //find wallet id by custom range
    List<Transaction> findByFromWallet_WalletIdOrToWallet_WalletIdAndTimestampBetween(String walletId, String walletId1, LocalDateTime startOfDay, LocalDateTime endOfDay);
   // List<Transaction>findByFromWallet_WalletIdOrToWallet_WalletId(String )
    List<Transaction> findByFromWallet_WalletIdAndType(String walletId, Type type);
}
