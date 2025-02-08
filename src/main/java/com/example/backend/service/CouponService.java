package com.example.backend.service;

import com.example.backend.DTO.CouponDTO;
import com.example.backend.model.Coupon;

import java.util.List;

public interface CouponService {
    CouponDTO createCoupon(CouponDTO couponDTO);
    CouponDTO updateCoupon(CouponDTO couponDTO, String couponId) throws Exception;
    void deleteCoupon(String couponId);
    CouponDTO getCoupon(String couponId);
    List<Coupon> getAllCoupon();
}
