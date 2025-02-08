package com.example.backend.DTO;

import com.example.backend.ENUM.USER_ROLE;
import com.example.backend.model.Address;
import lombok.Data;

@Data
public class UserInforDTO {
    private String phone;
    private String photo;
    private Address address;
}
