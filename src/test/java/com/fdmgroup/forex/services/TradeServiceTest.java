package com.fdmgroup.forex.services;

import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.*;
import org.mockito.*;

import com.fdmgroup.forex.models.Order;
import com.fdmgroup.forex.models.Portfolio;
import com.fdmgroup.forex.repos.TradeRepo;

public class TradeServiceTest {

    @Mock
    private TradeRepo tradeRepo;

    @InjectMocks
    private TradeService tradeService;

    private Order order;
    private Portfolio portfolio;
    private UUID portfolioId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        order = mock(Order.class);
        portfolio = order.getPortfolio();
        portfolioId = portfolio.getId();
    }

    @Test
    void testFindTradesByPortfolioId_ReturnsListOfTrades() {


    }


}
