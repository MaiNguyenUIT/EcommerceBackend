package com.example.backend.service;

import com.example.backend.ENUM.ORDER_STATUS;
import com.example.backend.model.Order;

import java.util.List;

public interface AdminOrderService {
    List<Order> getAllOrder();
    Order updateOrderStatus(String orderId, ORDER_STATUS orderStatus);
}
