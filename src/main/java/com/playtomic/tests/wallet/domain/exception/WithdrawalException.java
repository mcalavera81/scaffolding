package com.playtomic.tests.wallet.domain.exception;

import static java.lang.String.format;

public class WithdrawalException extends Exception {

    public WithdrawalException(long walletId, Throwable cause) {
        super(format("Withdrawal exception on walletId %s!", walletId), cause);
    }
}
