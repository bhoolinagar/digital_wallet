package com.batuaa.transactionservice.repository;


import com.batuaa.transactionservice.model.Status;
import com.batuaa.transactionservice.model.Transaction;
import com.batuaa.transactionservice.model.Type;
import com.batuaa.transactionservice.model.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    //find wallet id by custom range
    List<Transaction> findByFromWallet_WalletIdOrToWallet_WalletIdAndTimestampBetween(String walletId, String walletId1, LocalDateTime startOfDay, LocalDateTime endOfDay);


    // List<Transaction>findByFromWallet_WalletIdOrToWallet_WalletId(String )
    //List<Transaction> findByFromWallet_WalletIdAndType(String walletId, Type type);

    @Query("""
        SELECT t FROM Transaction t
        WHERE (UPPER(t.fromWallet.walletId) = UPPER(:walletId)
               OR UPPER(t.toWallet.walletId) = UPPER(:walletId))
          AND t.timestamp BETWEEN :startTime AND :endTime
        ORDER BY t.timestamp DESC
    """)
    Page<Transaction> findByWalletAndTimestampRange(
            @Param("walletId") String walletId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            Pageable pageable
    );

    @Query("""
            select t from Transaction t
            where t.fromWallet.walletId = :walletId and t.toWallet.walletId = :walletId1 and t.type = :type""")
    List<Transaction> findByWalletIdAndType(@Param("walletId") String walletId, @Param("walletId1") String walletId1, @Param("type") Type type);


    @Query(
            value = "SELECT * FROM transaction_records t " +
                    "WHERE (UPPER(t.from_wallet_id) = UPPER(:walletId) OR UPPER(t.to_wallet_id) = UPPER(:walletId)) " +
                    "AND (UPPER(t.from_email_id) = UPPER(:emailId) OR UPPER(t.to_email_id) = UPPER(:emailId)) " +
                    "AND LOWER(t.remarks) LIKE LOWER(CONCAT('%', :keyword, '%'))",
            nativeQuery = true)
    List<Transaction> findByWalletAndEmailAndRemarkNative(
            @Param("walletId") String walletId,
            @Param("emailId") String emailId,
            @Param("keyword") String keyword);


    Optional<Transaction> findByFromWalletAndToWalletAndAmountAndStatus(Wallet walletFrom, Wallet walletTo, BigDecimal amount, Status status);
}
