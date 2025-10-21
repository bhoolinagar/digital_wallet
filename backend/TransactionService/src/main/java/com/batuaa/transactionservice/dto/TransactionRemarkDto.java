package com.batuaa.transactionservice.dto;

public class TransactionRemarkDto {
    private  String emailId;
    private String walletId;
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
