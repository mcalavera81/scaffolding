package com.playtomic.tests.wallet.domain.exception;

import java.util.function.Supplier;

public class WalletNotFoundException extends Exception {

    public WalletNotFoundException(long walletId) {
        super(String.format("Wallet %s not found!",walletId));
    }


    public static Supplier<WalletNotFoundException> of(long walletId){
        return ()-> new WalletNotFoundException(walletId);
    }
}
