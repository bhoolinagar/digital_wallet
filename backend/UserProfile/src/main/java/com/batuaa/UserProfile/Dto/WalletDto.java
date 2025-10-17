package com.batuaa.UserProfile.Dto;
import java.math.BigDecimal;
public class WalletDto {
    private String emailId;
    private BigDecimal balance;
    private String bankName;
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
