package com.batuaa.userprofile.service;

import com.batuaa.userprofile.dto.WalletDto;
import com.batuaa.userprofile.exception.BuyerNotFoundException;
import com.batuaa.userprofile.exception.WalletAlreadyFound;
import com.batuaa.userprofile.exception.WalletNotFoundException;
import com.batuaa.userprofile.model.Buyer;
import com.batuaa.userprofile.model.Gender;
import com.batuaa.userprofile.model.Role;
import com.batuaa.userprofile.model.Wallet;
import com.batuaa.userprofile.repository.BuyerRepository;
import com.batuaa.userprofile.repository.WalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@Slf4j
class WalletServiceImplTest {

    @Mock
    private WalletRepository walletRepository;
    @Mock
    private BuyerRepository buyerRepository;
    @InjectMocks
    private WalletServiceImpl walletService;

    private WalletDto walletDto;
    private Buyer buyer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        walletDto = new WalletDto();
        walletDto.setEmailId("bhooli123@gmail.com");
        walletDto.setAccountNumber("AC1234");
        walletDto.setBalance(new BigDecimal("1000"));
        walletDto.setBankName("HDFC Bank");

        buyer = new Buyer();
        buyer.setEmailId("bhooli123@gmail.com");
        buyer.setName("Bhooli");
        buyer.setGender(Gender.FEMALE);
        buyer.setPassword("password123");
        buyer.setRole(Role.BUYER);
    }

    @Test
    void generateWalletId() {
    }

    @Test
    void getWalletDetails() {
    }

    @Test
    void linkBankAccountToWallet_success() {
        // Mock: Buyer exists and wallet does not exist
        when(buyerRepository.findByEmailId(walletDto.getEmailId())).thenReturn(Optional.of(buyer));
        when(walletRepository.existsByWalletId(anyString())).thenReturn(false);
        when(walletRepository.existsByBuyerEmailIdAndAccountNumber(walletDto.getEmailId(), walletDto.getAccountNumber()))
                .thenReturn(false);

        String walletId = walletService.linkBankAccountToWallet(walletDto);

        // Assertions
        assertNotNull(walletId);
       // assertTrue(walletId.startsWith("WAL"));          // Wallet ID prefix
       // assertEquals(12, walletId.length());             // 12-character ID

        // Verify save is called
        verify(walletRepository, times(1)).save(any(Wallet.class));
    }

    @Test
    void linkBankAccountToWallet_walletAlreadyExists() {
        when(walletRepository.existsByBuyerEmailIdAndAccountNumber(walletDto.getEmailId(), walletDto.getAccountNumber()))
                .thenReturn(true);

        WalletAlreadyFound exception = assertThrows(WalletAlreadyFound.class, () -> {
            walletService.linkBankAccountToWallet(walletDto);
        });

        assertEquals("Bank account already linked with a wallet", exception.getMessage());
    }

    @Test
    void linkBankAccountToWallet_buyerNotFound() {
        when(buyerRepository.findByEmailId(walletDto.getEmailId())).thenReturn(Optional.empty());

        BuyerNotFoundException exception = assertThrows(BuyerNotFoundException.class, () -> {
            walletService.linkBankAccountToWallet(walletDto);
        });

        assertEquals("Buyer not registered with this email ID", exception.getMessage());
    }
    @Test
    void updateMoneyFromBank_success() {
        // Arrange
        String walletId = "WALB7A0E0285";
        BigDecimal amountToAdd = new BigDecimal("500");
        Wallet existingWallet = new Wallet();
        existingWallet.setWalletId(walletId);
        existingWallet.setBalance(new BigDecimal("1000"));

        when(walletRepository.findByWalletId(walletId)).thenReturn(Optional.of(existingWallet));

        // Act
        String result = walletService.updateMoneyFromBank(walletId, amountToAdd);

        // Assert
        assertEquals("Money successfully added into wallet. New Balance: 1500", result);
        assertEquals(new BigDecimal("1500"), existingWallet.getBalance());
        verify(walletRepository, times(1)).save(existingWallet);
    }

    @Test
    void updateMoneyFromBank_walletNotFound() {
        String walletId = "INVALID_WALLET";
        BigDecimal amount = new BigDecimal("500");

        when(walletRepository.findByWalletId(walletId)).thenReturn(Optional.empty());

        WalletNotFoundException exception = assertThrows(WalletNotFoundException.class, () -> {
            walletService.updateMoneyFromBank(walletId, amount);
        });

        assertEquals("Wallet not found with ID: INVALID_WALLET", exception.getMessage());
    }

    @Test
    void getWalletDetails_success() {
        String email = "bhooli123@gmail.com";
        String walletId = "WALB7A0E0285";

        Wallet wallet = new Wallet();
        wallet.setWalletId(walletId);
        wallet.setBalance(new BigDecimal("1000"));
        Buyer buyer = new Buyer();
        buyer.setEmailId(email);
        wallet.setBuyer(buyer);

        when(walletRepository.findByBuyerEmailIdAndWalletId(email, walletId)).thenReturn(Optional.of(wallet));

        Wallet result = walletService.getWalletDetails(email, walletId);

        assertNotNull(result);
        assertEquals(walletId, result.getWalletId());
        assertEquals(new BigDecimal("1000"), result.getBalance());
    }

    @Test
    void getWalletDetails_walletNotFound() {
        String email = "bhooli123@gmail.com";
        String walletId = "INVALID_WALLET";

        when(walletRepository.findByBuyerEmailIdAndWalletId(email, walletId)).thenReturn(Optional.empty());

        WalletNotFoundException exception = assertThrows(WalletNotFoundException.class, () -> {
            walletService.getWalletDetails(email, walletId);
        });

        assertEquals("Wallet not found with ID: " + walletId, exception.getMessage());
    }

}