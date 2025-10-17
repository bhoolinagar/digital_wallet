package com.batuaa.UserProfile.controller;

import com.batuaa.UserProfile.Dto.ApiResponse;
import com.batuaa.UserProfile.Dto.WalletDto;
import com.batuaa.UserProfile.exception.BuyerNotFoundException;
import com.batuaa.UserProfile.exception.WalletAlreadyFound;
import com.batuaa.UserProfile.exception.WalletNotFoundException;
import com.batuaa.UserProfile.model.Wallet;
import com.batuaa.UserProfile.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Slf4j
@RestController
@RequestMapping("wallet/api/v1")
public class WalletController {
@Autowired
    private final WalletService walletService;
@Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    // Link Bank Account
    @PostMapping("/link-bank-account")
    public ResponseEntity<ApiResponse> linkBankAccountToWallet(@RequestBody WalletDto wallet) {
        String walletId = walletService.linkBankAccountToWallet(wallet);
        return ResponseEntity.status(201)
                .body(new ApiResponse("success", "Wallet generated successfully", walletId));
    }

    // Add Money to Wallet
    @PostMapping("/add-money")
    public ResponseEntity<ApiResponse> addMoneyToWallet(
            @RequestParam String walletId,
            @RequestParam BigDecimal amount) {

        String message = walletService.updateMoneyFromBank(walletId, amount);
        return ResponseEntity.ok(new ApiResponse("success", message));
    }

    //Get Wallet Details
    @GetMapping("/details")
    public ResponseEntity<ApiResponse> getWalletDetails(
            @RequestParam String email,
            @RequestParam String walletId) {

        Wallet wallet = walletService.getWalletDetails(email, walletId);
       log.info("Wallet balance: "+wallet.getBalance());
        return ResponseEntity.ok(new ApiResponse("success", "Wallet details fetched successfully", wallet));
    }
}


