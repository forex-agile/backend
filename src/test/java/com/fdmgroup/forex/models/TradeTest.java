package com.fdmgroup.forex.models;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.*;

public class TradeTest {

    private Order order;
    private Trade trade;
    private double baseUSD;
    private double quoteHKD;

    @BeforeEach
    public void setUp() {
        order = mock(Order.class);
        baseUSD = 100;
        quoteHKD = 780;
        trade = new Trade(UUID.randomUUID(), order, baseUSD, quoteHKD);
        
    }

    @Test
    public void testTrade_DefaultConstructor() {
        Trade defaultTrade = new Trade();
        assertNull(defaultTrade.getId());
        assertNull(defaultTrade.getOrder());
        assertNull(defaultTrade.getExecutionDate());
        assertEquals(0, defaultTrade.getBaseFxAmount());
        assertEquals(0, defaultTrade.getQuoteFxAmount());
    }

    @Test
    public void testTrade_ParameterizedConstructor() {
        assertNotNull(trade.getOrder(), "Trade order should not be null");
        assertEquals(order, trade.getOrder(), "Trade order should match");
        assertEquals(baseUSD, trade.getBaseFxAmount());
        assertEquals(quoteHKD, trade.getQuoteFxAmount());
    }

    @Test
    public void testSettersAndGetters() {
        Order newOrder = mock(Order.class);
        UUID newId = UUID.randomUUID();
        double newBaseUSD = 200;
        double newQuoteHKD = 1560;

        trade.setOrder(newOrder);
        trade.setId(newId);
        trade.setBaseFxAmount((newBaseUSD));
        trade.setQuoteFxAmount(newQuoteHKD);

        assertNotEquals(order, trade.getOrder());
        assertEquals(newOrder, trade.getOrder());
        assertEquals(newId, trade.getId());
        assertNotEquals(baseUSD, trade.getBaseFxAmount());
        assertEquals(newBaseUSD, trade.getBaseFxAmount());
        assertNotEquals(quoteHKD, trade.getQuoteFxAmount());
        assertEquals(newQuoteHKD, trade.getQuoteFxAmount());
    }

    @Test
    public void testSetters_IgnoreNullValues() {
        trade.setBaseFxAmount(0);
        trade.setQuoteFxAmount(0);

        assertEquals(baseUSD, trade.getBaseFxAmount());
        assertEquals(quoteHKD, trade.getQuoteFxAmount());
    }


}
