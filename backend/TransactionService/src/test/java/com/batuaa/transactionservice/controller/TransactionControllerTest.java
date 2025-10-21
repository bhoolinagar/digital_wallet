package com.batuaa.transactionservice.controller;

import com.batuaa.transactionservice.dto.TransactionDateRangeDto;
import com.batuaa.transactionservice.dto.TransactionRemarkDto;
import com.batuaa.transactionservice.dto.TransactionTypeDto;
import com.batuaa.transactionservice.exception.EmptyTransactionListException;
import com.batuaa.transactionservice.exception.GlobalExceptionHandler;
import com.batuaa.transactionservice.exception.WalletNotFoundException;
import com.batuaa.transactionservice.model.*;
import com.batuaa.transactionservice.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
public class TransactionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private List<Transaction> transactions;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(transactionController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .alwaysDo(result -> System.out.println("Response: " + result.getResponse().getContentAsString()))
                .build();

        Wallet walletFrom = new Wallet();
        walletFrom.setWalletId("WAL0DC01EF4");

        Wallet walletTo1 = new Wallet();
        walletTo1.setWalletId("WAL2ED73EBA");

        Wallet walletTo2 = new Wallet();
        walletTo2.setWalletId("WAL4C9D9DC6");

        Wallet walletTo3 = new Wallet();
        walletTo3.setWalletId("WAL4DA22CC6");

        Buyer buyer1 = new Buyer();
        buyer1.setEmailId("bhoolinagar@gmail.com");

        Buyer buyer2 = new Buyer();
        buyer2.setEmailId("someoneelse@gmail.com");

        // Mock transaction list
        Transaction t1 = new Transaction();
        t1.setTransactionId(15);
        t1.setFromWallet(walletFrom);
        t1.setToWallet(walletTo1);
        t1.setFromBuyer(buyer1);
        t1.setToBuyer(buyer2);
        t1.setAmount(new BigDecimal("500.00"));
        t1.setTimestamp(LocalDateTime.now());
        t1.setStatus(Status.SUCCESS);
        t1.setRemarks("Received Rs500 from wallet WAL4DA22CC6");
        t1.setType(Type.RECEIVED);

        Transaction t2 = new Transaction();
        t2.setTransactionId(16);
        t2.setFromWallet(walletFrom);
        t2.setToWallet(walletTo1);
        t2.setFromBuyer(buyer1);
        t2.setToBuyer(buyer2);
        t2.setAmount(new BigDecimal("200.00"));
        t2.setTimestamp(LocalDateTime.now());
        t2.setStatus(Status.SUCCESS);
        t2.setRemarks("Transferred Rs200 to wallet WAL4DE74491");
        t2.setType(Type.WITHDRAWN);

        Transaction t3 = new Transaction();
        t3.setTransactionId(17);
        t3.setFromWallet(walletFrom);
        t3.setToWallet(walletTo2);
        t3.setFromBuyer(buyer1);
        t3.setToBuyer(buyer2);
        t3.setAmount(new BigDecimal("200.00"));
        t3.setTimestamp(LocalDateTime.now());
        t3.setStatus(Status.SUCCESS);
        t3.setRemarks("Received Rs200 from wallet WAL4DA22CC6");
        t3.setType(Type.RECEIVED);

        Transaction t4 = new Transaction();
        t4.setTransactionId(18);
        t4.setFromWallet(walletFrom);
        t4.setToWallet(walletTo3);
        t4.setFromBuyer(buyer1);
        t4.setToBuyer(buyer2);
        t4.setAmount(new BigDecimal("500.00"));
        t4.setTimestamp(LocalDateTime.now());
        t4.setStatus(Status.SUCCESS);
        t4.setRemarks("Money added to wallet WALF729C2F");
        t4.setType(Type.WITHDRAWN);

        transactions = List.of(t1, t2, t3, t4);
    }

    @Test
    void transferWalletToWallet_success() throws Exception {
        Transaction mockTransaction = transactions.get(0);
        when(transactionService.transferWalletToWallet(any())).thenReturn(mockTransaction);

        String jsonRequest = """
            {
                "fromWalletId": "WAL0DC01EF4",
                "toWalletId": "WAL2ED73EBA",
                "amount": 500.00
            }
            """;

        mockMvc.perform(post("/transaction/api/v2/transfer-wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Transaction completed successfully"))
                .andExpect(jsonPath("$.data.transactionId").value(15))
                .andExpect(jsonPath("$.data.amount").value(500.00))
                .andExpect(jsonPath("$.data.remarks").value("Received Rs500 from wallet WAL4DA22CC6"));

        verify(transactionService, times(1)).transferWalletToWallet(any());
    }

    @Test
    void transferWalletToWallet_walletNotFound() throws Exception {

        when(transactionService.transferWalletToWallet(any()))
                .thenThrow(new WalletNotFoundException("Wallet not found"));

        String jsonRequest = """
            {
                "fromWalletId": "INVALID",
                "toWalletId": "WAL2ED73EBA",
                "amount": 500.00
            }
            """;

        mockMvc.perform(post("/transaction/api/v2/transfer-wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("Wallet not found"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }


    @Test
    void viewTransactionByRemark_success() throws Exception {
        // Mock the service to return our transaction list
        when(transactionService.filterTransactionsByRemark(any(TransactionRemarkDto.class)))
                .thenReturn(transactions);

        mockMvc.perform(get("/transaction/api/v2/view-transactions-by-remark")
                        .param("walletId", "WAL0DC01EF4")
                        .param("emailId", "khyatib@gmail.com")
                        .param("remark", "Received"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Filtered transactions successfully"))
                .andExpect(jsonPath("$.data[0].transactionId").value(15))
                .andExpect(jsonPath("$.data[1].transactionId").value(16));

        verify(transactionService, times(1)).filterTransactionsByRemark(any(TransactionRemarkDto.class));
    }


    @Test
    void viewTransactionByRemark_emptyList() throws Exception {
        when(transactionService.filterTransactionsByRemark(any(TransactionRemarkDto.class)))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/transaction/api/v2/view-transactions-by-remark")
                        .param("walletId", "WAL0DC01EF4")
                        .param("emailId", "bhoolinagar@gmail.com")
                        .param("remark", "Nothing"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Filtered transactions successfully"))
                .andExpect(jsonPath("$.data").isEmpty());

        verify(transactionService, times(1))
                .filterTransactionsByRemark(any(TransactionRemarkDto.class));
    }

}