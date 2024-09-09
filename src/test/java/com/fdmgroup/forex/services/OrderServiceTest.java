package com.fdmgroup.forex.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fdmgroup.forex.enums.OrderStatus;
import com.fdmgroup.forex.enums.OrderType;
import com.fdmgroup.forex.exceptions.RecordNotFoundException;
import com.fdmgroup.forex.models.Order;
import com.fdmgroup.forex.repos.OrderRepo;

public class OrderServiceTest {

    @Mock
    private OrderRepo orderRepo;

    @InjectMocks
    private OrderService orderService;

    private UUID existingId;
    private UUID nonExistingId;
    private UUID userId;
    private Order order;
    private List<Order> orders;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        existingId = UUID.randomUUID();
        nonExistingId = UUID.randomUUID();
        userId = UUID.randomUUID();
        order = new Order();
        orders = new ArrayList<>();
        orders.add(order);
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
        when(orderRepo.findByUser_Id(userId)).thenReturn(orders);
        List<Order> foundOrders = orderService.findOrdersByUserId(userId);
        assertEquals(orders, foundOrders, "OrderService should find orders by a valid user ID");
        verify(orderRepo, times(1)).findByUser_Id(userId);
    }

    @Test
    void testFindOrdersByUserId_WhenNoOrdersExist() {
        when(orderRepo.findByUser_Id(userId)).thenReturn(new ArrayList<>());
        List<Order> foundOrders = orderService.findOrdersByUserId(userId);
        assertTrue(foundOrders.isEmpty(), "OrderService should return an empty list if no orders exist for a user");
        verify(orderRepo, times(1)).findByUser_Id(userId);
    }

    @Test
    void testFindOrdersByUserIdAndOrderStatus_WhenOrdersExist() {
        OrderStatus status = OrderStatus.ACTIVE;
        when(orderRepo.findByUser_IdAndOrderStatus(userId, status)).thenReturn(orders);
        List<Order> foundOrders = orderService.findOrdersByUserIdAndOrderStatus(userId, status);
        assertEquals(orders, foundOrders, "OrderService should find orders by user ID and order status");
        verify(orderRepo, times(1)).findByUser_IdAndOrderStatus(userId, status);
    }

    @Test
    void testFindOrdersByUserIdAndOrderStatus_WhenNoOrdersExist() {
        OrderStatus status = OrderStatus.ACTIVE;
        when(orderRepo.findByUser_IdAndOrderStatus(userId, status)).thenReturn(new ArrayList<>());
        List<Order> foundOrders = orderService.findOrdersByUserIdAndOrderStatus(userId, status);
        assertTrue(foundOrders.isEmpty(), "OrderService should return an empty list if no orders match the user ID and order status");
        verify(orderRepo, times(1)).findByUser_IdAndOrderStatus(userId, status);
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
        assertTrue(foundOrders.isEmpty(), "OrderService should return an empty list if no orders match the order status");
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
}
