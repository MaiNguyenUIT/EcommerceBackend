package com.example.backend.DTO.request;

import com.example.backend.ENUM.ORDER_STATUS;
import lombok.Data;

@Data
public class UpdateOrderStatusRequest {
    ORDER_STATUS orderStatus;
}
