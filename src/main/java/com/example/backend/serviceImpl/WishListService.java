package com.example.backend.serviceImpl;

import com.example.backend.DTO.CartItemDTO;
import com.example.backend.DTO.WishListItemDTO;
import com.example.backend.exception.BadRequestException;
import com.example.backend.exception.NotFoundException;
import com.example.backend.mapper.WishListItemMapper;
import com.example.backend.model.*;
import com.example.backend.repository.GuestWishListRepository;
import com.example.backend.repository.ProductRepository;
import com.example.backend.repository.WishListRepository;
import com.example.backend.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WishListService implements com.example.backend.service.WishListService {
    @Autowired
    private UserService userService;
    @Autowired
    private WishListRepository wishListRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartService cartService;
    @Autowired
    private GuestWishListRepository guestWishListRepository;
    @Override
    public WishList addToWishList(String userId, WishListItemDTO wishListItemDTO) {
        WishList wishList = wishListRepository.findByuserId(userId);
        if(wishList == null){
            WishList newWishList = new WishList();
            Product product = productRepository.findById(wishListItemDTO.getProductId())
                    .orElseThrow(() -> new NotFoundException("Product not found"));
            newWishList.getWishListItems().add(WishListItemMapper.INSTANCE.toEntity(wishListItemDTO));
            newWishList.setUserId(userId);
            return wishListRepository.save(newWishList);
        } else {
            wishList.getWishListItems().stream()
                    .filter(item -> item.getProductId().equals(wishListItemDTO.getProductId()))
                    .findFirst()
                    .ifPresentOrElse(
                            existingItem -> { throw new BadRequestException("This product is already in wish list"); },
                            () -> wishList.getWishListItems().add(WishListItemMapper.INSTANCE.toEntity(wishListItemDTO))
                    );
            return wishListRepository.save(wishList);
        }
    }

    @Override
    public WishList removeFromWishList(String userId, String productId) {
        WishList wishList = wishListRepository.findByuserId(userId);
        if(wishList == null){
            throw new BadRequestException("Wish list is empty with id: " + userId);
        } else {
            boolean removed = wishList.getWishListItems().removeIf(item -> item.getProductId().equals(productId));
            if (!removed) {
                throw new NotFoundException("Product with id " + productId + " not found in wishlist");
            }

            return wishListRepository.save(wishList);
        }
    }

    @Override
    public WishList getUserWishList(String userId) {
        WishList wishList = wishListRepository.findByuserId(userId);
        if(wishList == null){
            throw new NotFoundException("Wish list is empty with userid: " + userId);
        }
        return wishList;
    }

    @Override
    public WishList addWishListItemToCart(String userId, String productId) {
        WishList wishList = wishListRepository.findByuserId(userId);
        if(wishList == null){
            throw new BadRequestException("Wish list is empty with id: " + userId);
        } else {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new NotFoundException("Product with id: " + productId + " not found in wish list"));
            CartItemDTO cartItemDTO = new CartItemDTO();
            cartItemDTO.setProductId(productId);
            cartService.addItemToUserCart(userId, cartItemDTO);
            wishList.getWishListItems().removeIf(item -> item.getProductId().equals(productId));

            return wishListRepository.save(wishList);
        }
    }

    @Override
    public void clearWishList(String userId) {
        wishListRepository.deleteByuserId(userId);
    }

    @Override
    public WishList mergeGuestToUser(String sessionId, String userId) {
        GuestWishList guestWishList = guestWishListRepository.findById(sessionId)
                .orElseThrow(null);

        WishList wishList = wishListRepository.findByuserId(userId);
        if(wishList == null){
            wishList.setWishListItems(guestWishList.getWishListItems());
            wishList.setUserId(userId);
            guestWishListRepository.deleteById(sessionId);
        } else {
            for(WishListItem wishListItem : guestWishList.getWishListItems())
            {
                wishList.getWishListItems().stream()
                        .filter(item -> item.getProductId().equals(wishListItem.getProductId()))
                        .findFirst()
                        .ifPresentOrElse(
                                existingItem -> {},
                                () -> wishList.getWishListItems().add(wishListItem)
                        );
            }
        }
        return wishListRepository.save(wishList);
    }
}
