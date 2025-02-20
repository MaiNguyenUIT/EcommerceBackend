package com.example.backend.serviceImpl;

import com.example.backend.DTO.CartItemDTO;
import com.example.backend.exception.NotFoundException;
import com.example.backend.mapper.CartItemMapper;
import com.example.backend.model.Cart;
import com.example.backend.model.CartItem;
import com.example.backend.model.GuestCart;
import com.example.backend.model.Product;
import com.example.backend.repository.GuestCartRepository;
import com.example.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GuestCartService implements com.example.backend.service.GuestCartService {
    @Autowired
    private GuestCartRepository guestCartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Override
    public GuestCart addItemToGuestCart(String sessionId, CartItemDTO cartItemDTO) {
        GuestCart guestCart = guestCartRepository.findById(sessionId).orElse(null);
        if(guestCart == null){
            GuestCart newGuestCart = new GuestCart();
            Product product = productRepository.findById(cartItemDTO.getProductId())
                    .orElseThrow(() -> new NotFoundException("Product not found"));
            newGuestCart.getCartItems().add(CartItemMapper.INSTANCE.toEntity(cartItemDTO));
            newGuestCart.setTotalItem(1);
            newGuestCart.setTotalPrice(product.getPrice());
            newGuestCart.setSessionId(sessionId);
            return guestCartRepository.save(newGuestCart);
        } else {
            guestCart.getCartItems().stream()
                    .filter(item -> item.getProductId().equals(cartItemDTO.getProductId()))
                    .findFirst()
                    .ifPresentOrElse(
                            existingItem -> existingItem.setQuantity(existingItem.getQuantity() + 1),
                            () -> guestCart.getCartItems().add(CartItemMapper.INSTANCE.toEntity(cartItemDTO))
                    );
            return guestCartRepository.save(updateCartInfor(guestCart));
        }
    }

    @Override
    public GuestCart getCartBySession(String sessionId) {
        return guestCartRepository.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("Cart not found"));
    }

    @Override
    public GuestCart updateItemQuantity(String sessionId, String productId, int quantity) {
        GuestCart cart = guestCartRepository.findById(sessionId)
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

        return guestCartRepository.save(updateCartInfor(cart)); // Save and return the updated cart
    }

    @Override
    public GuestCart deleteItemFromGuestCart(String sessionId, String productId) {
        GuestCart guestCart = guestCartRepository.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("Cart not found"));
        // Find and remove the cart item by productId
        guestCart.getCartItems().removeIf(item -> item.getProductId().equals(productId));
        // Save the updated cart
        return guestCartRepository.save(updateCartInfor(guestCart));
    }

    @Override
    public void clearCart(String sessionId) {
        guestCartRepository.deleteBysessionId(sessionId);
    }

    @Override
    public GuestCart increaseCartItem(String sessionId, String productId) {
        GuestCart cart = guestCartRepository.findById(sessionId)
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

        return guestCartRepository.save(updateCartInfor(cart));
    }

    @Override
    public GuestCart decreaseCartItem(String sessionId, String productId) {
        GuestCart cart = guestCartRepository.findById(sessionId)
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

        return guestCartRepository.save(updateCartInfor(cart));
    }

    @Override
    public GuestCart updateCartInfor(GuestCart cart) {
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
        return cart;
    }
}
