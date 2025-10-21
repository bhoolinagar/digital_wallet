package com.batuaa.transactionservice.repository;

import com.batuaa.transactionservice.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, String> {
    @Query("""
            select (count(w) > 0) from Wallet w

            where upper(w.walletId) = upper(?1) and upper(w.buyer.emailId) = upper(?2)""")
    boolean existsByIdAndEmailId(String walletId, String emailId);

            where upper(w.buyer.emailId) = upper(:emailId) and upper(w.walletId) = upper(:walletId)""")
    boolean existsByIdAndEmailId(@Param("emailId") String emailId, @Param("walletId") String walletId);

    Optional<Wallet> findByWalletId(String walletId);




}