package com.fdmgroup.forex.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fdmgroup.forex.enums.OrderSide;
import com.fdmgroup.forex.enums.OrderStatus;
import com.fdmgroup.forex.enums.OrderType;
import com.fdmgroup.forex.exceptions.RecordNotFoundException;
import com.fdmgroup.forex.models.Currency;
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
        List<Order> orders = orderRepo.findByOrderType(orderType);
        return orders;
    }

    public List<Order> findOrdersByBaseFx(Currency baseFx) {
        List<Order> orders = orderRepo.findByBaseFx(baseFx);
        return orders;
    }

    public List<Order> findOrdersByQuoteFx(Currency quoteFx) {
        List<Order> orders = orderRepo.findByQuoteFx(quoteFx);
        return orders;
    }

}
