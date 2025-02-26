package com.example.backend.repository;

import com.example.backend.ENUM.PRODUCT_STATE;
import com.example.backend.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    Product findByname(String productName);
    @Query(value = "{ 'name': ?0, '_id': { $ne: ?1 } }", exists = true)
    boolean existsByNameAndNotId(String name, String id);
    List<Product> findBycategoryName(String category);
    List<Product> findByproductState(PRODUCT_STATE state);

}
