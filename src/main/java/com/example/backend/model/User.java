package com.example.backend.model;

import com.example.backend.ENUM.USER_ROLE;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(
        "users"
)
@Data
public class User {
    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String photo = "https://www.shutterstock.com/image-vector/default-avatar-profile-icon-social-600nw-1677509740.jpg";
    private USER_ROLE role = USER_ROLE.ROLE_CUSTOMER;
    private Address address;
}
