package com.fdmgroup.forex.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

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
            new RecordNotFoundException("Order with id '" + id + "' not found"));
    }

    public List<Order> findOrdersByUserId(UUID userId) {
        List<Order> orders = orderRepo.findByPortfolio_User_Id(userId);
        return orders;
    }

    public List<Order> findOrdersByUserIdAndOrderStatus(UUID userId, OrderStatus orderStatus) {
        List<Order> orders = orderRepo.findByPortfolio_User_IdAndOrderStatus(userId, orderStatus);
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

    public List<Order> findOrdersByPortfolioId(UUID portfolioId) {
        List<Order> orders = orderRepo.findByPortfolio_Id(portfolioId);
        return orders;
    }

    public List<Order> findOrdersByPortfolioIdAndOrderStatus(UUID portfolioId, OrderStatus orderStatus) {
        List<Order> orders = orderRepo.findByPortfolio_IdAndOrderStatus(portfolioId, orderStatus);
        return orders;
    }

}
