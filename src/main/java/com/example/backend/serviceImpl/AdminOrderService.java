package com.example.backend.serviceImpl;

import com.example.backend.ENUM.ORDER_STATUS;
import com.example.backend.exception.NotFoundException;
import com.example.backend.model.Order;
import com.example.backend.model.User;
import com.example.backend.repository.OrderRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminOrderService implements com.example.backend.service.AdminOrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository userRepository;
    @Override
    public List<Order> getAllOrder() {
        return orderRepository.findAll();
    }

    @Override
    public Order updateOrderStatus(String orderId, ORDER_STATUS orderStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));
        order.setOrderStatus(orderStatus);
        User user = userRepository.findById(order.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found with id: " + order.getUserId()));
        emailService.sendOrderStatusUpdateEmail(user.getEmail(), order);
        return orderRepository.save(order);
    }
}
