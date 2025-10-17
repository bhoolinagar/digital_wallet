package com.batuaa.TransactionService.model;

import java.math.BigDecimal;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "transaction_records") // corrected name for proper mapping with db.

public class Transaction {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transactionId;  // Auto-generated PK

    @ManyToOne
    @JoinColumn(name = "from_wallet_id", referencedColumnName = "wallet_id")
    @JsonBackReference
    private Wallet fromWallet;      // FK to source Wallet

    @ManyToOne
    @JoinColumn(name = "to_wallet_id", referencedColumnName = "wallet_id")
    @JsonBackReference
    private Wallet toWallet;


    @ManyToOne
    @JoinColumn(name = "from_email_id", referencedColumnName = "email_id")
    @JsonBackReference
    private Buyer fromBuyer;      // FK to source Wallet

    @ManyToOne
    @JoinColumn(name = "to_email_id", referencedColumnName = "email_id")
    @JsonBackReference
    private Wallet toBuyer;
    // FK to destination Wallet
    private BigDecimal amount;
    private LocalDateTime timestamp; // Full date & time
    @Enumerated(EnumType.STRING)
    private Status status; // SUCCESS, PROCESSING, FAILED

    private String remarks;


}