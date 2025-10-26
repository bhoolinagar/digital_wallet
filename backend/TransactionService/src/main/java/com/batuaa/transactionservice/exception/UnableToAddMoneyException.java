package com.batuaa.transactionservice.exception;

public class UnableToAddMoneyException extends RuntimeException {
    public UnableToAddMoneyException(String message, Throwable cause) {
        super(message, cause);
    }
}