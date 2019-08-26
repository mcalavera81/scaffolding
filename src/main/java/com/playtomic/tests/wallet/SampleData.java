package com.playtomic.tests.wallet;

import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.domain.WalletRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.stream.Stream;

@Component
@Profile("develop")
public class SampleData implements CommandLineRunner {


	private final WalletRepository walletRepo;

	public SampleData(WalletRepository walletRepository) {
		this.walletRepo= walletRepository;

	}

	@Override
	public void run(String... args) throws Exception {


		Stream.of(100, 30, 500).map(b -> new Wallet(new BigDecimal(b)))
				.forEach(walletRepo::save);

		walletRepo.findAll().forEach(System.out::println);



	}
}