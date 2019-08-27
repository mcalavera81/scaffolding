package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.domain.Wallet;
import io.restassured.RestAssured;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.path.json.config.JsonPathConfig;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.config.RestAssuredMockMvcConfig.newConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.DOUBLE;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "test")
@AutoConfigureMockMvc
public class WalletApplicationIT {


	private final static BigDecimal epsilon = new BigDecimal(0.01);

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before
	public void initialiseRestAssuredMockMvcWebApplicationContext() {
		RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
		RestAssuredMockMvc.config = newConfig().jsonConfig(jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.BIG_DECIMAL));

	}


	@Test
	public void findWallet() {


		given()
				.when()
				.get("/wallet/" + 3)
				.then()
				.status(HttpStatus.OK)
				.body("id", equalTo(3))
				.body("currency", equalTo("EUR"))
				.body("balance", closeTo(new BigDecimal(500), epsilon));

	}

	@Test
	public void walletNotFound() {


		given()
				.when()
				.get("/wallet/" + -1)
				.then()
				.status(HttpStatus.NOT_FOUND)
				.body("$", hasKey("timestamp"))
				.body("$", hasKey("status"))
				.body("$", hasKey("error"));

	}

	@Test
	public void deposit() {


		given()
				.when()
				.post("/wallet/" + 2 + "/deposit/" + 30.1)
				.then()
				.status(HttpStatus.OK)
				.body("balance", closeTo(new BigDecimal(60.1), epsilon));


		given()
				.when()
				.get("/wallet/" + 2)
				.then()
				.status(HttpStatus.OK)
				.body("id", equalTo(2))
				.body("currency", equalTo("EUR"))
				.body("balance", closeTo(new BigDecimal(60.1), epsilon));

	}

	@Test
	public void NegativeDeposit() {

		int walletId = 7;
		BigDecimal deposit = BigDecimal.valueOf(-1);

		given()
				.when()
				.post("/wallet/{walletId}/deposit/{amount}", walletId, deposit)
				.then()
				.status(HttpStatus.BAD_REQUEST)
				.body("$", hasKey("timestamp"))
				.body("$", hasKey("status"))
				.body("$", hasKey("error"));


		given()
				.when()
				.get("/wallet/{walletId}",walletId)
				.then()
				.status(HttpStatus.OK)
				.body("id", equalTo(walletId))
				.body("currency", equalTo("EUR"))
				.body("balance", closeTo(new BigDecimal(1000), epsilon));

	}
		@Test
	public void withdraw() {

		given()
				.when()
				.post("/wallet/" + 1 + "/withdraw/" + 100)
				.then()
				.status(HttpStatus.OK)
				.body("balance", closeTo(BigDecimal.ZERO, epsilon));


		given()
				.when()
				.get("/wallet/" + 1)
				.then()
				.status(HttpStatus.OK)
				.body("id", equalTo(1))
				.body("currency", equalTo("EUR"))
				.body("balance", closeTo(BigDecimal.ZERO, epsilon));

	}

	@Test
	public void notEnoughFundsWithdraw() {

		given()
				.when()
				.post("/wallet/" + 6 + "/withdraw/" + 101)
				.then()
				.status(HttpStatus.BAD_REQUEST)
				.body("$", hasKey("timestamp"))
				.body("$", hasKey("status"))
				.body("$", hasKey("error"));


		given()
				.when()
				.get("/wallet/" + 6)
				.then()
				.status(HttpStatus.OK)
				.body("id", equalTo(6))
				.body("currency", equalTo("EUR"))
				.body("balance", closeTo(BigDecimal.ONE, epsilon));

	}

	@Test
	public void topup() {
		given()
				.when()
				.post("/wallet/" + 4 + "/topup/" + 100)
				.then()
				.status(HttpStatus.OK)
				.body("balance", closeTo(new BigDecimal(110), epsilon));

		given()
				.when()
				.get("/wallet/" + 4)
				.then()
				.status(HttpStatus.OK)
				.body("id", equalTo(4))
				.body("currency", equalTo("EUR"))
				.body("balance", closeTo(new BigDecimal(110), epsilon));
	}


	@Test
	public void topupError() {
		given()
				.when()
				.post("/wallet/" + 5 + "/topup/" + 9)
				.then()
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("$", hasKey("timestamp"))
				.body("$", hasKey("status"))
				.body("$", hasKey("error"));

		given()
				.when()
				.get("/wallet/" + 5)
				.then()
				.status(HttpStatus.OK)
				.body("balance", closeTo(new BigDecimal(40), epsilon));
	}
}
