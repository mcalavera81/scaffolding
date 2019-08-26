package com.playtomic.tests.wallet.domain.exception;

import static java.lang.String.format;

public class DepositException extends Exception {


    public DepositException(long walletId, Throwable cause) {
        super(format("Deposit exception on walletId %s!", walletId), cause);
    }
}
