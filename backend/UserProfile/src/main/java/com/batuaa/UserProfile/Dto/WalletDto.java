package com.batuaa.userprofile.dto;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
public class WalletDto {


    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String emailId;

    @DecimalMin(value = "0.0", inclusive = false, message = "Balance must be greater than 0")
    private BigDecimal balance;

    @NotBlank(message = "Bank name cannot be blank")
    private String bankName;

    @NotBlank(message = "Account number cannot be blank")
    @Pattern(regexp = "\\d{11}", message = "Account number must be exactly 11 digits")
    private String accountNumber;

    public String getEmailId() {
        return emailId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getBankName() {
        return bankName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}