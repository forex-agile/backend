package com.fdmgroup.forex.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fdmgroup.forex.enums.OrderStatus;
import com.fdmgroup.forex.enums.OrderType;
import com.fdmgroup.forex.models.Order;
import com.fdmgroup.forex.services.OrderService;

@RestController
@RequestMapping("api/v1/order")
@CrossOrigin(origins = "*")
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.findAllOrders();
        if (orders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/status/{orderStatus}")
    public ResponseEntity<List<Order>> getOrdersByOrderStatus(@PathVariable OrderStatus orderStatus) {
        List<Order> orders = orderService.findOrdersByOrderStatus(orderStatus);
        if (orders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/type/{orderType}")
    public ResponseEntity<List<Order>> getOrdersByOrderType(@PathVariable OrderType orderType) {
        List<Order> orders = orderService.findOrdersByOrderType(orderType);
        if (orders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<Order>> getOrdersByUserIdAndOptionalOrderStatus(
        @PathVariable UUID id, 
        @RequestParam OrderStatus orderStatus
    ) {
        List<Order> orders;
        if (orderStatus == null) {
            orders = orderService.findOrdersByUserId(id);
        } else {
            orders = orderService.findOrdersByUserIdAndOrderStatus(id, orderStatus);
        }
        if (orders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable UUID id) {
        Order order = orderService.findOrderById(id);
        return ResponseEntity.ok(order);
    }

}
