package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.domain.*;
import com.playtomic.tests.wallet.domain.exception.DepositException;
import com.playtomic.tests.wallet.domain.exception.PaymentServiceException;
import com.playtomic.tests.wallet.domain.exception.WalletNotFoundException;
import com.playtomic.tests.wallet.domain.exception.WithdrawalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@RestController
@RequestMapping(value = "wallet", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class WalletController {
    private Logger log = LoggerFactory.getLogger(WalletController.class);


    private WalletService walletService;

    @Autowired
    private WalletRepository repository;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }


    @GetMapping("{walletId}")
    public ResponseEntity<Wallet> findWallet(@PathVariable("walletId") long walletId) throws WalletNotFoundException {

        return ResponseEntity.ok(walletService.findWallet(walletId));
    }

    @PostMapping("{walletId}/deposit/{amount}")
    public ResponseEntity<Wallet> deposit(@PathVariable("walletId") long walletId,
                                          @PathVariable("amount") BigDecimal amount) throws DepositException, WalletNotFoundException {


        walletService.deposit(walletId, amount);
        return ResponseEntity.ok(walletService.findWallet(walletId));
    }

    @PostMapping("{walletId}/withdraw/{amount}")
    public ResponseEntity<Wallet> withdraw(@PathVariable("walletId") long walletId,
                                          @PathVariable("amount") BigDecimal amount) throws WithdrawalException, WalletNotFoundException {

        walletService.withdrawal(walletId, amount);
        return ResponseEntity.ok(walletService.findWallet(walletId));
    }

    @PostMapping("{walletId}/topup/{amount}")
    public ResponseEntity<Wallet> refillFromPaymentGateway(@PathVariable("walletId") long walletId,
                                           @PathVariable("amount") BigDecimal amount) throws WalletNotFoundException, PaymentServiceException {

        walletService.topupFromPaymentGateway(walletId, amount);
        return ResponseEntity.ok(walletService.findWallet(walletId));
    }


}
