package com.example.backend.serviceImpl;

import com.example.backend.ENUM.ORDER_STATUS;
import com.example.backend.exception.NotFoundException;
import com.example.backend.model.*;
import com.example.backend.repository.CategoryRepository;
import com.example.backend.repository.OrderRepository;
import com.example.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
@Service
public class StatisticalService implements com.example.backend.service.StatisticalService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public double getTotalRevenue(Date startDate, Date endDate) {
        List<Order> orders = orderRepository.findCompletedOrdersInRange(startDate, endDate);
        System.out.println(orders.size());
        return orders.stream()
                .mapToDouble(Order::getOrderAmount)
                .sum();
    }

    @Override
    public int getTotalOrders(Date startDate, Date endDate) {
        List<Order> orders = orderRepository.findCompletedOrdersInRange(startDate, endDate);
        return orders.size();
    }

    @Override
    public List<ProductStatistic> getBestSellingProducts(Date startDate, Date endDate, int limit) {
        List<Order> orders = orderRepository.findCompletedOrdersInRange(startDate, endDate);
        Map<String, Integer> productSales = new HashMap<>();
        for (Order order : orders) {
            for (OrderItem item : order.getOrderItems()) {
                productSales.put(item.getProductId(), productSales.getOrDefault(item.getProductId(), 0) + item.getQuantity());
            }
        }

        return productSales.entrySet().stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .limit(limit)
                .map(entry -> new ProductStatistic(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Double> getRevenueByCategory(Date startDate, Date endDate) {

        List<Order> orders = orderRepository.findCompletedOrdersInRange(startDate, endDate);

        Set<String> productIds = orders.stream()
                .flatMap(order -> order.getOrderItems().stream())
                .map(OrderItem::getProductId)
                .collect(Collectors.toSet());

        // Truy vấn tất cả sản phẩm liên quan một lần
        List<Product> products = productRepository.findAllById(productIds);

        // Tạo Map<ProductId, Product> để tra cứu nhanh
        Map<String, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        // Lấy tất cả categoryId từ danh sách sản phẩm
        Set<String> categoryIds = products.stream()
                .map(Product::getCategoryId)
                .collect(Collectors.toSet());

        // Truy vấn tất cả danh mục một lần
        List<Category> categories = categoryRepository.findAllById(categoryIds);

        // Tạo Map<CategoryId, CategoryTitle> để tra cứu nhanh
        Map<String, String> categoryMap = categories.stream()
                .collect(Collectors.toMap(Category::getId, Category::getTitle));

        // Khởi tạo map chứa doanh thu theo danh mục
        Map<String, Double> revenueByCategory = new HashMap<>();

        // Tính toán doanh thu
        for (Order order : orders) {
            for (OrderItem item : order.getOrderItems()) {
                Product product = productMap.get(item.getProductId());
                if (product == null) {
                    throw new NotFoundException("Product not found with id " + item.getProductId());
                }

                String categoryTitle = categoryMap.get(product.getCategoryId());
                if (categoryTitle == null) {
                    throw new NotFoundException("Category not found with id " + product.getCategoryId());
                }

                // Cộng dồn doanh thu vào danh mục
                revenueByCategory.put(
                        categoryTitle,
                        revenueByCategory.getOrDefault(categoryTitle, 0.0) + product.getPrice()* item.getQuantity()
                );
            }
        }
        return revenueByCategory;
    }

    @Override
    public Map<ORDER_STATUS, Integer> getOrderStatusStatistics(Date startDate, Date endDate) {
        List<Order> orders = orderRepository.findCompletedOrdersInRange(startDate, endDate);
        Map<ORDER_STATUS, Integer> statusCounts = new HashMap<>();
        for (Order order : orders){
            statusCounts.put(order.getOrderStatus(), statusCounts.getOrDefault(order.getOrderStatus(), 0) + 1);
        }
        return statusCounts;
    }

    @Override
    public int getTotalProductsSold(Date startDate, Date endDate) {
        List<Order> orders = orderRepository.findCompletedOrdersInRange(startDate, endDate);
        return orders.stream()
                .flatMap(order -> order.getOrderItems().stream())
                .mapToInt(OrderItem::getQuantity)
                .sum();
    }
}
