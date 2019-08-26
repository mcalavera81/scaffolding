package com.playtomic.tests.wallet.domain;

import com.playtomic.tests.wallet.domain.exception.PaymentServiceException;

import java.math.BigDecimal;

public interface PaymentService {
    void charge(BigDecimal amount) throws PaymentServiceException;
}
