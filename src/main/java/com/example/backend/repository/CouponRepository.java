package com.example.backend.repository;

import com.example.backend.model.Coupon;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CouponRepository extends MongoRepository<Coupon, String> {
    Coupon findByname(String couponName);
}
