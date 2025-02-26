package com.example.backend.service;

import com.example.backend.DTO.OrderDTO;
import com.example.backend.model.Order;

import java.util.List;

public interface OrderService {
    Order createOrder(String userId, OrderDTO orderDTO);
    Order getOrderById(String orderId);
    List<Order> getOrderByUserId(String userId);
    Order cancelOrder(String orderId, String userId);
    //Order processPayment(String orderId, )
}
