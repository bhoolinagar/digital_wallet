package com.batuaa.UserProfile.controller;

import com.batuaa.UserProfile.Dto.WalletDto;
import com.batuaa.UserProfile.exception.BuyerNotFoundException;
import com.batuaa.UserProfile.exception.GlobalExceptionHandler;
import com.batuaa.UserProfile.exception.WalletAlreadyFound;
import com.batuaa.UserProfile.exception.WalletNotFoundException;
import com.batuaa.UserProfile.model.Buyer;
import com.batuaa.UserProfile.model.Gender;
import com.batuaa.UserProfile.model.Role;
import com.batuaa.UserProfile.model.Wallet;
import com.batuaa.UserProfile.service.WalletService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class WalletControllerTest {

    private MockMvc mockMvc;

    @Mock
    private WalletService walletService;

    @InjectMocks
    private WalletController walletController;

    private WalletDto walletDto;
    private Wallet wallet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

// Attach GlobalExceptionHandler to MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(walletController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        walletDto = new WalletDto();
        walletDto.setEmailId("bhooli@gmail.com");
        walletDto.setAccountNumber("AC1234");
        walletDto.setBankName("HDFC Bank");
        walletDto.setBalance(new BigDecimal("1000"));

        Buyer buyer = new Buyer();
        buyer.setEmailId("bhooli@gmail.com");
        buyer.setName("Bhooli");
        buyer.setGender(Gender.FEMALE);
        buyer.setRole(Role.BUYER);

        wallet = new Wallet();
        wallet.setWalletId("WALB7A0E0285");
        wallet.setAccountNumber("AC1234");
        wallet.setBalance(new BigDecimal("1000"));
        wallet.setBuyer(buyer);
        wallet.setCreatedAt(LocalDateTime.now());
    }

    // -- Link Bank Account --
    @Test
    void linkBankAccount_success() throws Exception {
        when(walletService.linkBankAccountToWallet(any(WalletDto.class)))
                .thenReturn("WALB7A0E0285");

        String walletJson = "{ \"emailId\": \"bhooli@gmail.com\", \"balance\": 1000, \"bankName\": \"HDFC Bank\", \"accountNumber\": \"AC1234\" }";

        mockMvc.perform(post("/wallet/api/v1/link-bank-account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(walletJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").value("WALB7A0E0285"));
    }

    @Test
    void linkBankAccount_walletAlreadyExists() throws Exception {
        when(walletService.linkBankAccountToWallet(any(WalletDto.class)))
                .thenThrow(new WalletAlreadyFound("Bank account already linked with wallet"));

        String walletJson = "{ \"emailId\": \"bhooli@gmail.com\", \"balance\": 1000, \"bankName\": \"HDFC Bank\", \"accountNumber\": \"AC1234\" }";

        mockMvc.perform(post("/wallet/api/v1/link-bank-account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(walletJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Bank account already linked with wallet"));
    }
    @Test
    void linkBankAccount_failure_buyerNotFound() throws Exception {
        // Mock service to throw BuyerNotFoundException when buyer doesn't exist
        when(walletService.linkBankAccountToWallet(any(WalletDto.class)))
                .thenThrow(new BuyerNotFoundException("Buyer not found with email: invalid@gmail.com"));

        String walletJson = "{ \"emailId\": \"invalid@gmail.com\", \"balance\": 1000, \"bankName\": \"HDFC Bank\", \"accountNumber\": \"AC1234\" }";

        // Expect 404 Not Found
        mockMvc.perform(post("/wallet/api/v1/link-bank-account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(walletJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Buyer not found with email: invalid@gmail.com"));
    }

    // -- Add Money to Wallet --
    @Test
    void addMoneyToWallet_success() throws Exception {
        String walletId = "WALB7A0E0285";
        BigDecimal amount = new BigDecimal("500");

        when(walletService.updateMoneyFromBank(walletId, amount))
                .thenReturn("Money successfully added into wallet");

        mockMvc.perform(post("/wallet/api/v1/add-money")
                        .param("walletId", walletId)
                        .param("amount", amount.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Money successfully added into wallet"));
    }

    @Test
    void addMoneyToWallet_walletNotFound() throws Exception {
        String walletId = "INVALID_WALLET";
        BigDecimal amount = new BigDecimal("500");

        // Mock service to throw WalletNotFoundException
        when(walletService.updateMoneyFromBank(walletId, amount))
                .thenThrow(new WalletNotFoundException("Wallet not found with ID: " + walletId));

        // Perform POST request and expect 404 Not Found
        mockMvc.perform(post("/wallet/api/v1/add-money")
                        .param("walletId", walletId)
                        .param("amount", amount.toString()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Wallet not found with ID: " + walletId));
    }

    @Test
    void addMoneyToWallet_invalidAmount() throws Exception {
        String walletId = "WALB7A0E0285";
        BigDecimal amount = new BigDecimal("-100"); // negative amount

        // Mock service to throw IllegalArgumentException for invalid amount
        when(walletService.updateMoneyFromBank(walletId, amount))
                .thenThrow(new IllegalArgumentException("Amount must be greater than zero"));

        // Perform POST request and expect 400 Bad Request
        mockMvc.perform(post("/wallet/api/v1/add-money")
                        .param("walletId", walletId)
                        .param("amount", amount.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Amount must be greater than zero"));
    }

    // --- Get Wallet Details ---
    @Test
    void getWalletDetails_success() throws Exception {
        String email = "bhooli@gmail.com";
        String walletId = "WALB7A0E0285";

        when(walletService.getWalletDetails(email, walletId))
                .thenReturn(wallet);

        mockMvc.perform(get("/wallet/api/v1/details")
                        .param("email", email)
                        .param("walletId", walletId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Wallet details fetched successfully"))
                .andExpect(jsonPath("$.data.walletId").value(walletId))
                .andExpect(jsonPath("$.data.balance").value(1000));
    }

    @Test
    void getWalletDetails_walletNotFound() throws Exception {
        String email = "bhooli@gmail.com";
        String walletId = "INVALID_WALLET";

        when(walletService.getWalletDetails(email, walletId))
                .thenThrow(new WalletNotFoundException("Wallet not found with ID: " + walletId));

        mockMvc.perform(get("/wallet/api/v1/details")
                        .param("email", email)
                        .param("walletId", walletId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Wallet not found with ID: " + walletId));
    }
}
