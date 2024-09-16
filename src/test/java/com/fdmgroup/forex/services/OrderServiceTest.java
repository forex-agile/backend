package com.fdmgroup.forex.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.*;
import org.mockito.*;

import com.fdmgroup.forex.enums.OrderStatus;
import com.fdmgroup.forex.enums.OrderType;
import com.fdmgroup.forex.exceptions.RecordNotFoundException;
import com.fdmgroup.forex.facades.Matching;
import com.fdmgroup.forex.models.*;
import com.fdmgroup.forex.models.Order;
import com.fdmgroup.forex.repos.*;
import com.fdmgroup.forex.security.AuthUserService;

public class OrderServiceTest {

    @Mock
    private CurrencyRepo currencyRepo;

    @Mock
    private OrderRepo orderRepo;

    @Mock
    private AssetService assetService;

    @Mock
    private PortfolioService portfolioService;

    @Mock
    private Matching matching;

    @Mock
    private AuthUserService authUserService;

    @InjectMocks
    private OrderService orderService;

    private UUID existingId;
    private UUID nonExistingId;
    private UUID userId;
    private Order order;
    private List<Order> orders;
    private Portfolio portfolio;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderService(orderRepo, portfolioService, currencyRepo, authUserService, matching, assetService);
        existingId = UUID.randomUUID();
        nonExistingId = UUID.randomUUID();
        userId = UUID.randomUUID();
        order = new Order();
        orders = new ArrayList<>();
        orders.add(order);
        portfolio = new Portfolio();
        portfolio.setId(UUID.randomUUID());
        order.setPortfolio(portfolio);
    }

    @Test
    void testFindAllOrders_ReturnsListOfOrders() {
        when(orderRepo.findAll()).thenReturn(orders);
        List<Order> foundOrders = orderService.findAllOrders();
        assertEquals(orders, foundOrders, "OrderService should return the list of all orders");
        verify(orderRepo, times(1)).findAll();
    }

    @Test
    void testFindAllOrders_WhenNoOrdersExist() {
        when(orderRepo.findAll()).thenReturn(new ArrayList<>());
        List<Order> foundOrders = orderService.findAllOrders();
        assertTrue(foundOrders.isEmpty(), "OrderService should return an empty list when no orders exist");
        verify(orderRepo, times(1)).findAll();
    }

    @Test
    void testFindOrderById_WhenOrderExists() {
        when(orderRepo.findById(existingId)).thenReturn(Optional.of(order));
        Order foundOrder = orderService.findOrderById(existingId);
        assertEquals(order, foundOrder, "OrderService should find an order with a valid ID");
        verify(orderRepo, times(1)).findById(existingId);
    }

    @Test
    void testFindOrderById_WhenOrderDoesNotExist() {
        when(orderRepo.findById(nonExistingId)).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> orderService.findOrderById(nonExistingId),
                "OrderService should throw RecordNotFoundException for an invalid ID");
        verify(orderRepo, times(1)).findById(nonExistingId);
    }

    @Test
    void testFindOrdersByUserId_WhenOrdersExist() {
        when(orderRepo.findByPortfolio_User_Id(userId)).thenReturn(orders);
        List<Order> foundOrders = orderService.findOrdersByUserId(userId);
        assertEquals(orders, foundOrders, "OrderService should find orders by a valid user ID");
        verify(orderRepo, times(1)).findByPortfolio_User_Id(userId);
    }

    @Test
    void testFindOrdersByUserId_WhenNoOrdersExist() {
        when(orderRepo.findByPortfolio_User_Id(userId)).thenReturn(new ArrayList<>());
        List<Order> foundOrders = orderService.findOrdersByUserId(userId);
        assertTrue(foundOrders.isEmpty(), "OrderService should return an empty list if no orders exist for a user");
        verify(orderRepo, times(1)).findByPortfolio_User_Id(userId);
    }

    @Test
    void testFindOrdersByUserIdAndOrderStatus_WhenOrdersExist() {
        OrderStatus status = OrderStatus.ACTIVE;
        when(orderRepo.findByPortfolio_User_IdAndOrderStatus(userId, status)).thenReturn(orders);
        List<Order> foundOrders = orderService.findOrdersByUserIdAndOrderStatus(userId, status);
        assertEquals(orders, foundOrders, "OrderService should find orders by user ID and order status");
        verify(orderRepo, times(1)).findByPortfolio_User_IdAndOrderStatus(userId, status);
    }

    @Test
    void testFindOrdersByUserIdAndOrderStatus_WhenNoOrdersExist() {
        OrderStatus status = OrderStatus.ACTIVE;
        when(orderRepo.findByPortfolio_User_IdAndOrderStatus(userId, status)).thenReturn(new ArrayList<>());
        List<Order> foundOrders = orderService.findOrdersByUserIdAndOrderStatus(userId, status);
        assertTrue(foundOrders.isEmpty(),
                "OrderService should return an empty list if no orders match the user ID and order status");
        verify(orderRepo, times(1)).findByPortfolio_User_IdAndOrderStatus(userId, status);
    }

    @Test
    void testFindOrdersByOrderStatus_WhenOrdersExist() {
        OrderStatus status = OrderStatus.CLOSED;
        when(orderRepo.findByOrderStatus(status)).thenReturn(orders);
        List<Order> foundOrders = orderService.findOrdersByOrderStatus(status);
        assertEquals(orders, foundOrders, "OrderService should find orders by order status");
        verify(orderRepo, times(1)).findByOrderStatus(status);
    }

    @Test
    void testFindOrdersByOrderStatus_WhenNoOrdersExist() {
        OrderStatus status = OrderStatus.CLOSED;
        when(orderRepo.findByOrderStatus(status)).thenReturn(new ArrayList<>());
        List<Order> foundOrders = orderService.findOrdersByOrderStatus(status);
        assertTrue(foundOrders.isEmpty(),
                "OrderService should return an empty list if no orders match the order status");
        verify(orderRepo, times(1)).findByOrderStatus(status);
    }

    @Test
    void testFindOrdersByOrderType_WhenOrdersExist() {
        OrderType type = OrderType.MARKET;
        when(orderRepo.findByOrderType(type)).thenReturn(orders);
        List<Order> foundOrders = orderService.findOrdersByOrderType(type);
        assertEquals(orders, foundOrders, "OrderService should find orders by order type");
        verify(orderRepo, times(1)).findByOrderType(type);
    }

    @Test
    void testFindOrdersByOrderType_WhenNoOrdersExist() {
        OrderType type = OrderType.MARKET;
        when(orderRepo.findByOrderType(type)).thenReturn(new ArrayList<>());
        List<Order> foundOrders = orderService.findOrdersByOrderType(type);
        assertTrue(foundOrders.isEmpty(), "OrderService should return an empty list if no orders match the order type");
        verify(orderRepo, times(1)).findByOrderType(type);
    }

    @Test
    void testCancelOrderByUserIdAndOrderId_SuccessfulCancellation() {
        when(orderRepo.findById(existingId)).thenReturn(Optional.of(order));
        when(portfolioService.findPortfolioByUserId(userId)).thenReturn(portfolio);
        when(orderRepo.save(any(Order.class))).thenReturn(order);

        Order cancelledOrder = orderService.cancelOrderByUserIdAndOrderId(userId, existingId);

        assertEquals(OrderStatus.CANCELLED, cancelledOrder.getOrderStatus());
        verify(orderRepo, times(1)).findById(existingId);
        verify(portfolioService, times(1)).findPortfolioByUserId(userId);
        verify(orderRepo, times(1)).save(order);
    }

    @Test
    void testCancelOrderByUserIdAndOrderId_OrderNotFound() {
        when(orderRepo.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class,
                () -> orderService.cancelOrderByUserIdAndOrderId(userId, nonExistingId));

        verify(orderRepo, times(1)).findById(nonExistingId);
        verify(portfolioService, never()).findPortfolioByUserId(any(UUID.class));
        verify(orderRepo, never()).save(any(Order.class));
    }

    @Test
    void testCancelOrderByUserIdAndOrderId_PortfolioMismatch() {
        Order order = new Order();
        order.setId(existingId);
        Portfolio orderPortfolio = new Portfolio();
        orderPortfolio.setId(UUID.randomUUID());
        order.setPortfolio(orderPortfolio);

        Portfolio userPortfolio = new Portfolio();
        userPortfolio.setId(UUID.randomUUID());

        when(orderRepo.findById(existingId)).thenReturn(Optional.of(order));
        when(portfolioService.findPortfolioByUserId(userId)).thenReturn(userPortfolio);
        assertThrows(RecordNotFoundException.class,
                () -> orderService.cancelOrderByUserIdAndOrderId(userId, existingId));

        verify(orderRepo, times(1)).findById(existingId);
        verify(portfolioService, times(1)).findPortfolioByUserId(userId);
        verify(orderRepo, never()).save(any(Order.class));
    }

    @Test
    void testPortfoliosMatch_True() {
        Order matchingOrder = new Order();
        matchingOrder.setPortfolio(portfolio);
        assertTrue(orderService.portfoliosMatch(matchingOrder, portfolio));
    }

    @Test
    void testPortfoliosMatch_False() {
        Order nonMatchingOrder = new Order();
        Portfolio differentPortfolio = new Portfolio();
        differentPortfolio.setId(UUID.randomUUID());
        nonMatchingOrder.setPortfolio(differentPortfolio);
        assertFalse(orderService.portfoliosMatch(nonMatchingOrder, portfolio));
    }
}
