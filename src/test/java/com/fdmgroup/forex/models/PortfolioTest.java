package com.fdmgroup.forex.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

public class PortfolioTest {

    private Currency currency;
    private Portfolio portfolio;
    private User user;

    @BeforeEach
    public void setUp() {
        currency = new Currency("USD", "U.S. Dollars");
        user = new User("Demo User", "demo.user@email.com", "qwerty", currency, "demoaccount");
        portfolio = new Portfolio(user);
    }

    @Test
    public void testPortfolio_DefaultConstructor() {
        Portfolio defaultPortfolio = new Portfolio();
        assertNull(defaultPortfolio.getId(), "Portfolio ID should be null");
        assertNull(defaultPortfolio.getUser(), "Portfolio user should be null");
    }

    @Test
    public void testPortfolio_ParameterizedConstructor() {
        assertNotNull(portfolio.getUser(), "Portfolio user should not be null");
        assertEquals(user, portfolio.getUser(), "Portfolio user should match");
    }

    @Test
    public void testSettersAndGetters() {
        UUID newId = UUID.randomUUID();
        User newUser = new User("Demo User 2", "demo.user2@email.com", "asdf", currency, "demoaccount2");
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
        assertEquals(validUUID, portfolio.getId(), "Portfolio ID should be unchanged");
        assertEquals(user, portfolio.getUser(), "Portfolio user should be unchanged");
    }

}
