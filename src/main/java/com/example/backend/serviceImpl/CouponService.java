package com.example.backend.serviceImpl;

import com.example.backend.DTO.CouponDTO;
import com.example.backend.exception.BadRequestException;
import com.example.backend.exception.NotFoundException;
import com.example.backend.mapper.CouponMapper;
import com.example.backend.model.Coupon;
import com.example.backend.repository.CouponRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class CouponService implements com.example.backend.service.CouponService {
    @Autowired
    private CouponRepository couponRepository;
    @Override
    public CouponDTO createCoupon(CouponDTO couponDTO) {
        if(couponDTO.getExpiry().isBefore(LocalDateTime.now())){
            throw new BadRequestException("Coupon expiry can't before today");
        }
        if(couponRepository.findByname(couponDTO.getName()) != null){
            throw new BadRequestException("This coupon name is already exist");
        }
        if(couponDTO.getDiscount() > 100){
            throw new BadRequestException("Discount must be at least 100");
        }
        Coupon coupon = CouponMapper.INSTANCE.toEntity(couponDTO);
        couponRepository.save(coupon);
        return couponDTO;
    }

    @Override
    public CouponDTO updateCoupon(CouponDTO couponDTO, String couponId) throws Exception {
        Coupon existCoupon =  couponRepository.findById(couponId)
                .orElseThrow(() -> new NotFoundException("Coupon not found with ID: " + couponId));
        if(couponDTO.getExpiry().isBefore(LocalDateTime.now())){
            throw new BadRequestException("Coupon expiry can't before today");
        }
        if(couponRepository.findByname(couponDTO.getName()) != null){
            throw new BadRequestException("This coupon name is already exist");
        }
        if(couponDTO.getDiscount() > 100){
            throw new BadRequestException("Discount must be at least 100");
        }
        existCoupon.setName(couponDTO.getName());
        existCoupon.setExpiry(couponDTO.getExpiry());
        existCoupon.setDiscount(couponDTO.getDiscount());
        couponRepository.save(existCoupon);
        // Lưu vào DB
        return couponDTO;
    }

    @Override
    public void deleteCoupon(String couponId) {
        couponRepository.deleteById(couponId);
    }

    @Override
    public CouponDTO getCoupon(String couponId) {
        return CouponMapper.INSTANCE.toDTO(
                couponRepository.findById(couponId)
                        .orElseThrow(() -> new NotFoundException("Coupon not found with ID: " + couponId))
        );
    }

    @Override
    public List<Coupon> getAllCoupon() {
        return couponRepository.findAll();
    }
}
