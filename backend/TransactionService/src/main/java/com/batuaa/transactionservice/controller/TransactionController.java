package com.batuaa.transactionservice.controller;

import com.batuaa.transactionservice.dto.*;
import com.batuaa.transactionservice.model.Transaction;
import com.batuaa.transactionservice.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController

@RequestMapping("/transaction/api/v2")
public class TransactionController {

//    @Autowired
    private final TransactionService transactionService;
    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

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
    @PostMapping("/transfer-wallet")
    public ResponseEntity<?> transferWalletToWallet(@Valid @RequestBody TransferDto transferDto) {
        log.info("Initiating wallet-to-wallet transfer: {}", transferDto);
        Transaction transaction = transactionService.transferWalletToWallet(transferDto);
        return ResponseEntity.ok(ApiResponse.builder()
                .statusCode(200)
                .message("Transaction completed successfully")
                .data(transaction).build());
    }

    /**
     * View transaction by remark and transactionId
     */
//    @GetMapping("/view-transactions-by-remark")
//    public ResponseEntity<?> viewTransactionByRemark(@RequestBody TransactionRemarkDto transactionRemarkDto) {
//        log.info("Fetching transaction by remark: {}", transactionRemarkDto.getRemark());
//        return ResponseEntity.ok(transactionService.filterTransactionsByRemark(transactionRemarkDto ));
//    }
//    HTTP GET doesn’t support a body, so @RequestBody TransactionRemarkDto won’t work
//     solution figured was to use @ModelAttribute, which binds query parameters to a DTO automatically.
    @GetMapping("/view-transactions-by-remark")
    public ResponseEntity<?> viewTransactionByRemark(
            @Valid @ModelAttribute TransactionRemarkDto transactionRemarkDto) {

        log.info("Filtering transactions by remark: {}"
                ,transactionRemarkDto.getRemark());
        List<Transaction> transactions = transactionService.filterTransactionsByRemark(transactionRemarkDto);

        return ResponseEntity.ok(ApiResponse.builder()
                .statusCode(200)
                .message("Filtered transactions successfully")
                .data(transactions)
                .build());
    }

    /**
     * View transactions by type (DEBIT or CREDIT)
     */
    @PostMapping("/filter-by-type")
    public ResponseEntity<ApiResponse> filterByType(
            @Valid @RequestBody TransactionTypeDto transactionTypeDto) {

        log.info("Filtering transactions for wallet {} by type {}",
                transactionTypeDto.getWalletId(), transactionTypeDto.getType());

        List<Transaction> transactions = transactionService.viewTransactionsByType(transactionTypeDto);

        ApiResponse response = new ApiResponse(
                200,
                transactions.isEmpty()
                        ? "No transactions found for the selected type"
                        : "Transactions fetched successfully",
                transactions
        );

        return ResponseEntity.ok(response);
    }

    /**
     * View transaction history between startDate and endDate for a wallet
     */

    @PostMapping("/transactions/filter-by-date")
    public ResponseEntity<ApiResponse> filterTransactionsByDate(
            @Valid @RequestBody TransactionDateRangeDto dto) {
        List<Transaction> transactions = transactionService.findByWalletIdAndDateBetween(dto);
        ApiResponse response = new ApiResponse(200, "Transactions fetched successfully", transactions);
        return ResponseEntity.ok(response);
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
