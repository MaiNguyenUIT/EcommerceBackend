package com.example.backend.controller.user;

import com.example.backend.ApiResult.ApiResult;
import com.example.backend.DTO.OrderDTO;
import com.example.backend.model.Order;
import com.example.backend.model.User;
import com.example.backend.service.OrderService;
import com.example.backend.service.UserService;
import com.example.backend.utils.MapResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private MapResult mapResult;
    @PostMapping()
    public ResponseEntity<ApiResult<Order>> createOrder(@RequestHeader("Authorization") String jwt, @RequestBody OrderDTO orderDTO) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        ApiResult<Order> apiResult = mapResult.map(orderService.createOrder(user.getId(), orderDTO), "Create order successfully");
        return new ResponseEntity<>(apiResult, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<ApiResult<List<Order>>> getUserOrder(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        ApiResult<List<Order>> apiResult = mapResult.map(orderService.getOrderByUserId(user.getId()), "Get user order successfully");
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<List<Order>>> getOrderById(@RequestHeader("Authorization") String jwt, @PathVariable String id) throws Exception {
        ApiResult<List<Order>> apiResult = mapResult.map(orderService.getOrderById(id), "Get detail order successfully");
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResult<Order>> cancelOrder(@RequestHeader("Authorization") String jwt, @PathVariable String id) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        ApiResult<Order> apiResult = mapResult.map(orderService.cancelOrder(id, user.getId()), "Cancel order successfully");
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }
}
