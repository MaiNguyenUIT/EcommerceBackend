package com.example.backend.repository;

import com.example.backend.model.Cart;
import com.example.backend.model.GuestCart;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GuestCartRepository extends CrudRepository<GuestCart, String> {
    void deleteBysessionId(String id);
}
