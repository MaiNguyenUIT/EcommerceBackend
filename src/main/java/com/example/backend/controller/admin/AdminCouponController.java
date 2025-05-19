package com.example.backend.controller.admin;

import com.example.backend.ApiResult.ApiResult;
import com.example.backend.DTO.CouponDTO;
import com.example.backend.model.Coupon;
import com.example.backend.model.User;
import com.example.backend.serviceImpl.CouponService;
import com.example.backend.serviceImpl.UserService;
import com.example.backend.utils.MapResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin/coupon")
public class AdminCouponController {
    @Autowired
    private CouponService couponService;
    @Autowired
    private MapResult mapResult;
    @PostMapping
    public ResponseEntity<ApiResult<CouponDTO>> createCoupon(@RequestBody CouponDTO couponDTO) throws Exception {

        CouponDTO createCouponDTO = couponService.createCoupon(couponDTO);
        ApiResult<CouponDTO> apiResult = mapResult.map(createCouponDTO, "Create coupon successfully");
        return new ResponseEntity<>(apiResult, HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResult<CouponDTO>> updateCoupon(@RequestBody CouponDTO couponDTO, @PathVariable String id) throws Exception {
        CouponDTO updateCouponDTO = couponService.updateCoupon(couponDTO, id);
        ApiResult<CouponDTO> apiResult = mapResult.map(updateCouponDTO, "Update coupon successfully");
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCoupon(@PathVariable String id) throws Exception {
        couponService.deleteCoupon(id);
        return new ResponseEntity<>("Update coupon successfully", HttpStatus.OK);
    }
    @GetMapping("")
    public ResponseEntity<List<Coupon>> getAllCoupon() throws Exception {
        return new ResponseEntity<>(couponService.getAllCoupon(), HttpStatus.OK);
    }
}
