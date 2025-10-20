package com.batuaa.transactionservice.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class TransactionDateRangeDto {

    @NotBlank(message = "Wallet ID is required")
    private String walletId;

    @NotBlank(message = "Email ID is required")
    private String emailId;

    @NotNull(message = "Start date is required")
    @PastOrPresent(message = "Start date cannot be in the future")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @PastOrPresent(message = "End date cannot be in the future")
    private LocalDate endDate;

    @Min(value = 0, message = "Page index must be zero or positive")
    private int page;

    // Custom logical validation to ensure startDate <= endDate
    @AssertTrue(message = "Start date cannot be after end date")
    public boolean isValidDateRange() {
        if (startDate == null || endDate == null) return true; // handled by @NotNull separately
        return !startDate.isAfter(endDate);
    }

    // Getters and setters
    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
