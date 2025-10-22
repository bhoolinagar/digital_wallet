package com.batuaa.UserProfile.exception;
import com.batuaa.UserProfile.Dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(WalletNotFoundException.class)
    public ResponseEntity<ApiResponse> handleWalletNotFound(WalletNotFoundException ex) {
        ApiResponse response = new ApiResponse("fail", ex.getMessage());
        return ResponseEntity.status(404).body(response);
    }

    @ExceptionHandler(WalletAlreadyFound.class)
    public ResponseEntity<ApiResponse> handleWalletAlreadyFound(WalletAlreadyFound ex) {
        ApiResponse response = new ApiResponse("fail", ex.getMessage());
        return ResponseEntity.status(409).body(response);
    }

    @ExceptionHandler(BuyerNotFoundException.class)
    public ResponseEntity<ApiResponse> handleBuyerNotFound(BuyerNotFoundException ex) {
        ApiResponse response = new ApiResponse("fail", ex.getMessage());
        return ResponseEntity.status(404).body(response);
    }

    // Generic exception handler for all other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleAllOtherExceptions(Exception ex) {
        ApiResponse response = new ApiResponse("error", "Something went wrong: " + ex.getMessage());
        return ResponseEntity.status(500).body(response);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handleIllegalArgumentException(Exception ex) {
        ApiResponse response = new ApiResponse("fail",   ex.getMessage());
        return ResponseEntity.status(400).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        ApiResponse response = new ApiResponse("fail", errors, null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}