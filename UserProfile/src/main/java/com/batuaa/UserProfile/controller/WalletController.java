package com.batuaa.UserProfile.controller;

import com.batuaa.UserProfile.Dto.WalletDto;
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
    private WalletService walletService;

    // BigDecimal getBalance( String walletId)
    @GetMapping("/get-wallet-details")
    public ResponseEntity<?> getWalletDetails(@RequestParam String emailId, @RequestParam String walletId) {
        return ResponseEntity.ok(new Wallet());

    }

    // to link new bank account
    @PostMapping("/link-bank-account")
    public ResponseEntity<?> linkBankAccountToWallet(@RequestBody WalletDto wallet) {
        return ResponseEntity.ok(" Wallet generated sucessfully");
    }

    // to add more money from bank
    @PostMapping("/add-money-to-wallet")
    public ResponseEntity<?> updateMoneyFromBank(@RequestParam String walletId, @RequestParam BigDecimal amount) {
        //: String (update wallet balance)
        return ResponseEntity.ok("Money sucessfully added into wallet");
    }
}


