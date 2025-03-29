package com.example.backend.controller.admin;

import com.example.backend.ENUM.ORDER_STATUS;
import com.example.backend.model.ProductStatistic;
import com.example.backend.service.StatisticalService;
import com.example.backend.service.UserService;
import com.example.backend.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

@RestController
@RequestMapping("api/admin/statistical")
public class AdminStatisticalController {
    @Autowired
    private StatisticalService statisticalService;
    @Autowired
    private UserService userService;

    @GetMapping("/revenue")
    public ResponseEntity<ApiResponse<Double>> getTotalRevenue(@RequestHeader("Authorization") String jwt,
                                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
                                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate){
        ApiResponse<Double> apiResponse = ApiResponse.<Double>builder()
                .data(statisticalService.getTotalRevenue(startDate, endDate))
                .message("Get total revenue successfully")
                .status(200)
                .build();
        return ResponseEntity.ok(apiResponse);
    }
    @GetMapping("/order")
    public ResponseEntity<ApiResponse<Integer>> getTotalOrder(@RequestHeader("Authorization") String jwt,
                                                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
                                                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate){
        ApiResponse<Integer> apiResponse = ApiResponse.<Integer>builder()
                .data(statisticalService.getTotalOrders(startDate, endDate))
                .message("Get total order successfully")
                .status(200)
                .build();
        return ResponseEntity.ok(apiResponse);
    }
    @GetMapping("/bestSelling")
    public ResponseEntity<ApiResponse<List<ProductStatistic>>> getBestSelling(@RequestHeader("Authorization") String jwt,
                                                                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
                                                                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate){

        ApiResponse<List<ProductStatistic>> apiResponse = ApiResponse.<List<ProductStatistic>>builder()
                .data(statisticalService.getBestSellingProducts(startDate, endDate, 10))
                .message("Get best selling products successfully")
                .status(200)
                .build();
        return ResponseEntity.ok(apiResponse);
    }
    @GetMapping("/orderStatus-statistic")
    public ResponseEntity<ApiResponse<Map<ORDER_STATUS, Integer>>> getOrderStatusStatistic(@RequestHeader("Authorization") String jwt,
                                                                                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
                                                                                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate){
        ApiResponse<Map<ORDER_STATUS, Integer>> apiResponse = ApiResponse.<Map<ORDER_STATUS, Integer>>builder()
                .data(statisticalService.getOrderStatusStatistics(startDate, endDate))
                .message("Get order status statistic successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }
    @GetMapping("/productSold")
    public ResponseEntity<ApiResponse<Integer>> getProductSold(@RequestHeader("Authorization") String jwt,
                                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
                                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate){

        ApiResponse<Integer> apiResponse = ApiResponse.<Integer>builder()
                .data(statisticalService.getTotalProductsSold(startDate, endDate))
                .message("Get product sold successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }
    @GetMapping("/revenueCategory")
    public ResponseEntity<ApiResponse<Map<String, Double>>> getRevenueCategory(@RequestHeader("Authorization") String jwt,
                                                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
                                                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate){

        ApiResponse<Map<String, Double>> apiResponse = ApiResponse.<Map<String, Double>>builder()
                .data(statisticalService.getRevenueByCategory(startDate, endDate))
                .message("Get revenue category successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
