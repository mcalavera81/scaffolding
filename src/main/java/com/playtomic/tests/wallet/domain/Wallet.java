package com.playtomic.tests.wallet.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.javamoney.moneta.Money;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

@Entity
@ToString
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Wallet {
    private static final BigDecimal ZERO = BigDecimal.ZERO;
    private static final String defaultCurrency = "EUR";

    public Wallet(){
        this(ZERO);
    }



    public Wallet(BigDecimal initialBalance){
        this.currency = Monetary.getCurrency(defaultCurrency).getCurrencyCode();
        this.balance = initialBalance;
    }


    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    private BigDecimal balance;

    private String currency;


    public MonetaryAmount balance(){
        return Money.of(balance, currency);
    }

    public void deposit(BigDecimal deposit){
        Objects.requireNonNull(deposit);

        if(deposit.compareTo(ZERO) < 0){
            throw new IllegalArgumentException(String.format("Deposit %s should be > 0", deposit));
        }

        this.balance = this.balance.add(deposit);

    }

    public void withdraw(BigDecimal withdrawal){
        Objects.requireNonNull(withdrawal);

        if(withdrawal.compareTo(ZERO) < 0){
            throw new IllegalArgumentException(String.format("Withdrawal %s should be > 0", withdrawal));
        }

        if(withdrawal.compareTo(balance) > 0){
            throw new IllegalArgumentException(String.format("Balance is insuficient for a withdrawal", withdrawal));
        }

        this.balance = this.balance.subtract(withdrawal);

    }

}
