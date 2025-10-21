package com.batuaa.transactionservice.service;

import com.batuaa.transactionservice.dto.TransactionRemarkDto;
import com.batuaa.transactionservice.dto.TransferDto;
import com.batuaa.transactionservice.exception.*;
import com.batuaa.transactionservice.model.*;
import com.batuaa.transactionservice.repository.TransactionRepository;
import com.batuaa.transactionservice.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TranscationSerivceImp transactionService;


    private Wallet walletFrom;
    private Wallet walletTo;
    private TransferDto transferDto;
    private Buyer buyerFrom;
    private Buyer buyerTo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // buyer
        buyerFrom = new Buyer();
        buyerFrom.setEmailId("from@example.com");
        buyerTo = new Buyer();
        buyerTo.setEmailId("to@example.com");

        // wallets
        walletFrom = new Wallet();
        walletFrom.setWalletId("WALLET_FROM");
        walletFrom.setBuyer(buyerFrom);
        walletFrom.setBalance(new BigDecimal("1000.00"));

        walletTo = new Wallet();
        walletTo.setWalletId("WALLET_TO");
        walletTo.setBuyer(buyerTo);
        walletTo.setBalance(new BigDecimal("500.00"));

        // TransferDTO
        transferDto = new TransferDto();
        transferDto.setFromWalletId("WALLET_FROM");
        transferDto.setToWalletId("WALLET_TO");
        transferDto.setAmount(new BigDecimal("200.00"));
    }

    /* Tests for wallet to wallet transfer:
    1) Successful transaction (checks balances, status, remarks, ID)
    2) Sender/receiver wallet not found
    3) Duplicate transaction
    4) Insufficient funds
    5) Unexpected DB exception (UnableToAddMoneyException)
     */
    @Test
    void testTransferWalletToWallet_GeneratesTransactionId_And_UpdatesStatus() {
        // Arrange
        when(walletRepository.findByWalletId("WALLET_FROM")).thenReturn(Optional.of(walletFrom));
        when(walletRepository.findByWalletId("WALLET_TO")).thenReturn(Optional.of(walletTo));

        when(transactionRepository.findByFromWalletAndToWalletAndAmountAndStatus(
                any(), any(), any(), any())).thenReturn(Optional.empty());

        doAnswer(invocation -> {
            Transaction tx = invocation.getArgument(0);
            if (tx.getTransactionId() == null) {
                tx.setTransactionId((int) (Math.random() * 1000));
            }
            return tx;
        }).when(transactionRepository).save(any(Transaction.class));

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);

        Transaction result = transactionService.transferWalletToWallet(transferDto);

        assertNotNull(result.getTransactionId(), "Transaction ID should be generated");
        assertEquals(Status.SUCCESS, result.getStatus(), "Final status should be SUCCESS");
        assertEquals("Transferred Rs 200.00 to wallet WALLET_TO", result.getRemarks());

        verify(transactionRepository, atLeast(3)).save(transactionCaptor.capture());
        List<Transaction> allSaved = transactionCaptor.getAllValues();

        assertTrue(allSaved.stream().anyMatch(t -> t.getStatus() == Status.SUCCESS),
                "There should be a SUCCESS transaction saved");

        assertEquals(new BigDecimal("800.00"), walletFrom.getBalance());
        assertEquals(new BigDecimal("700.00"), walletTo.getBalance());
    }

    @Test
    void testTransferWalletToWallet_SenderWalletNotFound() {

        when(walletRepository.findByWalletId("WALLET_FROM")).thenReturn(Optional.empty());

        WalletNotFoundException exception = assertThrows(
                WalletNotFoundException.class,
                () -> transactionService.transferWalletToWallet(transferDto)
        );

        assertEquals("Sender wallet not found: WALLET_FROM", exception.getMessage());
    }
    @Test
    void testTransferWalletToWallet_ReceiverWalletNotFound() {

        when(walletRepository.findByWalletId("WALLET_FROM")).thenReturn(Optional.of(walletFrom));
        when(walletRepository.findByWalletId("WALLET_TO")).thenReturn(Optional.empty());

        WalletNotFoundException exception = assertThrows(
                WalletNotFoundException.class,
                () -> transactionService.transferWalletToWallet(transferDto)
        );

        assertEquals("Receiver wallet not found: WALLET_TO", exception.getMessage());
    }

    @Test
    void testTransferWalletToWallet_DuplicateTransaction() {

        when(walletRepository.findByWalletId("WALLET_FROM")).thenReturn(Optional.of(walletFrom));
        when(walletRepository.findByWalletId("WALLET_TO")).thenReturn(Optional.of(walletTo));

        Transaction duplicateTx = new Transaction();
        when(transactionRepository.findByFromWalletAndToWalletAndAmountAndStatus(
                any(), any(), any(), any())).thenReturn(Optional.of(duplicateTx));

        DuplicateTransactionException exception = assertThrows(
                DuplicateTransactionException.class,
                () -> transactionService.transferWalletToWallet(transferDto)
        );

        assertEquals("A similar transaction is already in process from WALLET_FROM to WALLET_TO",
                exception.getMessage());
    }
    @Test
    void testTransferWalletToWallet_InsufficientFunds() {

        walletFrom.setBalance(new BigDecimal("100")); // Less than transfer amount
        when(walletRepository.findByWalletId("WALLET_FROM")).thenReturn(Optional.of(walletFrom));
        when(walletRepository.findByWalletId("WALLET_TO")).thenReturn(Optional.of(walletTo));
        when(transactionRepository.findByFromWalletAndToWalletAndAmountAndStatus(
                any(), any(), any(), any())).thenReturn(Optional.empty());

        InsufficientFundsException exception = assertThrows(
                InsufficientFundsException.class,
                () -> transactionService.transferWalletToWallet(transferDto)
        );

        assertEquals("Insufficient funds in sender wallet", exception.getMessage());
    }

    // Negative test for unexpected exception
    @Test
    void testTransferWalletToWallet_UnexpectedError() {
        // Arrange
        when(walletRepository.findByWalletId("WALLET_FROM")).thenReturn(Optional.of(walletFrom));
        when(walletRepository.findByWalletId("WALLET_TO")).thenReturn(Optional.of(walletTo));
        when(transactionRepository.findByFromWalletAndToWalletAndAmountAndStatus(any(), any(), any(), any()))
                .thenReturn(Optional.empty());

        doAnswer(invocation -> {
            Transaction tx = invocation.getArgument(0);
            if (tx.getStatus() == Status.PROCESSING) {
                throw new RuntimeException("DB down");
            }
            return tx;
        }).when(transactionRepository).save(any(Transaction.class));

        UnableToAddMoneyException ex = assertThrows(UnableToAddMoneyException.class,
                () -> transactionService.transferWalletToWallet(transferDto));

        assertTrue(ex.getMessage().contains("Error occurred while transferring money"));
        assertTrue(ex.getCause() instanceof RuntimeException);
        assertEquals("DB down", ex.getCause().getMessage());
    }
    /*
    1) Successful fetch with results
    2) No transactions found : TransactionNotFoundException
    3) Unexpected error :  UnableToFilterByRemarkException
     */
    @Test
    void testFilterTransactionsByRemark_success() {

        TransactionRemarkDto dto = new TransactionRemarkDto();
        dto.setWalletId("WALLET_FROM");
        dto.setEmailId("from@example.com");
        dto.setRemark("test");

        Transaction tx = new Transaction();
        tx.setTransactionId(1);
        tx.setRemarks("test remark");

        when(transactionRepository.findByWalletAndEmailAndRemarkNative(
                "WALLET_FROM", "from@example.com", "test")).thenReturn(List.of(tx));

        List<Transaction> result = transactionService.filterTransactionsByRemark(dto);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("test remark", result.get(0).getRemarks());
    }
    @Test
    void testFilterTransactionsByRemark_noTransactionsFound() {

        TransactionRemarkDto dto = new TransactionRemarkDto();
        dto.setWalletId("WALLET_FROM");
        dto.setEmailId("from@example.com");
        dto.setRemark("unknown");

        when(transactionRepository.findByWalletAndEmailAndRemarkNative(
                "WALLET_FROM", "from@example.com", "unknown")).thenReturn(List.of());

        TransactionNotFoundException ex = assertThrows(TransactionNotFoundException.class,
                () -> transactionService.filterTransactionsByRemark(dto));
        assertEquals("No transaction found for wallet WALLET_FROM with remark: unknown", ex.getMessage());
    }

    @Test
    void testFilterTransactionsByRemark_ThrowsUnableToFilterByRemarkException() {

        TransactionRemarkDto dto = new TransactionRemarkDto();
        dto.setWalletId("wallet123");
        dto.setRemark("testRemark");
        dto.setEmailId("test@example.com");

        when(transactionRepository.findByWalletAndEmailAndRemarkNative(
                dto.getWalletId(),
                dto.getEmailId(),
                dto.getRemark()))
                .thenThrow(new UnableToFilterByRemarkException("DB error"));

        assertThrows(UnableToFilterByRemarkException.class, () ->
                transactionService.filterTransactionsByRemark(dto)
        );
    }

}


