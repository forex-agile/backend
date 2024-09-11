package com.fdmgroup.forex.repos;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fdmgroup.forex.enums.OrderStatus;
import com.fdmgroup.forex.enums.OrderType;
import com.fdmgroup.forex.models.Currency;
import com.fdmgroup.forex.models.Order;

@Repository
public interface OrderRepo extends JpaRepository<Order, UUID> {

    List<Order> findByPortfolio_User_Id(UUID id);
    List<Order> findByPortfolio_User_IdAndOrderStatus(UUID userId, OrderStatus orderStatus);
    List<Order> findByOrderStatus(OrderStatus orderStatus);
    List<Order> findByOrderType(OrderType orderType);
    @Query("SELECT o FROM Order o WHERE o.baseFx = :quoteFx AND o.quoteFx = :baseFx AND o.orderStatus = :status")
    List<Order> findActiveOrdersByFx(@Param("quoteFx") Currency quoteFx, 
                                     @Param("baseFx") Currency baseFx, 
                                     @Param("status") OrderStatus status);
}
