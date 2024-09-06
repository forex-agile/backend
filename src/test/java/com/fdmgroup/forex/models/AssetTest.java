package com.fdmgroup.forex.models;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AssetTest {

    private Currency currency;
    private Portfolio portfolio;
    private Asset Asset;
    private User user;

    @BeforeEach
    public void setUp() {
        currency = new Currency("HKD", "Hong Kong Dollars");
        user = new User("Demo User", "demo.user@email.com", "qwerty", currency, "demoaccount");
        portfolio = new Portfolio(user, new ArrayList<>());
        Asset = new Asset(portfolio, currency, 100.0);
    }

    @Test
    public void testPortfolioAsset_DefaultConstructor() {
        Asset defaultPortfolioAsset = new Asset();
        assertNull(defaultPortfolioAsset.getId(), "Asset ID should be null");
        assertNull(defaultPortfolioAsset.getPortfolio(), "Asset portfolio should be null");
        assertNull(defaultPortfolioAsset.getCurrency(), "Asset currency should be null");
        assertEquals(0.0, defaultPortfolioAsset.getBalance(), "Asset balance should be 0.0");
    }

    @Test
    public void testPortfolioAsset_ParameterizedConstructor() {
        assertNotNull(Asset.getPortfolio(), "Asset portfolio should not be null");
        assertEquals(portfolio, Asset.getPortfolio(), "Asset portfolio should match");
        assertNotNull(Asset.getCurrency(), "Asset currency should not be null");
        assertEquals(currency, Asset.getCurrency(), "Asset currency should match");
        assertEquals(100.0, Asset.getBalance(), "Asset balance should match");
    }

    @Test
    public void testSettersAndGetters() {
        UUID newId = UUID.randomUUID();
        User newUser = new User("Demo User 2", "demo.user2@email.com", "asdf", currency, "demoaccount2");
        Portfolio newPortfolio = new Portfolio(newUser, new ArrayList<>());
        Currency newCurrency = new Currency("USD", "U.S. Dollars");
        double newBalance = 1000.0;
        Asset.setId(newId);
        Asset.setPortfolio(newPortfolio);
        Asset.setCurrency(newCurrency);
        Asset.setBalance(newBalance);
        assertEquals(newId, Asset.getId(), "Asset ID should match");
        assertEquals(newPortfolio, Asset.getPortfolio(), "Asset portfolio should match");
        assertEquals(newCurrency, Asset.getCurrency(), "Asset currency should match");
        assertEquals(newBalance, Asset.getBalance(), "Asset balance should match");
    }

    @Test
    public void testSetters_IgnoreNullValues() {
        Asset.setId(UUID.randomUUID());
        Asset.setId(null);
        Asset.setPortfolio(null);
        Asset.setCurrency(null);
        assertNotNull(Asset.getId(), "Asset ID should be unchanged");
        assertEquals(portfolio, Asset.getPortfolio(), "Asset portfolio should be unchanged");
        assertEquals(currency, Asset.getCurrency(), "Asset currency should be unchanged");
    }

}
