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

import java.util.Optional;

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
            int totalPrice = 0;
            int totalItem = 0;
            for(CartItem i : guestCart.getCartItems()){
                totalItem += i.getQuantity();
                totalPrice += productRepository.findById(i.getProductId()).get().getPrice()*i.getQuantity();
            }
            guestCart.setTotalItem(totalItem);
            guestCart.setTotalPrice(totalPrice);
            return guestCartRepository.save(guestCart);
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

        return guestCartRepository.save(cart); // Save and return the updated cart
    }

    @Override
    public GuestCart deleteItemFromGuestCart(String sessionId, String productId) {
        GuestCart guestCart = guestCartRepository.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("Cart not found"));
        // Find and remove the cart item by productId
        guestCart.getCartItems().removeIf(item -> item.getProductId().equals(productId));
        // Save the updated cart
        return guestCartRepository.save(guestCart);
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

        return guestCartRepository.save(cart);
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

        return guestCartRepository.save(cart);
    }
}
