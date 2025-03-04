package com.example.backend.serviceImpl;

import com.example.backend.DTO.CartItemDTO;
import com.example.backend.exception.NotFoundException;
import com.example.backend.mapper.CartItemMapper;
import com.example.backend.model.*;
import com.example.backend.repository.CartRepository;
import com.example.backend.repository.GuestCartRepository;
import com.example.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService implements com.example.backend.service.CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private GuestCartRepository guestCartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Override
    public Cart addItemToUserCart(String userId, CartItemDTO cartItemDTO) {
        Cart userCart = cartRepository.findByuserId(userId).orElse(null);
        if(userCart == null){
            Cart cart = new Cart();
            Product product = productRepository.findById(cartItemDTO.getProductId())
                    .orElseThrow(() -> new NotFoundException("Product not found"));
            cart.getCartItems().add(CartItemMapper.INSTANCE.toEntity(cartItemDTO));
            cart.setTotalItem(1);
            cart.setTotalPrice(product.getPrice());
            cart.setUserId(userId);
            return cartRepository.save(cart);
        } else{
            userCart.getCartItems().stream()
                    .filter(item -> item.getProductId().equals(cartItemDTO.getProductId()))
                    .findFirst()
                    .ifPresentOrElse(
                            existingItem -> existingItem.setQuantity(existingItem.getQuantity() + 1),
                            () -> userCart.getCartItems().add(CartItemMapper.INSTANCE.toEntity(cartItemDTO))
                    );

            return cartRepository.save(updateCartInfor(userCart));
        }
    }
    @Override
    public Cart getCartByUserId(String userId) {
        return cartRepository.findByuserId(userId)
                .orElseThrow(() -> new NotFoundException("Cart not found"));
    }

    @Override
    public Cart updateItemQuantity(String userId, String productId, int quantity) {
        Cart cart = cartRepository.findByuserId(userId)
                .orElseThrow(() -> new NotFoundException("Cart not found"));
        Optional<CartItem> cartItemOptional = cart.getCartItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (cartItemOptional.isPresent()) {
            CartItem cartItem = cartItemOptional.get();

            if (quantity > 0) {
                cartItem.setQuantity(quantity); // Update quantity
            } else {
                cart.getCartItems().remove(cartItem); // Remove item if quantity is zero
            }
        } else {
            throw new NotFoundException("Product not found in cart");
        }

        return cartRepository.save(updateCartInfor(cart));
    }

    @Override
    public Cart deleteItemFromCart(String userId, String productId) {
        Cart cart = cartRepository.findByuserId(userId)
                .orElseThrow(() -> new NotFoundException("Cart not found"));
        // Find and remove the cart item by productId
        cart.getCartItems().removeIf(item -> item.getProductId().equals(productId));
        // Save the updated cart
        return cartRepository.save(updateCartInfor(cart));
    }

    @Override
    public void clearCart(String userId) {
        cartRepository.deleteByuserId(userId);
    }

    @Override
    public Cart mergeGuestCartToUserCart(String sessionId, String userId) {
        // Lấy guestCart, nếu không có thì trả về userCart hiện tại
        GuestCart guestCart = guestCartRepository.findById(sessionId).orElse(null);
        Cart userCart = cartRepository.findByuserId(userId).orElse(new Cart());
        userCart.setUserId(userId); // Đảm bảo set userId cho userCart

        if (guestCart != null) {
            // Merge từng CartItem từ guestCart vào userCart
            for (CartItem guestItem : guestCart.getCartItems()) {
                userCart.getCartItems().stream()
                        .filter(userItem -> userItem.getProductId().equals(guestItem.getProductId()))
                        .findFirst()
                        .ifPresentOrElse(
                                existingItem -> {
                                    // Cập nhật số lượng cho mặt hàng đã tồn tại
                                    existingItem.setQuantity(existingItem.getQuantity() + guestItem.getQuantity());
                                },
                                () -> {
                                    // Thêm mới nếu chưa có mặt hàng đó trong userCart
                                    userCart.getCartItems().add(guestItem);
                                }
                        );
            }
            // Sau khi merge, xoá guestCart
            guestCartRepository.deleteById(sessionId);
        }
        // Lưu và trả về giỏ hàng của user
        return cartRepository.save(updateCartInfor(userCart));
    }


    @Override
    public Cart increaseCartItem(String userId, String productId) {
        Cart cart = cartRepository.findByuserId(userId)
                .orElseThrow(() -> new NotFoundException("Cart not found"));

        Optional<CartItem> cartItemOptional = cart.getCartItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (cartItemOptional.isPresent()) {
            CartItem cartItem = cartItemOptional.get();
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        } else {
            throw new NotFoundException("Product not found in cart");
        }

        return cartRepository.save(updateCartInfor(cart));
    }

    @Override
    public Cart decreaseCartItem(String userId, String productId) {
        Cart cart = cartRepository.findByuserId(userId)
                .orElseThrow(() -> new NotFoundException("Cart not found"));

        Optional<CartItem> cartItemOptional = cart.getCartItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (cartItemOptional.isPresent()) {
            CartItem cartItem = cartItemOptional.get();
            int newQuantity = cartItem.getQuantity() - 1;

            if (newQuantity > 0) {
                cartItem.setQuantity(newQuantity);
            } else {
                cart.getCartItems().remove(cartItem); // Remove item if quantity reaches zero
            }
        } else {
            throw new NotFoundException("Product not found in cart");
        }

        return cartRepository.save(updateCartInfor(cart));
    }

    @Override
    public Cart updateCartInfor(Cart cart) {
        List<String> productIds = cart.getCartItems().stream()
                .map(CartItem::getProductId)
                .collect(Collectors.toList());

        List<Product> products = productRepository.findAllById(productIds);

        Map<String, Integer> productPriceMap = products.stream()
                .collect(Collectors.toMap(Product::getId, Product::getPrice));

        int totalPrice = 0;
        int totalItem = 0;

        for (CartItem i : cart.getCartItems()) {
            totalItem += i.getQuantity();
            totalPrice += productPriceMap.get(i.getProductId()) * i.getQuantity();
        }

        cart.setTotalItem(totalItem);
        cart.setTotalPrice(totalPrice);
        return cart;
    }
}
