package com.playtomic.tests.wallet.domain.exception;

/**
 *
 */
public class PaymentServiceException extends Exception {

    public PaymentServiceException() {
        super("Error with the Payment Gateway");
    }
}
