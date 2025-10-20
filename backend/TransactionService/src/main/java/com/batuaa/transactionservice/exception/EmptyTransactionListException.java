package com.batuaa.transactionservice.exception;

public class EmptyTransactionListException extends RuntimeException {
    public EmptyTransactionListException(String message) {
        super(message);
    }
}

