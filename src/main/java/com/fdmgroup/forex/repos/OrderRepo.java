package com.fdmgroup.forex.repos;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.forex.enums.OrderSide;
import com.fdmgroup.forex.enums.OrderStatus;
import com.fdmgroup.forex.enums.OrderType;
import com.fdmgroup.forex.models.Currency;
import com.fdmgroup.forex.models.Order;

@Repository
public interface OrderRepo extends JpaRepository<Order, UUID> {

    List<Order> findByOrderSide(OrderSide orderSide);
    List<Order> findByOrderStatus(OrderStatus orderStatus);
    List<Order> findByOrderType(OrderType orderType);
    List<Order> findByOrderStatusAndOrderType(OrderStatus orderStatus, OrderType orderType);

    List<Order> findByBaseFx(Currency baseFx);
    List<Order> findByQuoteFx(Currency quoteFx);
    List<Order> findByBaseFxAndQuoteFx(Currency baseFx, Currency quoteFx);

}
