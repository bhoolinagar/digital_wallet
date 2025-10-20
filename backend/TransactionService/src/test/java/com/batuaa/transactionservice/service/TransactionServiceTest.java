package com.batuaa.transactionservice.service;

import com.batuaa.transactionservice.dto.TransactionDateRangeDto;
import com.batuaa.transactionservice.dto.TransactionTypeDto;
import com.batuaa.transactionservice.exception.EmptyTransactionListException;
import com.batuaa.transactionservice.exception.WalletNotFoundException;
import com.batuaa.transactionservice.model.Buyer;
import com.batuaa.transactionservice.model.Transaction;
import com.batuaa.transactionservice.model.Type;
import com.batuaa.transactionservice.model.Wallet;
import com.batuaa.transactionservice.repository.TransactionRepository;
import com.batuaa.transactionservice.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TranscationSerivceImp transactionService;

    private TransactionDateRangeDto dateRangeDto;
    private TransactionTypeDto transactionTypeDto;
    private Wallet testWallet;
    private Transaction tx1;
    private Transaction tx2;
    private List<Transaction> transactions;
    private Transaction tx3;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Wallet setup
        testWallet = new Wallet();
        testWallet.setWalletId("WAL0DC01EF4");
        // Date range DTO
        dateRangeDto = new TransactionDateRangeDto();
        dateRangeDto.setWalletId("WAL0DC01EF4");
        dateRangeDto.setEmailId("bhoolinagar@gmail.com");
        dateRangeDto.setStartDate(LocalDate.of(2025, 10, 1));
        dateRangeDto.setEndDate(LocalDate.of(2025, 10, 10));
        dateRangeDto.setPage(0);

        // Transaction type DTO
        transactionTypeDto = new TransactionTypeDto();
        transactionTypeDto.setWalletId("WAL0DC01EF4");
        transactionTypeDto.setEmailId("bhoolinagar@gmail.com");
        transactionTypeDto.setType(Type.RECEIVED);
