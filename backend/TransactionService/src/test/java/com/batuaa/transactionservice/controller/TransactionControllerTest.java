package com.batuaa.transactionservice.controller;

import com.batuaa.transactionservice.dto.TransactionDateRangeDto;
import com.batuaa.transactionservice.dto.TransactionTypeDto;
import com.batuaa.transactionservice.exception.EmptyTransactionListException;
import com.batuaa.transactionservice.exception.GlobalExceptionHandler;
import com.batuaa.transactionservice.exception.WalletNotFoundException;
import com.batuaa.transactionservice.model.Status;
import com.batuaa.transactionservice.model.Transaction;
import com.batuaa.transactionservice.model.Type;
import com.batuaa.transactionservice.model.Wallet;
import com.batuaa.transactionservice.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class TransactionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private List<Transaction> transactions;
    private TransactionTypeDto transactionTypeDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(transactionController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .alwaysDo(result -> System.out.println("Response: " + result.getResponse().getContentAsString()))
                .build();

        // Mock transaction data
        transactions = List.of(
                new Transaction(15, new Wallet("WAL0DC01EF4"), new Wallet("WAL2ED73EBA"),
                        null, null,
                        new BigDecimal("500.00"),
                        LocalDateTime.of(2025, 10, 15, 15, 27, 48),
                        Status.SUCCESS,
                        "Received Rs500 from wallet WAL4DA22CC6",
                        Type.RECEIVED),

                new Transaction(16, new Wallet("WAL0DC01EF4"), new Wallet("WAL2ED73EBA"),
                        null, null,
                        new BigDecimal("200.00"),
                        LocalDateTime.of(2025, 10, 15, 15, 28, 15),
                        Status.SUCCESS,
                        "Transferred Rs200 to wallet WAL4DE74491",
                        Type.WITHDRAWN),

                new Transaction(17, new Wallet("WAL0DC01EF4"), new Wallet("WAL4C9D9DC6"),
                        null, null,
                        new BigDecimal("200.00"),
                        LocalDateTime.of(2025, 10, 15, 15, 28, 15),
                        Status.SUCCESS,
                        "Received Rs200 from wallet WAL4DA22CC6",
                        Type.RECEIVED),

                new Transaction(18, new Wallet("WAL0DC01EF4"), new Wallet("WAL4DA22CC6"),
                        null, null,
                        new BigDecimal("500.00"),
                        LocalDateTime.of(2025, 10, 16, 0, 55, 55),
                        Status.SUCCESS,
                        "Money added to wallet WALF729C2F",
                        Type.WITHDRAWN)
        );

        // Mock DTO for type filtering
        transactionTypeDto = new TransactionTypeDto();
        transactionTypeDto.setWalletId("WAL0DC01EF4");
        transactionTypeDto.setEmailId("bhoolinagar@gmail.com");
        transactionTypeDto.setType(Type.RECEIVED);
    }

    //  SUCCESS TEST WITH REAL DATA
    @Test
    void filterTransactionsByDate_success() throws Exception {
        when(transactionService.findByWalletIdAndDateBetween(any(TransactionDateRangeDto.class)))
                .thenReturn(transactions);

        String json = """
                {
                  "walletId": "WAL0DC01EF4",
                  "emailId": "bhoolinagar@gmail.com",
                  "startDate": "2025-10-01",
                  "endDate": "2025-10-16",
                  "page": 0
                }
                """;
        mockMvc.perform(post("/transaction/api/v2/filter-by-date")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Transactions fetched successfully"))
                .andExpect(jsonPath("$.data[0].transactionId").value(15))
                .andExpect(jsonPath("$.data[0].amount").value(500.00))
                .andExpect(jsonPath("$.data[0].remarks").value("Received Rs500 from wallet WAL4DA22CC6"))
                .andExpect(jsonPath("$.data[1].transactionId").value(16))
                .andExpect(jsonPath("$.data[1].type").value("WITHDRAWN"));
    }

    // WALLET NOT FOUND
    @Test
    void filterTransactionsByDate_walletNotFound() throws Exception {
        when(transactionService.findByWalletIdAndDateBetween(any()))
                .thenThrow(new WalletNotFoundException("Wallet not found: WAL999"));

        String json = """
                {
                  "walletId": "WAL999",
                  "emailId": "test@example.com",
                  "startDate": "2025-10-01",
                  "endDate": "2025-10-10",
                  "page": 0
                }
                """;

        mockMvc.perform(post("/transaction/api/v2/filter-by-date")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Wallet not found: WAL999"));
    }

    // EMPTY TRANSACTION LIST
    @Test
    void filterTransactionsByDate_emptyTransactionList() throws Exception {
        when(transactionService.findByWalletIdAndDateBetween(any()))
                .thenThrow(new EmptyTransactionListException("No transactions found for the given date range."));

        String json = """
                {
                  "walletId": "WAL0DC01EF4",
                  "emailId": "bhoolinagar@gmail.com",
                  "startDate": "2025-10-01",
                  "endDate": "2025-10-05",
                  "page": 0
                }
                """;

        mockMvc.perform(post("/transaction/api/v2/filter-by-date")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("No transactions found for the given date range."));
    }

    // INVALID DATE RANGE
    @Test
    void filterTransactionsByDate_invalidDateRange() throws Exception {
        when(transactionService.findByWalletIdAndDateBetween(any()))
                .thenThrow(new DateTimeException("Start date cannot be after end date"));

        String json = """
                {
                  "walletId": "WAL0DC01EF4",
                  "emailId": "bhoolinagar@gmail.com",
                  "startDate": "2025-10-20",
                  "endDate": "2025-10-10",
                  "page": 0
                }
                """;

        mockMvc.perform(post("/transaction/api/v2/filter-by-date")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Start date cannot be after end date"));
    }

    //  FUTURE DATE TEST
    @Test
    void filterTransactionsByDate_futureDates() throws Exception {
        when(transactionService.findByWalletIdAndDateBetween(any()))
                .thenThrow(new DateTimeException("Start date cannot be in the future, End date cannot be in the future"));

        String json = """
                {
                  "walletId": "WAL0DC01EF4",
                  "emailId": "bhoolinagar@gmail.com",
                  "startDate": "2050-10-01",
                  "endDate": "2050-10-10",
                  "page": 0
                }
                """;

        mockMvc.perform(post("/transaction/api/v2/filter-by-date")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Start date cannot be in the future, End date cannot be in the future"));
    }


    @Test
    void filterTransactionsByType_success() throws Exception {
        when(transactionService.viewTransactionsByType(any(TransactionTypeDto.class)))
                .thenReturn(List.of(transactions.get(0), transactions.get(2))); // Only RECEIVED

        String jsonRequest = """
                {
                  "walletId": "WAL0DC01EF4",
                  "emailId": "bhoolinagar@gmail.com",
                  "type": "RECEIVED"
                }
                """;

        mockMvc.perform(post("/transaction/api/v2/filter-by-type")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Transactions fetched successfully"))
                .andExpect(jsonPath("$.data[0].transactionId").value(15))
                .andExpect(jsonPath("$.data[0].type").value("RECEIVED"))
                .andExpect(jsonPath("$.data[1].transactionId").value(17))
                .andExpect(jsonPath("$.data[1].type").value("RECEIVED"));

        verify(transactionService, times(1)).viewTransactionsByType(any());
    }

    @Test
    void filterTransactionsByType_withdrawal_success() throws Exception {
        // Mock service to return only WITHDRAWN transactions
        when(transactionService.viewTransactionsByType(any(TransactionTypeDto.class)))
                .thenReturn(List.of(transactions.get(3))); // Only WITHDRAWN

        String jsonRequest = """
                {
                  "walletId": "WAL0DC01EF4",
                  "emailId": "bhoolinagar@gmail.com",
                  "type": "WITHDRAWN"
                }
                """;

        mockMvc.perform(post("/transaction/api/v2/filter-by-type")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Transactions fetched successfully"))
                .andExpect(jsonPath("$.data[0].transactionId").value(18))
                .andExpect(jsonPath("$.data[0].type").value("WITHDRAWN"))
                .andExpect(jsonPath("$.data[0].amount").value(500.00))
                .andExpect(jsonPath("$.data[0].remarks").value("Money added to wallet WALF729C2F"));

        verify(transactionService, times(1)).viewTransactionsByType(any());
    }


    @Test
    void filterTransactionsByType_emptyList() throws Exception {
        when(transactionService.viewTransactionsByType(any(TransactionTypeDto.class)))
                .thenReturn(Collections.emptyList());

        String jsonRequest = """
                {
                  "walletId": "WAL0DC01EF4",
                  "emailId": "bhoolinagar@gmail.com",
                  "type": "RECEIVED"
                }
                """;

        mockMvc.perform(post("/transaction/api/v2/filter-by-type")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("No transactions found"))
                .andExpect(jsonPath("$.data").isEmpty());

        verify(transactionService, times(1)).viewTransactionsByType(any());
    }
}
