package com.batuaa.transactionservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class TransactionRemarkDto {

    @NotBlank(message = "Email ID cannot be blank")
    @Email(message = "Email ID must be valid")
    private  String emailId;

    @NotBlank(message = "Wallet ID cannot be blank")
    private String walletId;
    @NotBlank(message = "Remark cannot be blank")
    private  String remark;

    public String getEmailId() {
        return emailId;
    }

    public String getWalletId() {
        return walletId;
    }

    public String getRemark() {
        return remark;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
