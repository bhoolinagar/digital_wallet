package com.batuaa.userprofile.service;

import com.batuaa.userprofile.dto.WalletDto;
import com.batuaa.userprofile.model.Wallet;

import java.math.BigDecimal;

public interface WalletService {
	// to generate walletId unique 11 characters with help of( userId,email,
	// accountNumber)
	// Generates a unique 11-character wallet ID using email and account number
	public String generateWalletId(String email, String  accountNumber);

	// to get balance from wallet
    Wallet getWalletDetails(String emailId, String walletId);

    // linkBankAccountToWallet( WalletDto wallet)
    public  String linkBankAccountToWallet( WalletDto wallet);
    String updateMoneyFromBank(String walletId,  BigDecimal amount);


}