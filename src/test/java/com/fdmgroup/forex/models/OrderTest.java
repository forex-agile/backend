package com.fdmgroup.forex.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fdmgroup.forex.enums.OrderSide;
import com.fdmgroup.forex.enums.OrderStatus;
import com.fdmgroup.forex.enums.OrderType;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.UUID;

public class OrderTest {

    private User user;
    private Currency baseFx;
    private Currency quoteFx;
    private Order order;
    private Date expiryDate;
    
    @BeforeEach
    public void setUp() {
        user = new User("Demo User", "demo.user@email.com", "qwerty", baseFx, "demoaccount");
        baseFx = new Currency("USD", "U.S. Dollars");
        quoteFx = new Currency("EUR", "Euros");
        expiryDate = new Date();
        order = new Order(user, OrderType.LIMIT, OrderSide.BUY, OrderStatus.ACTIVE, expiryDate, baseFx, quoteFx, 1000, 500);
    }

    @Test
    public void testOrder_DefaultConstructor() {
        Order defaultOrder = new Order();
        assertNull(defaultOrder.getId(), "Order ID should be null");
        assertNull(defaultOrder.getUser(), "Order user should be null");
        assertNull(defaultOrder.getBaseFx(), "Order base currency should be null");
        assertNull(defaultOrder.getQuoteFx(), "Order quote currency should be null");
        assertNull(defaultOrder.getCreationDate(), "Order creation date should be null");
    }

    @Test
    public void testOrder_ParameterizedConstructor() {
        assertNotNull(order.getUser(), "Order user should not be null");
        assertEquals(user, order.getUser(), "Order user should match");
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
        User newUser = new User("Demo User 2", "demo.user2@email.com", "asdf", baseFx, "demoaccount2");
        Currency newBaseFx = new Currency("GBP", "British Pound");
        Currency newQuoteFx = new Currency("JPY", "Japanese Yen");

        order.setId(newId);
        order.setUser(newUser);
        order.setBaseFx(newBaseFx);
        order.setQuoteFx(newQuoteFx);
        order.setTotal(2000);
        order.setResidual(1500);
        order.setLimit(1.25);

        assertEquals(newId, order.getId(), "Order ID should match");
        assertEquals(newUser, order.getUser(), "Order user should match");
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
        order.setUser(null);
        order.setBaseFx(null);
        order.setQuoteFx(null);

        assertEquals(validUUID, order.getId(), "Order ID should be unchanged");
        assertEquals(user, order.getUser(), "Order user should be unchanged");
        assertEquals(baseFx, order.getBaseFx(), "Order base currency should be unchanged");
        assertEquals(quoteFx, order.getQuoteFx(), "Order quote currency should be unchanged");
    }
}