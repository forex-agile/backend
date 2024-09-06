package com.fdmgroup.forex.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PortfolioTest {

	private Currency currency1;
	private Currency currency2;
	private Portfolio portfolio;
	private List<Asset> assets;
	private User user;

	@BeforeEach
	public void setUp() {
		currency1 = new Currency("USD", "U.S. Dollars");
		currency2 = new Currency("HKD", "Hong Kong Dollars");
		user = new User(UUID.randomUUID(), "Demo User", "demo.user@email.com", "qwerty", currency1, "demoaccount",
				new Role());

		portfolio = new Portfolio(user, new ArrayList<>());
		Asset portfolioAsset1 = new Asset(portfolio, currency1, 100.0);
		Asset portfolioAsset2 = new Asset(portfolio, currency2, 555.0);
		assets = new ArrayList<>();
		assets.add(portfolioAsset1);
		assets.add(portfolioAsset2);
		portfolio.setAssets(assets);
	}

	@Test
	public void testPortfolio_DefaultConstructor() {
		Portfolio defaultPortfolio = new Portfolio();
		assertNull(defaultPortfolio.getId(), "Portfolio ID should be null");
		assertNull(defaultPortfolio.getUser(), "Portfolio user should be null");
		assertNull(defaultPortfolio.getAssets(), "Portfolio assets should be null");
	}

	@Test
	public void testPortfolio_ParameterizedConstructor() {
		assertNotNull(portfolio.getUser(), "Portfolio user should not be null");
		assertEquals(user, portfolio.getUser(), "Portfolio user should match");
		assertEquals(assets, portfolio.getAssets(), "Portfolio assets should match");
	}

	@Test
	public void testSettersAndGetters() {
		UUID newId = UUID.randomUUID();
		User newUser = new User(UUID.randomUUID(), "Demo User 2", "demo.user2@email.com", "asdf", currency1,
				"demoaccount2", new Role());
		portfolio.setId(newId);
		portfolio.setUser(newUser);
		assertEquals(newId, portfolio.getId(), "Portfolio ID should match");
		assertEquals(newUser, portfolio.getUser(), "Portfolio user should match");
	}

	@Test
	public void testSetters_IgnoreNullValues() {
		UUID validUUID = UUID.randomUUID();
		portfolio.setId(validUUID);
		portfolio.setId(null);
		portfolio.setUser(null);
		portfolio.setAssets(null);
		assertEquals(validUUID, portfolio.getId(), "Portfolio ID should be unchanged");
		assertEquals(user, portfolio.getUser(), "Portfolio user should be unchanged");
		assertEquals(assets, portfolio.getAssets(), "Portfolio assets should be unchanged");
	}

}
