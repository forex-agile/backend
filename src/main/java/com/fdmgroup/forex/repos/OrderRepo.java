package com.fdmgroup.forex.repos;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.forex.enums.OrderSide;
import com.fdmgroup.forex.enums.OrderStatus;
import com.fdmgroup.forex.enums.OrderType;
import com.fdmgroup.forex.models.Order;

@Repository
public interface OrderRepo extends JpaRepository<Order, UUID> {

    List<Order> findByOrderSide(OrderSide orderSide);
    List<Order> findByOrderStatus(OrderStatus orderStatus);
    List<Order> findByOrderType(OrderType orderType);
    List<Order> findByOrderStatusAndOrderType(OrderStatus orderStatus, OrderType orderType);

    List<Order> findByBaseFx_CurrencyCode(String baseFxCurrencyCode);
    List<Order> findByQuoteFx_CurrencyCode(String quoteFxCurrencyCode);
    List<Order> findByBaseFx_CurrencyCodeAndQuoteFx_CurrencyCode(String baseFxCurrencyCode, String quoteFxCurrencyCode);

    List<Order> findByOrderSideAndUser_Id(OrderSide orderSide, UUID userId);
    List<Order> findByOrderStatusAndUser_Id(OrderStatus orderStatus, UUID userId);
    List<Order> findByOrderTypeAndUser_Id(OrderType orderType, UUID userId);
    List<Order> findByOrderStatusAndOrderTypeAndUser_Id(OrderStatus orderStatus, OrderType orderType, UUID userId);

    List<Order> findByBaseFx_CurrencyCodeAndUser_Id(String baseFxCurrencyCode, UUID userId);
    List<Order> findByQuoteFx_CurrencyCodeAndUser_Id(String quoteFxCurrencyCode, UUID userId);
    List<Order> findByBaseFx_CurrencyCodeAndQuoteFx_CurrencyCodeAndUser_Id(String baseFxCurrencyCode, String quoteFxCurrencyCode, UUID userId);

}
