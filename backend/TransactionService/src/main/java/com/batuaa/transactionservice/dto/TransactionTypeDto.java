package com.batuaa.transactionservice.dto;

import com.batuaa.transactionservice.model.Type;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TransactionTypeDto {

    @NotBlank(message = "Wallet ID cannot be empty")
    private String walletId;

    @Email(message = "Please provide a valid email ID")
    private String emailId;

    @NotNull(message = "Transaction type cannot be null (CREDIT or DEBIT)")
    private Type type;
    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
