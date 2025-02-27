package com.example.backend.service;

import com.example.backend.model.ClickItem;

import java.util.List;

public interface ClickItemService {
    ClickItem addItemToClickList(String identified, String productId);

    ClickItem getAllClickItem(String identified);
}
