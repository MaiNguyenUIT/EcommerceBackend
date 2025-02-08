package com.example.backend.model;

import lombok.Data;

@Data
public class ShippingAddress {
    private String name;
    private String phone;
    private String province;
    private String district;
    private String street;
}
