package com.example.backend.serviceImpl;

import com.example.backend.DTO.CartItemDTO;
import com.example.backend.DTO.WishListItemDTO;
import com.example.backend.exception.BadRequestException;
import com.example.backend.exception.NotFoundException;
import com.example.backend.mapper.WishListItemMapper;
import com.example.backend.model.GuestWishList;
import com.example.backend.model.Product;
import com.example.backend.repository.GuestWishListRepository;
import com.example.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GuestWishListService implements com.example.backend.service.GuestWishListService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private GuestCartService guestCartService;
    @Autowired
    private GuestWishListRepository guestWishListRepository;
    @Override
    public GuestWishList addToWishList(String sessionId, WishListItemDTO wishListItemDTO) {
        GuestWishList wishList = guestWishListRepository.findById(sessionId)
                .orElse(null);
        if(wishList == null){
            GuestWishList newWishList = new GuestWishList();
            Product product = productRepository.findById(wishListItemDTO.getProductId())
                    .orElseThrow(() -> new NotFoundException("Product not found"));
            newWishList.getWishListItems().add(WishListItemMapper.INSTANCE.toEntity(wishListItemDTO));
            newWishList.setSessionId(sessionId);
            return guestWishListRepository.save(newWishList);
        } else {
            wishList.getWishListItems().stream()
                    .filter(item -> item.getProductId().equals(wishListItemDTO.getProductId()))
                    .findFirst()
                    .ifPresentOrElse(
                            existingItem -> { throw new BadRequestException("This product is already in wish list"); },
                            () -> wishList.getWishListItems().add(WishListItemMapper.INSTANCE.toEntity(wishListItemDTO))
                    );
            return guestWishListRepository.save(wishList);
        }
    }

    @Override
    public GuestWishList removeFromWishList(String sessionId, String productId) {
        GuestWishList wishList = guestWishListRepository.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("Wish list is empty with session id: " + sessionId));
        if(wishList == null){
            throw new BadRequestException("Wish list is empty with id: " + sessionId);
        } else {
            boolean removed = wishList.getWishListItems().removeIf(item -> item.getProductId().equals(productId));
            if (!removed) {
                throw new NotFoundException("Product with id " + productId + " not found in wishlist");
            }

            return guestWishListRepository.save(wishList);
        }
    }

    @Override
    public GuestWishList getWishList(String sessionId) {
        return guestWishListRepository.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("Wish list not found"));
    }

    @Override
    public GuestWishList addWishListItemToCart(String sessionId, String productId) {
        GuestWishList wishList = guestWishListRepository.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("Wish list not found"));
        if(wishList == null){
            throw new BadRequestException("Wish list is empty with id: " + sessionId);
        } else {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new NotFoundException("Product with id: " + productId + " not found in wish list"));
            CartItemDTO cartItemDTO = new CartItemDTO();
            cartItemDTO.setProductId(productId);
            cartItemDTO.setProductName(product.getName());
            guestCartService.addItemToGuestCart(sessionId, cartItemDTO);
            wishList.getWishListItems().removeIf(item -> item.getProductId().equals(productId));

            return guestWishListRepository.save(wishList);
        }
    }

    @Override
    public void clearWishList(String sessionId) {
        guestWishListRepository.deleteById(sessionId);
    }
}
