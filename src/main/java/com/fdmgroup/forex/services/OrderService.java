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

    public List<Order> findOrdersByOrderSide(OrderSide orderSide) {
        List<Order> orders = orderRepo.findByOrderSide(orderSide);
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

    public List<Order> findOrdersByOrderStatusAndOrderType(OrderStatus orderStatus, OrderType orderType) {
        List<Order> orders = orderRepo.findByOrderStatusAndOrderType(orderStatus, orderType);
        return orders;
    }

    public List<Order> findOrdersByBaseFx(String baseFxCurrencyCode) {
        List<Order> orders = orderRepo.findByBaseFx_CurrencyCode(baseFxCurrencyCode);
        return orders;
    }

    public List<Order> findOrdersByQuoteFx(String quoteFxCurrencyCode) {
        List<Order> orders = orderRepo.findByQuoteFx_CurrencyCode(quoteFxCurrencyCode);
        return orders;
    }


    public List<Order> findOrdersByOrderSideAndUserId(OrderSide orderSide, UUID userId) {
        List<Order> orders = orderRepo.findByOrderSideAndUser_Id(orderSide, userId);
        return orders;
    }

    public List<Order> findOrdersByOrderStatusAndUserId(OrderStatus orderStatus, UUID userId) {
        List<Order> orders = orderRepo.findByOrderStatusAndUser_Id(orderStatus, userId);
        return orders;
    }

    public List<Order> findOrdersByOrderTypeAndUserId(OrderType orderType, UUID userId) {
        List<Order> orders = orderRepo.findByOrderTypeAndUser_Id(orderType, userId);
        return orders;
    }

    public List<Order> findOrdersByOrderStatusAndOrderTypeAndUserId(OrderStatus orderStatus, OrderType orderType, UUID userId) {
        List<Order> orders = orderRepo.findByOrderStatusAndOrderTypeAndUser_Id(orderStatus, orderType, userId);
        return orders;
    }

}
