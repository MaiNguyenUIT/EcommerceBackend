package com.example.backend.repository;

import com.example.backend.model.ClickItem;
import org.springframework.data.repository.CrudRepository;

public interface ClickItemRepository extends CrudRepository<ClickItem, String> {
}
