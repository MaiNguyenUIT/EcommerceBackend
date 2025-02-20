package com.example.backend.repository;

import com.example.backend.model.GuestCart;
import com.example.backend.model.GuestWishList;
import org.springframework.data.repository.CrudRepository;

public interface GuestWishListRepository extends CrudRepository<GuestWishList, String> {
}
