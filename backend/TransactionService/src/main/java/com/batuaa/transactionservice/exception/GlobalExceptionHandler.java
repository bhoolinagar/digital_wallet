package com.batuaa.transactionservice.exception;

import com.batuaa.transactionservice.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.DateTimeException;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ApiResponse<Object>> handleInsufficientFunds(InsufficientFundsException ex) {

        ApiResponse<Object> response = new ApiResponse<>("fail", ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(UnableToAddMoneyException.class)
    public ResponseEntity<ApiResponse<Object>> handleUnableToAddMoney(UnableToAddMoneyException ex) {
        ApiResponse<Object> response = new ApiResponse<>("fail", ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleTransactionNotFound(TransactionNotFoundException ex) {
        ApiResponse<Object> response = new ApiResponse<>("fail", ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(UnableToFilterByRemarkException.class)
    public ResponseEntity<ApiResponse<Object>> handleUnableToFilterByRemark(UnableToFilterByRemarkException ex) {
        ApiResponse<Object> response = new ApiResponse<>("fail", ex.getMessage() + ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(DuplicateTransactionException.class)
    public ResponseEntity<ApiResponse<Object>> handleDuplicateTransaction(DuplicateTransactionException ex) {
        ApiResponse<Object> response = new ApiResponse<>("fail", ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(WalletNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleWalletNotFound(WalletNotFoundException ex) {

        ApiResponse<Object> response = new ApiResponse<>("fail", ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

    }

    @ExceptionHandler(EmptyTransactionListException.class)
    public ResponseEntity<ApiResponse<Object>> handleTransactionNotFound(EmptyTransactionListException ex) {

        ApiResponse<Object> response = new ApiResponse<>("fail", ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

    }

    @ExceptionHandler(DateTimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleDateTimeException(DateTimeException ex) {

        ApiResponse<Object> response = new ApiResponse<>("fail", ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        ApiResponse<Object> response = new ApiResponse<>("fail", errors, null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex) {
        ApiResponse<Object> response = new ApiResponse<>("fail", "Unexpected error: " + ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }


    @ExceptionHandler(AdminNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleAdminNotFound(AdminNotFoundException ex) {

        ApiResponse<Object> response = new ApiResponse<>("fail", ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

    }


}
