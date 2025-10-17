package com.batuaa.UserProfile.repository;

import com.batuaa.UserProfile.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, String> {
    // Locks row in DB to prevent concurrent access, will prevents race conditions on same wallet
   // @Lock(LockModeType.PESSIMISTIC_WRITE)
   
	Optional<Wallet> findByWalletId(String walletId);




}