package com.example.backend.repository;

import com.example.backend.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findByuserId(String userId);
    @Query("{'orderDateTime': { $gte: ?0, $lte: ?1 }, 'orderStatus': 'SUCCESS' }")
    List<Order> findCompletedOrdersInRange(Date startDate, Date endDate);
}
