package com.fdmgroup.forex.models;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fdmgroup.forex.enums.OrderSide;
import com.fdmgroup.forex.enums.OrderStatus;
import com.fdmgroup.forex.enums.OrderType;

import java.util.Date;
import java.util.UUID;

public class OrderTest {

    private Portfolio portfolio;
    private Currency baseFx;
    private Currency quoteFx;
    private Order order;
    private Date expiryDate;
    
    @BeforeEach
    public void setUp() {
        portfolio = mock(Portfolio.class);
        baseFx = new Currency("USD", "U.S. Dollars");
        quoteFx = new Currency("EUR", "Euros");
        expiryDate = new Date();
        order = new Order(portfolio, OrderType.LIMIT, OrderSide.BUY, OrderStatus.ACTIVE, expiryDate, baseFx, quoteFx, 1000, 500);
    }

    @Test
    public void testOrder_DefaultConstructor() {
        Order defaultOrder = new Order();
        assertNull(defaultOrder.getId(), "Order ID should be null");
        assertNull(defaultOrder.getPortfolio(), "Order portfolio should be null");
        assertNull(defaultOrder.getBaseFx(), "Order base currency should be null");
        assertNull(defaultOrder.getQuoteFx(), "Order quote currency should be null");
        assertNull(defaultOrder.getCreationDate(), "Order creation date should be null");
    }

    @Test
    public void testOrder_ParameterizedConstructor() {
        assertNotNull(order.getPortfolio(), "Order portfolio should not be null");
        assertEquals(portfolio, order.getPortfolio(), "Order portfolio should match");
        assertEquals(OrderType.LIMIT, order.getOrderType(), "Order type should be LIMIT");
        assertEquals(OrderSide.BUY, order.getOrderSide(), "Order side should be BUY");
        assertEquals(OrderStatus.ACTIVE, order.getOrderStatus(), "Order status should be ACTIVE");
        assertEquals(baseFx, order.getBaseFx(), "Order base currency should match");
        assertEquals(quoteFx, order.getQuoteFx(), "Order quote currency should match");
        assertEquals(1000, order.getTotal(), "Order total should match");
        assertEquals(500, order.getResidual(), "Order residual should match");
    }

    @Test
    public void testSettersAndGetters() {
        Order order = new Order();
        UUID newId = UUID.randomUUID();
        Portfolio newPortfolio = mock(Portfolio.class);
        Currency newBaseFx = new Currency("GBP", "British Pound");
        Currency newQuoteFx = new Currency("JPY", "Japanese Yen");

        order.setId(newId);
        order.setPortfolio(newPortfolio);
        order.setBaseFx(newBaseFx);
        order.setQuoteFx(newQuoteFx);
        order.setTotal(2000);
        order.setResidual(1500);
        order.setLimit(1.25);

        assertEquals(newId, order.getId(), "Order ID should match");
        assertEquals(newPortfolio, order.getPortfolio(), "Order portfolio should match");
        assertEquals(newBaseFx, order.getBaseFx(), "Order base currency should match");
        assertEquals(newQuoteFx, order.getQuoteFx(), "Order quote currency should match");
        assertEquals(2000, order.getTotal(), "Order total should match");
        assertEquals(1500, order.getResidual(), "Order residual should match");
        assertEquals(1.25, order.getLimit(), "Order limit should match");
    }

    @Test
    public void testSetters_IgnoreNullValues() {
        UUID validUUID = UUID.randomUUID();
        order.setId(validUUID);
        order.setId(null);
        order.setPortfolio(null);
        order.setBaseFx(null);
        order.setQuoteFx(null);

        assertEquals(validUUID, order.getId(), "Order ID should be unchanged");
        assertEquals(portfolio, order.getPortfolio(), "Order portfolio should be unchanged");
        assertEquals(baseFx, order.getBaseFx(), "Order base currency should be unchanged");
        assertEquals(quoteFx, order.getQuoteFx(), "Order quote currency should be unchanged");
    }
}
