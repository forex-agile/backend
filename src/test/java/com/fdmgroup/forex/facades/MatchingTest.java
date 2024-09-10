package com.fdmgroup.forex.facades;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fdmgroup.forex.repos.*;
import com.fdmgroup.forex.services.*;
import com.fdmgroup.forex.enums.*;
import com.fdmgroup.forex.models.*;

import java.util.Arrays;
import java.util.Date;
class MatchingTest {

    @Mock
    private OrderRepo orderRepo;

    @Mock
    private AssetRepo assetRepo;

    @Mock
    private AssetService assetService;

    @InjectMocks
    private Matching matching;

    private Portfolio portfolio1;
    private Portfolio portfolio2;
    private Currency usd;
    private Currency eur;
    private Asset sellAsset;
    private Asset buyAsset;
    private Order sellLimitOrder;
    private Order buyLimitOrder;
    private Order sellMarketOrder;
    private Order buyMarketOrder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize mock data
        usd = new Currency("USD", "US Dollar");
        eur = new Currency("EUR", "Euro");

        portfolio1 = new Portfolio();
        portfolio2 = new Portfolio();

        sellAsset = new Asset(portfolio1, usd, 1000.0);
        buyAsset = new Asset(portfolio2, eur, 1000.0);

        // Set different creation dates
        sellLimitOrder = new Order(portfolio1, OrderType.LIMIT, OrderSide.SELL, OrderStatus.ACTIVE, null, usd, eur, 1000.0, 1000.0, 1100.0);
        sellLimitOrder.setCreationDate(new Date()); // Set creation date

        buyLimitOrder = new Order(portfolio2, OrderType.LIMIT, OrderSide.BUY, OrderStatus.ACTIVE, null, eur, usd, 1000.0, 1000.0, 1200.0);
        buyLimitOrder.setCreationDate(new Date(System.currentTimeMillis() + 1000)); // Set a later creation date

        sellMarketOrder = new Order(portfolio1, OrderType.MARKET, OrderSide.SELL, OrderStatus.ACTIVE, null, usd, eur, 1000.0, 1000.0);
        sellMarketOrder.setCreationDate(new Date(System.currentTimeMillis() + 2000)); // Set a later creation date

        buyMarketOrder = new Order(portfolio2, OrderType.MARKET, OrderSide.BUY, OrderStatus.ACTIVE, null, eur, usd, 1000.0, 1000.0);
        buyMarketOrder.setCreationDate(new Date(System.currentTimeMillis() + 3000)); // Set a later creation date

        // Mock the asset service to return the corresponding asset
        when(assetService.findAssetByPortfolioAndCurrency(portfolio1, usd)).thenReturn(sellAsset);
        when(assetService.findAssetByPortfolioAndCurrency(portfolio2, eur)).thenReturn(buyAsset);

        // Mock the asset repo to save the assets
        when(assetRepo.save(any(Asset.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void testMatchBuyLimitWithSellLimit() {
        // Arrange
        when(orderRepo.findActiveOrdersByFx(eur.getCurrencyCode(), usd.getCurrencyCode(), OrderStatus.ACTIVE))
                .thenReturn(Arrays.asList(buyLimitOrder));

        // Act
        System.out.println("testMatchBuyLimitWithSellLimit---------------------");
        matching.matchOrders(sellLimitOrder);

        // Assert
        assertEquals(OrderStatus.ACTIVE, sellLimitOrder.getOrderStatus());
        assertEquals(OrderStatus.CLEARED, buyLimitOrder.getOrderStatus());
        assertEquals(1000.0-833.3333333333333333333333, sellLimitOrder.getResidual());
        assertEquals(0.0, buyLimitOrder.getResidual());
        assertEquals(1000.0-833.3333333333333333333333, sellAsset.getBalance());
        assertEquals(0.0, buyAsset.getBalance());
    }

    @Test
    void testMatchBuyMarketWithSellLimit() {
        // Arrange
        when(orderRepo.findActiveOrdersByFx(eur.getCurrencyCode(), usd.getCurrencyCode(), OrderStatus.ACTIVE))
                .thenReturn(Arrays.asList(buyLimitOrder));

        // Act
        System.out.println("testMatchBuyMarketWithSellLimit--------------------");
        matching.matchOrders(sellMarketOrder);

        // Assert
        assertEquals(OrderStatus.ACTIVE, sellMarketOrder.getOrderStatus());
        assertEquals(OrderStatus.CLEARED, buyLimitOrder.getOrderStatus());
        assertEquals(1000.0-833.3333333333333333333333, sellMarketOrder.getResidual());
        assertEquals(0.0, buyLimitOrder.getResidual());
    }
    
    @Test
    void testUnableToMatch() {
        // Arrange
        when(orderRepo.findActiveOrdersByFx(eur.getCurrencyCode(), usd.getCurrencyCode(), OrderStatus.ACTIVE))
                .thenReturn(Arrays.asList()); // No matching orders

        // Act
        matching.matchOrders(sellLimitOrder);

        // Assert
        assertEquals(OrderStatus.ACTIVE, sellLimitOrder.getOrderStatus());
    }

    @Test
    void testOneOrderMatchesMultipleOrders() {
        // Arrange
        Order anotherSellLimitOrder = new Order(portfolio2, OrderType.LIMIT, OrderSide.BUY, OrderStatus.ACTIVE, null, eur, usd, 1000.0, 1000.0, 1150.0);
        anotherSellLimitOrder.setCreationDate(new Date(System.currentTimeMillis() + 2000)); // Set a later creation date

        when(orderRepo.findActiveOrdersByFx(eur.getCurrencyCode(), usd.getCurrencyCode(), OrderStatus.ACTIVE))
                .thenReturn(Arrays.asList(buyLimitOrder, anotherSellLimitOrder));

        // Act
        System.out.println("testOneOrderMatchesMultipleOrders------------------------------");
        matching.matchOrders(sellLimitOrder);

        // Assert
        assertEquals(OrderStatus.ACTIVE, sellLimitOrder.getOrderStatus());
        assertEquals(OrderStatus.CLEARED, buyLimitOrder.getOrderStatus());
        assertEquals(OrderStatus.CANCELLED, anotherSellLimitOrder.getOrderStatus());
        assertEquals(1000.0-833.3333333333333333333333, sellLimitOrder.getResidual());
        assertEquals(0.0, buyLimitOrder.getResidual());
        assertEquals(1000.0, anotherSellLimitOrder.getResidual());
    }
}