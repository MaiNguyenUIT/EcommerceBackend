package com.example.backend.serviceImpl;

import com.example.backend.exception.BadRequestException;
import com.example.backend.model.ClickItem;
import com.example.backend.repository.ClickItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClickItemService implements com.example.backend.service.ClickItemService {
    @Autowired
    private UserService userService;
    @Autowired
    private ClickItemRepository clickItemRepository;

    @Override
    public ClickItem addItemToClickList(String identified, String productId) {
        ClickItem clickItem = clickItemRepository.findById(identified)
                .orElseGet(() -> {
                    ClickItem newClickItem = new ClickItem();
                    newClickItem.setIdentified(identified);
                    newClickItem.getProductId().add(productId);
                    return newClickItem;
                });

        if (!clickItem.getProductId().contains(productId)) {
            clickItem.getProductId().add(productId);
        }

        return clickItemRepository.save(clickItem);
    }

    @Override
    public ClickItem getAllClickItem(String identified) {
        return clickItemRepository.findById(identified)
                .orElseThrow(() -> new BadRequestException("clickItem is empty"));
    }
}
