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

            List<String> productIds = userCart.getCartItems().stream()
                    .map(CartItem::getProductId)
                    .collect(Collectors.toList());

            // Truy vấn tất cả sản phẩm trong một lần
            List<Product> products = productRepository.findAllById(productIds);

            // Tạo Map để truy xuất giá nhanh
            Map<String, Integer> productPriceMap = products.stream()
                    .collect(Collectors.toMap(Product::getId, Product::getPrice));

            // Tính tổng số lượng và tổng giá trị
            int totalPrice = 0;
            int totalItem = 0;

            for (CartItem i : userCart.getCartItems()) {
                totalItem += i.getQuantity();
                totalPrice += productPriceMap.get(i.getProductId()) * i.getQuantity();
            }

            userCart.setTotalItem(totalItem);
            userCart.setTotalPrice(totalPrice);

            return cartRepository.save(userCart);
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

        List<String> productIds = cart.getCartItems().stream()
                .map(CartItem::getProductId)
                .collect(Collectors.toList());

        // Truy vấn tất cả sản phẩm trong một lần
        List<Product> products = productRepository.findAllById(productIds);

        // Tạo Map để truy xuất giá nhanh
        Map<String, Integer> productPriceMap = products.stream()
                .collect(Collectors.toMap(Product::getId, Product::getPrice));

        // Tính tổng số lượng và tổng giá trị
        int totalPrice = 0;
        int totalItem = 0;

        for (CartItem i : cart.getCartItems()) {
            totalItem += i.getQuantity();
            totalPrice += productPriceMap.get(i.getProductId()) * i.getQuantity();
        }

        cart.setTotalItem(totalItem);
        cart.setTotalPrice(totalPrice);

        return cartRepository.save(cart);
    }

    @Override
    public Cart deleteItemFromCart(String userId, String productId) {
        Cart cart = cartRepository.findByuserId(userId)
                .orElseThrow(() -> new NotFoundException("Cart not found"));
        // Find and remove the cart item by productId
        cart.getCartItems().removeIf(item -> item.getProductName().equals(productId));
        // Save the updated cart
        List<String> productIds = cart.getCartItems().stream()
                .map(CartItem::getProductId)
                .collect(Collectors.toList());

        // Truy vấn tất cả sản phẩm trong một lần
        List<Product> products = productRepository.findAllById(productIds);

        // Tạo Map để truy xuất giá nhanh
        Map<String, Integer> productPriceMap = products.stream()
                .collect(Collectors.toMap(Product::getId, Product::getPrice));

        // Tính tổng số lượng và tổng giá trị
        int totalPrice = 0;
        int totalItem = 0;

        for (CartItem i : cart.getCartItems()) {
            totalItem += i.getQuantity();
            totalPrice += productPriceMap.get(i.getProductId()) * i.getQuantity();
        }

        cart.setTotalItem(totalItem);
        cart.setTotalPrice(totalPrice);

        return cartRepository.save(cart);
    }

    @Override
    public void clearCart(String userId) {
        cartRepository.deleteByuserId(userId);
    }

    @Override
    public Cart mergeGuestCartToUserCart(String sessionId, String userId) {
        GuestCart guestCart = guestCartRepository.findById(sessionId).orElse(null);
        Cart userCart = cartRepository.findByuserId(userId).orElse(new Cart());

        if (guestCart != null) {
            userCart.getCartItems().addAll(guestCart.getCartItems());
            userCart.setUserId(userId);
            userCart.setTotalPrice(guestCart.getTotalPrice());
            userCart.setTotalItem(guestCart.getTotalItem());
            guestCartRepository.deleteById(sessionId);
        }

        return cartRepository.save(userCart);
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

        List<String> productIds = cart.getCartItems().stream()
                .map(CartItem::getProductId)
                .collect(Collectors.toList());

        // Truy vấn tất cả sản phẩm trong một lần
        List<Product> products = productRepository.findAllById(productIds);

        // Tạo Map để truy xuất giá nhanh
        Map<String, Integer> productPriceMap = products.stream()
                .collect(Collectors.toMap(Product::getId, Product::getPrice));

        // Tính tổng số lượng và tổng giá trị
        int totalPrice = 0;
        int totalItem = 0;

        for (CartItem i : cart.getCartItems()) {
            totalItem += i.getQuantity();
            totalPrice += productPriceMap.get(i.getProductId()) * i.getQuantity();
        }

        cart.setTotalItem(totalItem);
        cart.setTotalPrice(totalPrice);

        return cartRepository.save(cart);
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

        List<String> productIds = cart.getCartItems().stream()
                .map(CartItem::getProductId)
                .collect(Collectors.toList());

        // Truy vấn tất cả sản phẩm trong một lần
        List<Product> products = productRepository.findAllById(productIds);

        // Tạo Map để truy xuất giá nhanh
        Map<String, Integer> productPriceMap = products.stream()
                .collect(Collectors.toMap(Product::getId, Product::getPrice));

        // Tính tổng số lượng và tổng giá trị
        int totalPrice = 0;
        int totalItem = 0;

        for (CartItem i : cart.getCartItems()) {
            totalItem += i.getQuantity();
            totalPrice += productPriceMap.get(i.getProductId()) * i.getQuantity();
        }

        cart.setTotalItem(totalItem);
        cart.setTotalPrice(totalPrice);

        return cartRepository.save(cart);
    }
}
