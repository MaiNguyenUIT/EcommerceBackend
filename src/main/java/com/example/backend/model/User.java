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


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public USER_ROLE getRole() {
        return role;
    }

    public void setRole(USER_ROLE role) {
        this.role = role;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
    public String getWishListId() {
        return wishListId;
    }

    public void setWishListId(String wishListId) {
        this.wishListId = wishListId;
    }

    private String wishListId;
}
