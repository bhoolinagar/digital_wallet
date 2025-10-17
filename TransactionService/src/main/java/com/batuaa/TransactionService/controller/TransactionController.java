package com.batuaa.TransactionService.controller;

import com.batuaa.TransactionService.dto.TransactionDateRangeDto;
import com.batuaa.TransactionService.dto.TransactionTypeDto;
import com.batuaa.TransactionService.dto.TransactionRemarkDto;
import com.batuaa.TransactionService.dto.TransferDto;
import com.batuaa.TransactionService.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction/api/v2")
public class TransactionController {
    private final TransactionService transactionService;

    /**
     * Get all transactions for a specific wallet (sorted by most recent)
     */
    @GetMapping("/all-transactions")
    public ResponseEntity<?> getAllTransactions(@RequestParam String emailId,@RequestParam String walletId) {
        log.info("Fetching all transactions for walletId: {}", walletId);
        return ResponseEntity.ok(transactionService.getAllTransactions(emailId,walletId));
    }

    /**
     * Transfer money from one wallet to another
     */
    @PostMapping("/transfer-wallet-to-wallet")
    public ResponseEntity<?> transferWalletToWallet(@Valid @RequestBody TransferDto transferDto) {
        log.info("Initiating wallet-to-wallet transfer: {}", transferDto);
        return ResponseEntity.ok(transactionService.transferWalletToWallet(transferDto));
    }

    /**
     * View transaction by remark and transactionId
     */
    @GetMapping("/view-transactions-by-remark")
    public ResponseEntity<?> viewTransactionByRemark(@RequestBody TransactionRemarkDto transactionRemarkDto) {
        log.info("Fetching transaction by remark: {}", transactionRemarkDto.getRemark());
        return ResponseEntity.ok(transactionService.filterTransactionsByRemark(transactionRemarkDto ));
    }

    /**
     * View transactions by type (DEBIT or CREDIT)
     */
    @PostMapping("/view-transactions-by-type")
    public ResponseEntity<?> viewTransactionByType(@Valid @RequestBody TransactionTypeDto transactionTypeDto) {
        log.info("Fetching transactions by type: {}", transactionTypeDto.getType());
        return ResponseEntity.ok(transactionService.viewTransactionsByType(transactionTypeDto));
    }

    /**
     * View transaction history between startDate and endDate for a wallet
     */
    @PostMapping("/view-transactions-by-date")
    public ResponseEntity<?> viewTransactionByDateRange(@Valid @RequestBody TransactionDateRangeDto transactionDateRangeDto) {
        log.info("Fetching transactions by date range: {}", transactionDateRangeDto);
        return ResponseEntity.ok(transactionService.findByWalletIdAndDateBetween(transactionDateRangeDto));
    }

    /**
     * View transactions sorted by amount (ascending or descending)
     */
    @GetMapping("/view-transactions-by-amount")
    public ResponseEntity<?> viewTransactionsByAmount(@RequestParam String walletId,
                                                      @RequestParam(defaultValue = "DESC") String sortOrder) {
        log.info("Fetching transactions by amount for walletId: {} sorted by: {}", walletId, sortOrder);
        return ResponseEntity.ok(transactionService.sortTransactionsByAmount(walletId, sortOrder));
    }
}
