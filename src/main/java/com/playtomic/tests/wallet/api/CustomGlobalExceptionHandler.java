package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.domain.exception.DepositException;
import com.playtomic.tests.wallet.domain.exception.PaymentServiceException;
import com.playtomic.tests.wallet.domain.exception.WalletNotFoundException;
import com.playtomic.tests.wallet.domain.exception.WithdrawalException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(WalletNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> customHandlerNotFound(Exception ex, WebRequest request) {

        CustomErrorResponse errors = new CustomErrorResponse();
        errors.setTimestamp(LocalDateTime.now());
        errors.setError(ex.getMessage());
        errors.setStatus(HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler({DepositException.class, WithdrawalException.class})
    public ResponseEntity<CustomErrorResponse> customHandlerTransaction(Exception ex, WebRequest request) {

        CustomErrorResponse errors = new CustomErrorResponse();
        errors.setTimestamp(LocalDateTime.now());
        errors.setError(ex.getMessage() + (ex.getCause() != null ? "Cause: " + ex.getCause().getMessage() : ""));
        errors.setStatus(HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(PaymentServiceException.class)
    public ResponseEntity<CustomErrorResponse> customHandlerPaymentService(Exception ex, WebRequest request) {

        CustomErrorResponse errors = new CustomErrorResponse();
        errors.setTimestamp(LocalDateTime.now());
        errors.setError(ex.getMessage() + (ex.getCause() != null ? "Cause: " + ex.getCause().getMessage() : ""));
        errors.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}