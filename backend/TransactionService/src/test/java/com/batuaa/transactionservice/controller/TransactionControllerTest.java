package com.batuaa.transactionservice.controller;

import com.batuaa.transactionservice.dto.TransactionDateRangeDto;
import com.batuaa.transactionservice.dto.TransactionRemarkDto;
import com.batuaa.transactionservice.dto.TransactionTypeDto;
import com.batuaa.transactionservice.exception.AdminNotFoundException;
import com.batuaa.transactionservice.exception.EmptyTransactionListException;
import com.batuaa.transactionservice.exception.GlobalExceptionHandler;
import com.batuaa.transactionservice.exception.WalletNotFoundException;
import com.batuaa.transactionservice.model.*;
import com.batuaa.transactionservice.service.TransactionService;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@Slf4j
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private TransactionService transactionService;


    @InjectMocks
    private TransactionController transactionController;

    private List<Transaction> transactions;
    List<Transaction> transactionList;
    private TransactionTypeDto transactionTypeDto;
    private List<Transaction> transactions_k;
    private Wallet walletFrom;
    private Wallet walletTo;
    private Buyer fromBuyer;
    private Transaction senderTransaction;

    private List<Transaction> mockTransactions;
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
        transactions_k = List.of(t1, t2, t3, t4);


        // sakshi transactions list

        fromBuyer = new Buyer();
        fromBuyer.setEmailId("sakshi@gmail.com");

        walletFrom = new Wallet();
        walletFrom.setWalletId("wallet123");
        walletFrom.setBalance(new BigDecimal("1000"));

        walletTo = new Wallet();
        walletTo.setWalletId("wallet456");
        walletTo.setBalance(new BigDecimal("500"));

        // Sample sender transaction returned by service
        senderTransaction = new Transaction();
        senderTransaction.setTransactionId(1);
        senderTransaction.setFromWallet(walletFrom);
        senderTransaction.setToWallet(walletTo);
        senderTransaction.setAmount(new BigDecimal("200"));
        senderTransaction.setStatus(Status.SUCCESS);
        senderTransaction.setTimestamp(LocalDateTime.now());
        senderTransaction.setRemarks("Transferred Rs200 to wallet wallet456");
        senderTransaction.setFromBuyer(fromBuyer);

        transactionList = new ArrayList<>();
        transactionList.add(senderTransaction);


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


        mockTransactions = Arrays.asList(
                new Transaction(1, null, null, null, null,
                        new BigDecimal("200.00"), LocalDateTime.now(),
                        Status.SUCCESS, "OK", Type.RECEIVED),
                new Transaction(2, null, null, null, null,
                        new BigDecimal("500.00"), LocalDateTime.now(),
                        Status.SUCCESS, "OK", Type.RECEIVED)
        );
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
        Wallet walletFrom = new Wallet();
        walletFrom.setWalletId("WAL0DC01EF4");

        String json = """
                {
                  "walletId": "WAL999",
                  "emailId": "test@example.com",
                  "startDate": "2025-10-01",
                  "endDate": "2025-10-10",
                  "page": 0
                }
                """;
        Wallet walletTo1 = new Wallet();
        walletTo1.setWalletId("WAL2ED73EBA");

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
                .andExpect(result -> {
                    String message = JsonPath.read(result.getResponse().getContentAsString(), "$.message");
                    assertTrue(
                            message.contains("Start date cannot be in the future") &&
                                    message.contains("End date cannot be in the future")
                    );
                });
        //.andExpect(jsonPath("$.message").value("Start date cannot be in the future, End date cannot be in the future"));
        // .andExpect(jsonPath("$.message").value(new DateTimeException("Start date cannot be in the future, End date cannot be in the future")));
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
                .andExpect(jsonPath("$.message").value("No transactions found for the selected type"))
                .andExpect(jsonPath("$.data").isEmpty());

        verify(transactionService, times(1)).viewTransactionsByType(any());
    }


    //khyati test cases

    @Test
    void transferWalletToWallet_success() throws Exception {
        Transaction mockTransaction = transactions_k.get(0);
        when(transactionService.transferWalletToWallet(any())).thenReturn(mockTransaction);

        String jsonRequest = """
                {
                  "fromWalletId": "WAL1698231B2",
                  "toWalletId": "WALDC6299F84",
                  "fromBuyerEmailId": "pawan@gmail.com",
                  "toBuyerEmailId": "bhoolinagar@gmail.com",
                  "amount": 500.00,
                  "remarks": "for electricity bill"
                }
                """;

        mockMvc.perform(post("/transaction/api/v2/transfer-wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Transaction completed successfully"))
                .andExpect(jsonPath("$.data.transactionId").value(mockTransaction.getTransactionId()))
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
                .andExpect(jsonPath("$.status").value("fail"))

                .andExpect(result -> {
                    String message = JsonPath.read(result.getResponse().getContentAsString(), "$.message");
                    assertTrue(
                            message.contains("Sender email cannot be blank") &&
                                    message.contains("Receiver email cannot be blank")
                    );
                })
                //    .andExpect(jsonPath("$.message").value("Sender email cannot be blank, Receiver email cannot be blank"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }


    @Test
    void viewTransactionByRemark_success() throws Exception {
        when(transactionService.filterTransactionsByRemark(any(TransactionRemarkDto.class)))
                .thenReturn(transactions_k);

        String jsonRequest = """
                {
                    "walletId": "WAL0DC01EF4",
                    "emailId": "khyatib@gmail.com",
                    "remark": "Received"
                }
                """;

        mockMvc.perform(post("/transaction/api/v2/view-transactions-by-remark")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Filtered transactions successfully"))
                .andExpect(jsonPath("$.data[0].transactionId").value(15))
                .andExpect(jsonPath("$.data[1].transactionId").value(16));

        verify(transactionService, times(1)).filterTransactionsByRemark(any(TransactionRemarkDto.class));
    }


    @Test
    void viewTransactionByRemark_emptyList() throws Exception {
        when(transactionService.filterTransactionsByRemark(any(TransactionRemarkDto.class)))
                .thenReturn(Collections.emptyList());

        String jsonRequest = """
                {
                    "walletId": "WAL0DC01EF4",
                    "emailId": "bhoolinagar@gmail.com",
                    "remark": "Nothing"
                }
                """;

        mockMvc.perform(post("/transaction/api/v2/view-transactions-by-remark")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("No transactions found for the selected remark"))
                .andExpect(jsonPath("$.data").isEmpty());

        verify(transactionService, times(1))
                .filterTransactionsByRemark(any(TransactionRemarkDto.class));
    }

    // get all  transactions by buyer id
    @Test
    public void givenGetAllTransactionsForWalletIdThenShouldReturnListOfAllTransactionsForWalletId() {


        when(transactionService.getAllTransactions("sakshi@gmail.com", "wallet123")).thenReturn(transactionList);
        ResponseEntity<?> response = transactionController.getAllTransactions("sakshi@gmail.com", "wallet123");
        ;
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactionList, response.getBody());
        verify(transactionService, times(1)).getAllTransactions("sakshi@gmail.com", "wallet123");


    }


    // TEST CASE 1: SUCCESS
    @Test
    void testGetAllTransactionsByAdmin_Success() throws Exception {


        when(transactionService.findByAdminEmailAndRole(anyString(), any(Role.class)))
                .thenReturn(transactions);
        //admin@gmail.com/ADMIN
        mockMvc.perform(get("/transaction/api/v2/admin-transactions")
                        .param("email", "admin@gmail.com").param("role", "ADMIN")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Transactions retrieved successfully"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(4));
    }

    // TEST CASE 2: ADMIN NOT FOUND
    @Test
    void testGetAllTransactionsByAdmin_AdminNotFound() throws Exception {
        when(transactionService.findByAdminEmailAndRole(anyString(), any(Role.class)))
                .thenThrow(new AdminNotFoundException("Admin not found"));

        mockMvc.perform(get("/transaction/api/v2/admin-transactions")
                        .param("email", "invalidgmail.com").param("role", "ADMIN")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Admin not found"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    // TEST CASE 3: NO TRANSACTIONS FOUND
    @Test
    void testGetAllTransactionsByAdmin_NoTransactions() throws Exception {
        when(transactionService.findByAdminEmailAndRole(anyString(), any(Role.class)))
                .thenThrow(new EmptyTransactionListException("No transactions found."));

        mockMvc.perform(get("/transaction/api/v2/admin-transactions")
                        .param("email", "admin@gmail.com").param("role", "ADMIN")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("No transactions found."))
                .andExpect(jsonPath("$.data").isEmpty());
    }


    @Test
    void testViewTransactionsByAmount_Success() throws Exception {
        when(transactionService.sortTransactionsByAmount(anyString(), anyString(), anyString()))
                .thenReturn(mockTransactions);

        mockMvc.perform(get("/transaction/api/v2/view-transactions-by-amount")
                        .param("walletId", "WAL123")
                        .param("emailId", "user@example.com")
                        .param("sortOrder", "DESC")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount").value(200.00))
                .andExpect(jsonPath("$[1].amount").value(500.00));

        verify(transactionService, times(1))
                .sortTransactionsByAmount(anyString(), anyString(), anyString());
    }

}
