package com.batuaa.transactionservice.exception;

public class AmountCanNotBeNullException extends RuntimeException {
    public AmountCanNotBeNullException(String message) {
        super(message);
    }
}
