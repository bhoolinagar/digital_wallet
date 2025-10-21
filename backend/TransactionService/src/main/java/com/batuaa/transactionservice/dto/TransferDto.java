package com.batuaa.transactionservice.dto;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class TransferDto {
    @NotBlank(message = "Sender walletId cannot be blank")
    private String fromWalletId;      // FK to source Wallet

    @NotBlank(message = "Receiver walletId cannot be blank")
    private String toWalletId;

    public void setFromBuyerEmailId(String fromBuyerEmailId) {
        this.fromBuyerEmailId = fromBuyerEmailId;
    }

    public void setToBuyerEmailId(String toBuyerEmailId) {
        this.toBuyerEmailId = toBuyerEmailId;
    }

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be greater than 0")
//    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    private String remarks;

//    Currently keeping the emailIds , as the code was breaking if the email address was not found or
//    was invalid/incorrect format | Can be removed will not affect the code if email is passed via session
    @NotBlank(message = "Sender email cannot be blank")
    @Email(message = "Invalid sender email format")
    private String fromBuyerEmailId;

    @NotBlank(message = "Receiver email cannot be blank")
    @Email(message = "Invalid receiver email format")
    private String toBuyerEmailId;

    public String getFromWalletId() {
        return fromWalletId;
    }

    public String getToWalletId() {
        return toWalletId;
    }


    public BigDecimal getAmount() {
        return amount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setFromWalletId(String fromWalletId) {
        this.fromWalletId = fromWalletId;
    }

    public void setToWalletId(String toWalletId) {
        this.toWalletId = toWalletId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
