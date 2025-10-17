package com.batuaa.TransactionService.dto;

import com.batuaa.TransactionService.model.Type;

public class TransactionTypeDto {
    private  String emailId;
    private String walletId;
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
