package com.example.backend.controller.admin;

import com.example.backend.ApiResult.ApiResult;
import com.example.backend.DTO.request.UpdateOrderStatusRequest;
import com.example.backend.model.Order;
import com.example.backend.service.AdminOrderService;
import com.example.backend.service.OrderService;
import com.example.backend.service.UserService;
import com.example.backend.utils.MapResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.BooleanOperators;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/order")
public class AdminOrderController {
    @Autowired
    private UserService userService;
    @Autowired
    private AdminOrderService adminOrderService;
    @Autowired
    private MapResult mapResult;
    @PutMapping("/{id}")
    public ResponseEntity<ApiResult<Order>> updateOrderStatus(@RequestHeader("Authorization") String jwt, @PathVariable String id, @RequestBody UpdateOrderStatusRequest request) throws Exception{
        ApiResult<Order> apiResult = mapResult.map(adminOrderService.updateOrderStatus(id, request.getOrderStatus()), "Update order status successfully");
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<ApiResult<List<Order>>> getAllOrder(@RequestHeader("Authorization") String jwt) throws Exception{
        ApiResult<List<Order>> apiResult = mapResult.map(adminOrderService.getAllOrder(), "Update order status successfully");
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }
}
