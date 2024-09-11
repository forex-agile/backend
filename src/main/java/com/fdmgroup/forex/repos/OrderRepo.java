package com.fdmgroup.forex.repos;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.forex.enums.OrderStatus;
import com.fdmgroup.forex.enums.OrderType;
import com.fdmgroup.forex.models.Order;

@Repository
public interface OrderRepo extends JpaRepository<Order, UUID> {

    List<Order> findByPortfolio_User_Id(UUID id);
    List<Order> findByPortfolio_User_IdAndOrderStatus(UUID userId, OrderStatus orderStatus);
    List<Order> findByOrderStatus(OrderStatus orderStatus);
    List<Order> findByOrderType(OrderType orderType);
    List<Order> findByPortfolio_IdAndOrderStatus(UUID id, OrderStatus orderStatus);
    List<Order> findByPortfolio_Id(UUID id);

}
