package com.fdmgroup.forex.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fdmgroup.forex.enums.OrderSide;
import com.fdmgroup.forex.enums.OrderStatus;
import com.fdmgroup.forex.enums.OrderType;
import com.fdmgroup.forex.exceptions.RecordNotFoundException;
import com.fdmgroup.forex.models.Order;
import com.fdmgroup.forex.repos.OrderRepo;

@Service
public class OrderService {

    private OrderRepo orderRepo;

    public OrderService(OrderRepo orderRepo) {
        this.orderRepo = orderRepo;
    }

    public List<Order> findAllOrders() {
        return orderRepo.findAll();
    }

    public Order findOrderById(UUID id) {
        Optional<Order> orderOptional = orderRepo.findById(id);
        return orderOptional.orElseThrow(() -> 
            new RecordNotFoundException("Order with id '" + id + "'' not found"));
    }

    public List<Order> findOrdersByUserId(UUID userId) {
        List<Order> orders = orderRepo.findByUser_Id(userId);
        return orders;
    }

    public List<Order> findOrdersByUserIdAndOrderStatus(UUID userId, OrderStatus orderStatus) {
        List<Order> orders = orderRepo.findByUser_IdAndOrderStatus(userId, orderStatus);
        return orders;
    }

    public List<Order> findOrdersByOrderSide(OrderSide orderSide) {
        List<Order> orders = orderRepo.findByOrderSide(orderSide);
        return orders;
    }

    public List<Order> findOrdersByOrderSideAndUserId(OrderSide orderSide, UUID userId) {
        List<Order> orders = orderRepo.findByOrderSideAndUser_Id(orderSide, userId);
        return orders;
    }

    public List<Order> findOrdersByOrderStatus(OrderStatus orderStatus) {
        List<Order> orders = orderRepo.findByOrderStatus(orderStatus);
        return orders;
    }

    public List<Order> findOrdersByOrderType(OrderType orderType) {
        List<Order> orders = orderRepo.findByOrderType(orderType);
        return orders;
    }

    public List<Order> findOrdersByOrderTypeAndUserId(OrderType orderType, UUID userId) {
        List<Order> orders = orderRepo.findByOrderTypeAndUser_Id(orderType, userId);
        return orders;
    }

    public List<Order> findOrdersByOrderStatusAndOrderType(OrderStatus orderStatus, OrderType orderType) {
        List<Order> orders = orderRepo.findByOrderStatusAndOrderType(orderStatus, orderType);
        return orders;
    }

    public List<Order> findOrdersByOrderStatusAndOrderTypeAndUserId(OrderStatus orderStatus, OrderType orderType, UUID userId) {
        List<Order> orders = orderRepo.findByOrderStatusAndOrderTypeAndUser_Id(orderStatus, orderType, userId);
        return orders;
    }

    public List<Order> findOrdersByBaseFxId(String baseFxCurrencyCode) {
        List<Order> orders = orderRepo.findByBaseFx_CurrencyCode(baseFxCurrencyCode);
        return orders;
    }

    public List<Order> findOrdersByBaseFxIdAndUserId(String baseFxCurrencyCode, UUID userId) {
        List<Order> orders = orderRepo.findByBaseFx_CurrencyCodeAndUser_Id(baseFxCurrencyCode, userId);
        return orders;
    }

    public List<Order> findOrdersByQuoteFxId(String quoteFxCurrencyCode) {
        List<Order> orders = orderRepo.findByQuoteFx_CurrencyCode(quoteFxCurrencyCode);
        return orders;
    }

    public List<Order> findOrdersByQuoteFxIdAndUserId(String quoteFxCurrencyCode, UUID userId) {
        List<Order> orders = orderRepo.findByQuoteFx_CurrencyCodeAndUser_Id(quoteFxCurrencyCode, userId);
        return orders;
    }

    public List<Order> findOrdersByBaseFxAndQuoteFxId(String baseFxCurrencyCode, String quoteFxCurrencyCode) {
        List<Order> orders = orderRepo.findByBaseFx_CurrencyCodeAndQuoteFx_CurrencyCode(baseFxCurrencyCode, quoteFxCurrencyCode);
        return orders;
    }

    public List<Order> findOrdersByBaseFxAndQuoteFxIdAndUserId(String baseFxCurrencyCode, String quoteFxCurrencyCode, UUID userId) {
        List<Order> orders = orderRepo.findByBaseFx_CurrencyCodeAndQuoteFx_CurrencyCodeAndUser_Id(baseFxCurrencyCode, quoteFxCurrencyCode, userId);
        return orders;
    }

}
