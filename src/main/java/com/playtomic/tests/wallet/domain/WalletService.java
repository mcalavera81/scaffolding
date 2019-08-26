package com.playtomic.tests.wallet.domain;

import com.playtomic.tests.wallet.domain.exception.DepositException;
import com.playtomic.tests.wallet.domain.exception.PaymentServiceException;
import com.playtomic.tests.wallet.domain.exception.WalletNotFoundException;
import com.playtomic.tests.wallet.domain.exception.WithdrawalException;

import java.math.BigDecimal;
import java.util.Optional;


public interface WalletService {

    Wallet findWallet(long id) throws WalletNotFoundException;

    void deposit(long walletId, BigDecimal amount) throws DepositException, WalletNotFoundException;

    void withdrawal(long walletId, BigDecimal amount) throws WalletNotFoundException, WithdrawalException;

    void topupFromPaymentGateway(long walletId, BigDecimal amount) throws WalletNotFoundException, PaymentServiceException;
}
