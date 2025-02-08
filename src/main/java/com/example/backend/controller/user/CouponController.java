package com.example.backend.controller.user;

import com.example.backend.ApiResult.ApiResult;
import com.example.backend.DTO.CategoryDTO;
import com.example.backend.DTO.CouponDTO;
import com.example.backend.model.User;
import com.example.backend.serviceImpl.CouponService;
import com.example.backend.serviceImpl.UserService;
import com.example.backend.utils.MapResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coupon")
public class CouponController {
    @Autowired
    private UserService userService;
    @Autowired
    private CouponService couponService;
    @Autowired
    private MapResult mapResult;
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<CouponDTO>> getCoupon(@PathVariable String id) throws Exception {
        CouponDTO coupon = couponService.getCoupon(id);
        ApiResult<CouponDTO> apiResult = mapResult.map(coupon, "Get coupon successfully");
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }
}
