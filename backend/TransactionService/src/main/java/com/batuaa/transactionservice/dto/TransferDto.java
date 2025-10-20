package com.batuaa.transactionservice.dto;
import java.math.BigDecimal;

public class TransferDto {
private String fromWalletId;      // FK to source Wallet
private String toWalletId;
private String fromBuyerEmailId;      // FK to source Wallet
private String toBuyerEmailId;      // FK to destination Wallet
private BigDecimal amount;
private String remarks;

    public String getFromWalletId() {
        return fromWalletId;
    }

    public String getToWalletId() {
        return toWalletId;
    }

    public String getFromBuyerEmailId() {
        return fromBuyerEmailId;
    }

    public String getToBuyerEmailId() {
        return toBuyerEmailId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getRemarks() {
        return remarks;
    }
}