/*
        // transactions
        tx1 = new Transaction(15, new BigDecimal("500.00"), "Received Rs500 from wallet WAL4DA22CC6",
                "SUCCESS", LocalDateTime.of(2025, 10, 2, 10, 0), null,
                new Wallet("WAL0DC01EF4"), new Wallet("WAL2ED73EBA"),
                "bhoolinagar@gmail.com", "anjali@gmail.com", Type.RECEIVED);

        tx2 = new Transaction(16, new BigDecimal("200.00"), "Received Rs200 from wallet WAL4C9D9DC6",
                "SUCCESS", LocalDateTime.of(2025, 10, 3, 10, 0), null,
                new Wallet("WAL0DC01EF4"), new Wallet("WAL4C9D9DC6"),
                "bhoolinagar@gmail.com", "khyati@gmail.com", Type.RECEIVED);

        transactions = List.of(tx1, tx2);

 */

        // TransactionType DTO
        transactionTypeDto = new TransactionTypeDto();
        transactionTypeDto.setWalletId("WAL0DC01EF4");
        transactionTypeDto.setEmailId("bhoolinagar@gmail.com");
        transactionTypeDto.setType(Type.RECEIVED);

        // Date range DTO
        dateRangeDto = new TransactionDateRangeDto();
        dateRangeDto.setWalletId("WAL0DC01EF4");
        dateRangeDto.setEmailId("bhoolinagar@gmail.com");
        dateRangeDto.setStartDate(LocalDate.of(2025, 10, 1));
        dateRangeDto.setEndDate(LocalDate.of(2025, 10, 10));
        dateRangeDto.setPage(0);

        // Wallets
        Wallet wallet1 = new Wallet("WAL0DC01EF4");
        Wallet wallet2 = new Wallet("WAL2ED73EBA");
        Wallet wallet3 = new Wallet("WAL4C9D9DC6");
        Wallet wallet4 = new Wallet("WAL4DA22CC6");
        // Buyers
        Buyer fromBuyer1 = new Buyer();
        fromBuyer1.setEmailId("bhoolinagar@gmail.com");
        Buyer toBuyer1 = new Buyer();
        toBuyer1.setEmailId("anjali@gmail.com");

        Buyer fromBuyer2 = new Buyer();
        fromBuyer2.setEmailId("bhoolinagar@gmail.com");
        Buyer toBuyer2 = new Buyer();
        toBuyer2.setEmailId("khyati@gmail.com");
        Buyer toBuyer3 = new Buyer();
        toBuyer3.setEmailId("antima@gmail.com");
        // Transactions
        tx1 = new Transaction(
                15,
                wallet1,
                wallet2,
                fromBuyer1,
                toBuyer1,
                new BigDecimal("500.00"),
                LocalDateTime.of(2025, 10, 2, 10, 0),
                null,
                "Received Rs500 from wallet WAL4DA22CC6",
                Type.RECEIVED
        );

        tx2 = new Transaction(
                16,
                wallet1,
                wallet3,
                fromBuyer2,
                toBuyer2,
                new BigDecimal("200.00"),
                LocalDateTime.of(2025, 10, 3, 10, 0),
                null,
                "Received Rs200 from wallet WAL4C9D9DC6",
                Type.RECEIVED
        );
        tx3 = new Transaction(
                18, wallet1, wallet4, fromBuyer1, toBuyer3,
                new BigDecimal("500.00"), LocalDateTime.of(2025, 10, 16, 0, 55),
                null, "Money added to wallet WALF729C2F", Type.WITHDRAWN
        );
        // List of transactions
        transactions = List.of(tx1, tx2);
    }

    // ===================== Date Range Tests =====================

    @Test
    void testWalletNotFound() {
        when(walletRepository.existsByIdAndEmailId(anyString(), anyString())).thenReturn(false);

        WalletNotFoundException exception = assertThrows(WalletNotFoundException.class,
                () -> transactionService.findByWalletIdAndDateBetween(dateRangeDto));

        assertEquals("Wallet not found: WAL0DC01EF4", exception.getMessage());
    }

    @Test
    void testStartDateAfterEndDate() {
        dateRangeDto.setStartDate(LocalDate.of(2025, 10, 11));
        dateRangeDto.setEndDate(LocalDate.of(2025, 10, 10));

        when(walletRepository.existsByIdAndEmailId(anyString(), anyString())).thenReturn(true);

        DateTimeException exception = assertThrows(DateTimeException.class,
                () -> transactionService.findByWalletIdAndDateBetween(dateRangeDto));

        assertEquals("Start date cannot be after end date.", exception.getMessage());
    }

    @Test
    void testFutureDate() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        dateRangeDto.setStartDate(futureDate);
        dateRangeDto.setEndDate(futureDate);

        when(walletRepository.existsByIdAndEmailId(anyString(), anyString())).thenReturn(true);

        DateTimeException exception = assertThrows(DateTimeException.class,
                () -> transactionService.findByWalletIdAndDateBetween(dateRangeDto));

        assertEquals("Start and end dates must not be in the future.", exception.getMessage());
    }

    @Test
    void testEmptyTransactionList() {
        when(walletRepository.existsByIdAndEmailId(anyString(), anyString())).thenReturn(true);

        Page<Transaction> emptyPage = new PageImpl<>(Collections.emptyList());
        when(transactionRepository.findByWalletAndTimestampRange(anyString(), any(), any(), any())).thenReturn(emptyPage);

        EmptyTransactionListException exception = assertThrows(EmptyTransactionListException.class,
                () -> transactionService.findByWalletIdAndDateBetween(dateRangeDto));

        assertEquals("No transactions found for the given date range.", exception.getMessage());
    }

    @Test
    void testValidTransactionList() {
        when(walletRepository.existsByIdAndEmailId(anyString(), anyString())).thenReturn(true);

        Page<Transaction> page = new PageImpl<>(transactions);
        when(transactionRepository.findByWalletAndTimestampRange(anyString(), any(), any(), any())).thenReturn(page);

        List<Transaction> result = transactionService.findByWalletIdAndDateBetween(dateRangeDto);

        assertEquals(2, result.size());
        // Ensure descending order by timestamp
        assertTrue(result.get(0).getTimestamp().isAfter(result.get(1).getTimestamp()));
    }

    // ===================== Filter by Type Tests =====================

    @Test
    void testViewTransactionsByType_success() {
        // Wallet exists
        when(walletRepository.existsByIdAndEmailId(transactionTypeDto.getWalletId(), transactionTypeDto.getEmailId()))
                .thenReturn(true);

        // Mock repository call
        when(transactionRepository.findByToWalletIdAndType(
                transactionTypeDto.getWalletId(),

                transactionTypeDto.getType()))
                .thenReturn(List.of(tx1));

        // When
        List<Transaction> result = transactionService.viewTransactionsByType(transactionTypeDto);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Type.RECEIVED, result.get(0).getType());
        assertEquals("WAL0DC01EF4", result.get(0).getFromWallet().getWalletId());
        assertEquals("Received Rs500 from wallet WAL4DA22CC6", result.get(0).getRemarks());

        // Verify repository call
        verify(transactionRepository, times(1))
                .findByToWalletIdAndType(
                        transactionTypeDto.getWalletId(),

                        transactionTypeDto.getType());
    }

    //for check filter by withrowa

    @Test
    void testViewTransactionsByType_emptyList() {
        when(walletRepository.existsByIdAndEmailId(transactionTypeDto.getWalletId(), transactionTypeDto.getEmailId()))
                .thenReturn(true);

        when(transactionRepository.findByToWalletIdAndType(
                transactionTypeDto.getWalletId(),
                transactionTypeDto.getType()))
                .thenReturn(Collections.emptyList());

        EmptyTransactionListException exception = assertThrows(
                EmptyTransactionListException.class,
                () -> transactionService.viewTransactionsByType(transactionTypeDto)
        );

        assertEquals("No transactions found for wallet: WAL0DC01EF4 and type: RECEIVED", exception.getMessage());

        verify(transactionRepository, times(1))
                .findByToWalletIdAndType(
                        transactionTypeDto.getWalletId(),
                        transactionTypeDto.getType());
    }


    @Test
    void testViewTransactionsByType_withdrawal_success() {
        // Set up TransactionTypeDto for WITHDRAWN
        TransactionTypeDto withdrawalDto = new TransactionTypeDto();
        withdrawalDto.setWalletId("WAL0DC01EF4");
        withdrawalDto.setEmailId("bhoolinagar@gmail.com");
        withdrawalDto.setType(Type.WITHDRAWN);

        // Mock wallet existence
        when(walletRepository.existsByIdAndEmailId(withdrawalDto.getWalletId(), withdrawalDto.getEmailId()))
                .thenReturn(true);

        // Mock transactionRepository to return only WITHDRAWN transaction
        when(transactionRepository.findByFromWalletIdAndType(withdrawalDto.getWalletId(), Type.WITHDRAWN))
                .thenReturn(List.of(tx3)); // WITHDRAWN transaction

        // Call service method
        List<Transaction> result = transactionService.viewTransactionsByType(withdrawalDto);

        // Assertions
        assertEquals(1, result.size());
        assertEquals(Type.WITHDRAWN, result.get(0).getType());
        assertEquals(18, result.get(0).getTransactionId());
        assertEquals(new BigDecimal("500.00"), result.get(0).getAmount());
        assertEquals("Money added to wallet WALF729C2F", result.get(0).getRemarks());

        // Verify repository call
        verify(transactionRepository, times(1))
                .findByFromWalletIdAndType(withdrawalDto.getWalletId(), Type.WITHDRAWN);
    }


}
