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
    private List<PortfolioAsset> portfolioAssets;
    private User user;

    @BeforeEach
    public void setUp() {
        currency1 = new Currency("USD", "U.S. Dollars");
        currency2 = new Currency("HKD", "Hong Kong Dollars");
        user = new User("Demo User", "demo.user@email.com", "qwerty", currency1, "demoaccount");
        
        portfolio = new Portfolio(user, new ArrayList<>());
        PortfolioAsset portfolioAsset1 = new PortfolioAsset(portfolio, currency1, 100.0);
        PortfolioAsset portfolioAsset2 = new PortfolioAsset(portfolio, currency2, 555.0);
        portfolioAssets = new ArrayList<>();
        portfolioAssets.add(portfolioAsset1);
        portfolioAssets.add(portfolioAsset2);
        portfolio.setPortfolioAssets(portfolioAssets);
    }

    @Test
    public void testPortfolio_DefaultConstructor() {
        Portfolio defaultPortfolio = new Portfolio();
        assertNull(defaultPortfolio.getId(), "Portfolio ID should be null");
        assertNull(defaultPortfolio.getUser(), "Portfolio user should be null");
        assertNull(defaultPortfolio.getPortfolioAssets(), "Portfolio assets should be null");
    }

    @Test
    public void testPortfolio_ParameterizedConstructor() {
        assertNotNull(portfolio.getUser(), "Portfolio user should not be null");
        assertEquals(user, portfolio.getUser(), "Portfolio user should match");
        assertEquals(portfolioAssets, portfolio.getPortfolioAssets(), "Portfolio assets should match");
    }

    @Test
    public void testSettersAndGetters() {
        UUID newId = UUID.randomUUID();
        User newUser = new User("Demo User 2", "demo.user2@email.com", "asdf", currency1, "demoaccount2");
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
        portfolio.setPortfolioAssets(null);
        assertEquals(validUUID, portfolio.getId(), "Portfolio ID should be unchanged");
        assertEquals(user, portfolio.getUser(), "Portfolio user should be unchanged");
        assertEquals(portfolioAssets, portfolio.getPortfolioAssets(), "Portfolio assets should be unchanged");
    }

}
