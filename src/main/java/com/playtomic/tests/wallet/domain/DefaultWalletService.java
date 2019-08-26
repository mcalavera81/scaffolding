package com.playtomic.tests.wallet.domain;


import com.playtomic.tests.wallet.domain.exception.DepositException;
import com.playtomic.tests.wallet.domain.exception.PaymentServiceException;
import com.playtomic.tests.wallet.domain.exception.WalletNotFoundException;
import com.playtomic.tests.wallet.domain.exception.WithdrawalException;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Optional;

@Component
@Transactional
public class DefaultWalletService implements WalletService {

    private WalletRepository walletRepo;
    private PaymentService paymentService;

    public DefaultWalletService(
            PaymentService paymentService,
            WalletRepository walletRepo) {
        this.paymentService = paymentService;
        this.walletRepo = walletRepo;
    }


    @Override
    public Wallet findWallet(long walletId) throws WalletNotFoundException {
        return Optional.ofNullable(walletRepo.findOne(walletId)).orElseThrow(() -> new WalletNotFoundException(walletId));
    }

    @Override
    public void deposit(long walletId, BigDecimal amount) throws DepositException, WalletNotFoundException {
        Wallet wallet = Optional.ofNullable(walletRepo.findOne(walletId)).orElseThrow(() -> new WalletNotFoundException(walletId));

        try{
            wallet.deposit(amount);
        }catch (Exception exception){
            throw new DepositException(walletId, exception);
        }

        walletRepo.save(wallet);

    }

    @Override
    public void withdrawal(long walletId, BigDecimal amount) throws WalletNotFoundException, WithdrawalException {
        Wallet wallet = Optional.ofNullable(walletRepo.findOne(walletId)).orElseThrow(WalletNotFoundException.of(walletId));

        try{
            wallet.withdraw(amount);
        }catch (Exception exception){
            throw new WithdrawalException(walletId, exception);
        }

        walletRepo.save(wallet);
    }

    @Override
    public void topupFromPaymentGateway(long walletId, BigDecimal amount) throws WalletNotFoundException, PaymentServiceException {

        Wallet wallet = Optional.ofNullable(walletRepo.findOne(walletId)).orElseThrow(WalletNotFoundException.of(walletId));

        paymentService.charge(amount);
        wallet.deposit(amount);

    }
}
