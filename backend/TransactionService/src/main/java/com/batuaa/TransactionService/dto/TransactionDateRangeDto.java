package com.batuaa.TransactionService.dto;

import java.time.LocalDate;

public class TransactionDateRangeDto {
    private  String emailId;
    private String walletId;
    private LocalDate startDate;
    private LocalDate endDate;

    public String getEmailId() {
        return emailId;
    }

    public String getWalletId() {
        return walletId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
