package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.domain.WalletRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.stream.Stream;

@Component
@Profile("test")
public class SampleDataTest implements CommandLineRunner {


	private final WalletRepository walletRepo;

	public SampleDataTest(WalletRepository walletRepository) {
		this.walletRepo= walletRepository;

	}

	@Override
	public void run(String... args) throws Exception {


		Stream.of(100, 30, 500,10, 40, 1,1000).map(b -> new Wallet(new BigDecimal(b)))
				.forEach(walletRepo::save);

		walletRepo.findAll().forEach(System.out::println);



	}
}