package com.batuaa.UserProfile.service;
import com.batuaa.UserProfile.model.Wallet;
import com.batuaa.UserProfile.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;


@Service
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;

    @Autowired
    public WalletServiceImpl(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

	// to generate walletId unique 11 characters with help of( userId,email,
	// accountNumber)
	@Override
	public String generateWalletId(String email, String accountNumber) {
		try {
			// Combine key user attributes for uniqueness
			String input = email + accountNumber;

			// Create SHA-256 hash
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

			// Convert first few bytes to hex (for 8 hash characters)
			StringBuilder hex = new StringBuilder();
			for (int i = 0; i < 4; i++) { // 4 bytes = 8 hex chars
				hex.append(String.format("%02x", hash[i]));
			}

			// Build final ID: "WAL" + 8-character hash = 11 characters
            String s = "WAL" + hex.substring(0, 8).toUpperCase();
            return s;

		} catch (Exception e) {
			throw new RuntimeException("Error generating wallet ID", e);
		}
	}

    // to get balance from wallet
    @Override
    public Wallet getWalletDetails(String emailId, String walletId) {
        return null;
    }

    // to get balance from wallet



}