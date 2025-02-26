package com.example.backend.serviceImpl;

import com.example.backend.DTO.OrderDTO;
import com.example.backend.ENUM.ORDER_STATUS;
import com.example.backend.ENUM.PRODUCT_STATE;
import com.example.backend.exception.BadRequestException;
import com.example.backend.exception.NotFoundException;
import com.example.backend.mapper.OrderMapper;
import com.example.backend.model.*;
import com.example.backend.repository.OrderRepository;
import com.example.backend.repository.ProductRepository;
import com.example.backend.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class OrderService implements com.example.backend.service.OrderService {
    @Autowired
    private CartService cartService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private EmailService emailService;
    @Override
    public Order createOrder(String userId, OrderDTO orderDTO) {
        Cart cart = cartService.getCartByUserId(userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
        if (cart == null || cart.getCartItems().isEmpty()) {
            throw new BadRequestException("Cart is empty with user id: " + userId);
        }

        List<RLock> locks = new ArrayList<>();
        try {
            // Tạo danh sách khóa cho tất cả sản phẩm trong giỏ hàng
            for (CartItem item : cart.getCartItems()) {
                RLock lock = redissonClient.getLock("lock:product:" + item.getProductId());
                if (!lock.tryLock(10, 10, TimeUnit.SECONDS)) {
                    throw new RuntimeException("Could not acquire lock for product: " + item.getProductId());
                }
                locks.add(lock);
            }

            // Kiểm tra tồn kho và giảm stock từng sản phẩm
            for (CartItem item : cart.getCartItems()) {
                reduceStock(item.getProductId(), item.getQuantity());
            }

            // Tạo đơn hàng
            Order order = OrderMapper.INSTANCE.toEntity(orderDTO);
            order.setUserId(userId);
            for (CartItem i : cart.getCartItems()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setQuantity(i.getQuantity());
                orderItem.setProductId(i.getProductId());
                orderItem.setProductName(i.getProductName());
                order.getOrderItems().add(orderItem);
            }
            order.setOrderAmount(cart.getTotalPrice());

            cartService.clearCart(userId);
            Order savedOrder = orderRepository.save(order);
            emailService.sendOrderConfirmationEmail(user.getEmail(), savedOrder);

            return savedOrder;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while acquiring lock", e);
        } catch (MessagingException e) {
            throw new RuntimeException("Cannot send email...");
        } finally {
            for (RLock lock : locks) {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        }
    }
    public void reduceStock(String productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException("Product not found with id: " + productId));

        if (product.getProductState().equals(PRODUCT_STATE.HIDDEN)) {
            throw new BadRequestException(product.getName() + " is out of stock");
        }

        if (product.getQuantity() < quantity) {
            throw new BadRequestException(product.getName() + " is not enough in stock");
        }

        int afterQuantity = product.getQuantity() - quantity;
        product.setQuantity(afterQuantity);
        product.setSold(product.getSold() + quantity);

        if (afterQuantity == 0) {
            product.setProductState(PRODUCT_STATE.HIDDEN);
        }

        productRepository.save(product);
    }

    @Override
    public Order getOrderById(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));
    }

    @Override
    public List<Order> getOrderByUserId(String userId) {
        return orderRepository.findByuserId(userId);
    }

    @Override
    public Order cancelOrder(String orderId, String userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));
        if (!order.getUserId().equals(userId)) {
            throw new BadRequestException("You are not authorized to cancel this order: " + orderId);
        }
        if(order.getOrderStatus() != ORDER_STATUS.PENDING){
            throw new BadRequestException("Cannot cancel this order: " + orderId);
        } else {
            order.setOrderStatus(ORDER_STATUS.CANCELLED);
            return orderRepository.save(order);
        }
    }
}
