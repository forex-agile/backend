package com.fdmgroup.forex.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.*;
import org.mockito.*;

import com.fdmgroup.forex.models.Order;
import com.fdmgroup.forex.models.Trade;
import com.fdmgroup.forex.repos.TradeRepo;

public class TradeServiceTest {

    @Mock
    private TradeRepo tradeRepo;

    @InjectMocks
    private TradeService tradeService;

    private Order order;
    private UUID portfolioId;
    private UUID tradeId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        order = new Order();
        portfolioId = UUID.randomUUID();
        tradeId = UUID.randomUUID();
    }

    @Test
    void testCreateTrade_Success() {
        double baseFxAmount = 100.0;
        double quoteFxAmount = 200.0;
        Trade expectedTrade = new Trade(tradeId, order, baseFxAmount, quoteFxAmount);
        when(tradeRepo.save(any(Trade.class))).thenReturn(expectedTrade);
        Trade actualTrade = tradeService.createTrade(tradeId, order, baseFxAmount, quoteFxAmount);

        assertNotNull(actualTrade);
        assertEquals(tradeId, actualTrade.getId(), "Trade ID should match");
        assertEquals(order, actualTrade.getOrder(), "Trade order should match");
        assertEquals(baseFxAmount, actualTrade.getBaseFxAmount(), "Trade baseFx amounts should match");
        assertEquals(quoteFxAmount, actualTrade.getQuoteFxAmount(), "Trade quoteFx amounts should match");
        verify(tradeRepo).save(any(Trade.class));
    }

    @Test
    void testCreateTrade_NullId() {
        double baseFxAmount = 100.0;
        double quoteFxAmount = 200.0;

        assertThrows(IllegalArgumentException.class, () -> 
            tradeService.createTrade(null, order, baseFxAmount, quoteFxAmount), "Null ID should throw IllegalArgumentException");
        verify(tradeRepo, never()).save(any(Trade.class));
    }

    @Test
    void testCreateTrade_NegativeBaseFxAmount() {
        double baseFxAmount = -100.0;
        double quoteFxAmount = 200.0;

        assertThrows(IllegalArgumentException.class, () -> 
            tradeService.createTrade(tradeId, order, baseFxAmount, quoteFxAmount), "Negative baseFx amount should throw IllegalArgumentException");
        verify(tradeRepo, never()).save(any(Trade.class));
    }

    @Test
    void testCreateTrade_NegativeQuoteFxAmount() {
        double baseFxAmount = 100.0;
        double quoteFxAmount = -200.0;

        assertThrows(IllegalArgumentException.class, () -> 
            tradeService.createTrade(tradeId, order, baseFxAmount, quoteFxAmount), "Negative quoteFx amount should throw IllegalArgumentException");
        verify(tradeRepo, never()).save(any(Trade.class));
    }

    @Test
    void testCreateTrade_PropagatesExceptionFromRepo() {
        double baseFxAmount = 100.0;
        double quoteFxAmount = 200.0;
        when(tradeRepo.save(any(Trade.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> 
            tradeService.createTrade(tradeId, order, baseFxAmount, quoteFxAmount));
        verify(tradeRepo).save(any(Trade.class));
    }

    @Test
    void testFindTradesByPortfolioId_ReturnsListOfTrades() {
        List<Trade> expectedTrades = Arrays.asList(
            new Trade(UUID.randomUUID(), new Order(), 100.0, 200.0),
            new Trade(UUID.randomUUID(), new Order(), 150.0, 300.0)
        );
        when(tradeRepo.findByOrder_Portfolio_Id(portfolioId)).thenReturn(expectedTrades);
        List<Trade> actualTrades = tradeService.findTradesByPortfolioId(portfolioId);

        assertEquals(expectedTrades, actualTrades, "List of trades should match");
        verify(tradeRepo).findByOrder_Portfolio_Id(portfolioId);
    }

    @Test
    void testFindTradesByPortfolioId_ReturnsEmptyList_WhenNoTradesFound() {
        when(tradeRepo.findByOrder_Portfolio_Id(portfolioId)).thenReturn(Collections.emptyList());
        List<Trade> actualTrades = tradeService.findTradesByPortfolioId(portfolioId);

        assertTrue(actualTrades.isEmpty(), "List of trades should be empty");
        verify(tradeRepo).findByOrder_Portfolio_Id(portfolioId);
    }

    @Test
    void testFindTradesByPortfolioId_HandlesNullPortfolioId() {
        when(tradeRepo.findByOrder_Portfolio_Id(null)).thenReturn(Collections.emptyList());
        List<Trade> actualTrades = tradeService.findTradesByPortfolioId(null);

        assertTrue(actualTrades.isEmpty(), "List of trades should be empty");
        verify(tradeRepo).findByOrder_Portfolio_Id(null);
    }

    @Test
    void testFindTradesByPortfolioId_PropagatesExceptionFromRepo() {
        UUID portfolioId = UUID.randomUUID();
        when(tradeRepo.findByOrder_Portfolio_Id(portfolioId)).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> tradeService.findTradesByPortfolioId(portfolioId));
        verify(tradeRepo).findByOrder_Portfolio_Id(portfolioId);
    }

}
