package com.playtomic.tests.wallet.domain;

import org.javamoney.moneta.Money;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

import static javax.money.Monetary.getCurrency;

public class WalletTest {

    @Test
    public void deposit() {
        Wallet wallet = new Wallet();
        BigDecimal deposit = new BigDecimal(100);
        wallet.deposit(deposit);

        Assert.assertEquals(
                Money.of(deposit, getCurrency("EUR")),
                wallet.balance()  );
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeDeposit() {
        Wallet wallet = new Wallet();
        BigDecimal deposit = new BigDecimal(-1);
        wallet.deposit(deposit);

    }

    @Test
    public void withdrawal() {

        Wallet wallet = new Wallet(new BigDecimal(100));
        BigDecimal withdrawal = new BigDecimal(40);

        wallet.withdraw(withdrawal);

        Assert.assertEquals(
                Money.of(new BigDecimal(60), getCurrency("EUR")),
                wallet.balance()  );
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeWithdrawal() {

        Wallet wallet = new Wallet(new BigDecimal(100));
        BigDecimal withdrawal = new BigDecimal(-1);

        wallet.withdraw(withdrawal);

    }

    @Test(expected = IllegalArgumentException.class)
    public void notEnoughFundsWithdrawal() {

        Wallet wallet = new Wallet(new BigDecimal(100));
        BigDecimal withdrawal = new BigDecimal(100);

        wallet.withdraw(withdrawal);

    }

}

