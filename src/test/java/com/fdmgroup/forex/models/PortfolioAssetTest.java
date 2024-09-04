package com.fdmgroup.forex.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PortfolioAssetTest {

    private Currency currency;
    private Portfolio portfolio;
    private PortfolioAsset portfolioAsset;
    private User user;

    @BeforeEach
    public void setUp() {
        currency = new Currency("HKD", "Hong Kong Dollars");
        user = new User("Demo User", "demo.user@email.com", "qwerty", currency, "demoaccount");
        portfolio = new Portfolio(user);
        portfolioAsset = new PortfolioAsset(portfolio, currency, 100.0);
    }

    @Test
    public void testPortfolioAsset_DefaultConstructor() {
        PortfolioAsset defaultPortfolioAsset = new PortfolioAsset();
        assertNull(defaultPortfolioAsset.getId(), "PortfolioAsset ID should be null");
        assertNull(defaultPortfolioAsset.getPortfolio(), "PortfolioAsset portfolio should be null");
        assertNull(defaultPortfolioAsset.getCurrency(), "PortfolioAsset currency should be null");
        assertEquals(0.0, defaultPortfolioAsset.getBalance(), "PortfolioAsset balance should be 0.0");
    }

    @Test
    public void testPortfolioAsset_ParameterizedConstructor() {
        assertNotNull(portfolioAsset.getPortfolio(), "PortfolioAsset portfolio should not be null");
        assertEquals(portfolio, portfolioAsset.getPortfolio(), "PortfolioAsset portfolio should match");
        assertNotNull(portfolioAsset.getCurrency(), "PortfolioAsset currency should not be null");
        assertEquals(currency, portfolioAsset.getCurrency(), "PortfolioAsset currency should match");
        assertEquals(100.0, portfolioAsset.getBalance(), "PortfolioAsset balance should match");
    }

    @Test
    public void testSettersAndGetters() {
        UUID newId = UUID.randomUUID();
        User newUser = new User("Demo User 2", "demo.user2@email.com", "asdf", currency, "demoaccount2");
        Portfolio newPortfolio = new Portfolio(newUser);
        Currency newCurrency = new Currency("USD", "U.S. Dollars");
        double newBalance = 1000.0;
        portfolioAsset.setId(newId);
        portfolioAsset.setPortfolio(newPortfolio);
        portfolioAsset.setCurrency(newCurrency);
        portfolioAsset.setBalance(newBalance);
        assertEquals(newId, portfolioAsset.getId(), "PortfolioAsset ID should match");
        assertEquals(newPortfolio, portfolioAsset.getPortfolio(), "PortfolioAsset portfolio should match");
        assertEquals(newCurrency, portfolioAsset.getCurrency(), "PortfolioAsset currency should match");
        assertEquals(newBalance, portfolioAsset.getBalance(), "PortfolioAsset balance should match");
    }

    @Test
    public void testSetters_IgnoreNullValues() {
        portfolioAsset.setId(UUID.randomUUID());
        portfolioAsset.setId(null);
        portfolioAsset.setPortfolio(null);
        portfolioAsset.setCurrency(null);
        assertNotNull(portfolioAsset.getId(), "PortfolioAsset ID should be unchanged");
        assertEquals(portfolio, portfolioAsset.getPortfolio(), "PortfolioAsset portfolio should be unchanged");
        assertEquals(currency, portfolioAsset.getCurrency(), "PortfolioAsset currency should be unchanged");
    }

}
