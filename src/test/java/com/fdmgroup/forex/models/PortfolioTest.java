package com.fdmgroup.forex.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

public class PortfolioTest {

    private Portfolio portfolio;
    private UUID userId;

    @BeforeEach
    public void setUp() {
        userId = UUID.randomUUID();
        portfolio = new Portfolio(userId);
    }

    @Test
    public void testPortfolio_DefaultConstructor() {
        Portfolio defaultPortfolio = new Portfolio();
        assertNull(defaultPortfolio.getId(), "Portfolio ID should be null");
        assertNull(defaultPortfolio.getUserId(), "Portfolio user ID should be null");
    }

    @Test
    public void testPortfolio_ParameterizedConstructor() {
        assertNotNull(portfolio.getUserId(), "Portfolio user ID should not be null");
        assertEquals(userId, portfolio.getUserId(), "Portfolio user ID should match");
    }

    @Test
    public void testSettersAndGetters() {
        UUID newId = UUID.randomUUID();
        UUID newUserId = UUID.randomUUID();
        portfolio.setId(newId);
        portfolio.setUserId(newUserId);
        assertEquals(newId, portfolio.getId(), "Portfolio ID should match");
        assertEquals(newUserId, portfolio.getUserId(), "Portfolio user ID should match");
    }

    @Test
    public void testSetters_IgnoreNullValues() {
        UUID validUUID = UUID.randomUUID();
        portfolio.setId(validUUID);
        UUID nullId = null;
        UUID nullUserId = null;
        portfolio.setId(nullId);
        portfolio.setUserId(nullUserId);
        assertEquals(validUUID, portfolio.getId(), "Portfolio ID should be unchanged");
        assertEquals(userId, portfolio.getUserId(), "Portfolio user ID should be unchanged");
    }

    @Test
    public void testToString() {
        String expectedString = "Portfolio [id=" + portfolio.getId() + ", userId=" + userId + "]";
        assertEquals(expectedString, portfolio.toString(), "toString should match expected output");
    }

}
