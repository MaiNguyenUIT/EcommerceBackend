package com.example.backend.controller.user;

import com.example.backend.ApiResult.ApiResult;
import com.example.backend.DTO.CartItemDTO;
import com.example.backend.model.Cart;
import com.example.backend.model.GuestCart;
import com.example.backend.model.User;
import com.example.backend.service.CartService;
import com.example.backend.service.GuestCartService;
import com.example.backend.service.UserService;
import com.example.backend.utils.MapResult;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;
    @Autowired
    private MapResult mapResult;
    //Add to cart
    @PostMapping()
    private ResponseEntity<ApiResult<Cart>> addItemToUserCart(@RequestHeader("Authorization") String jwt, @RequestBody CartItemDTO cartItemDTO) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        ApiResult<Cart> apiResult = mapResult.map(cartService.addItemToUserCart(user.getId(), cartItemDTO), "Add " + cartItemDTO.getProductId() + " to cart successfully");
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }
    @PostMapping("/merge/{sessionId}")
    private ResponseEntity<ApiResult<Cart>> mergeGuestToUser(@RequestHeader("Authorization") String jwt, @PathVariable String sessionId) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        ApiResult<Cart> apiResult = mapResult.map(cartService.mergeGuestCartToUserCart(sessionId, user.getId()), "Merge cart successfully");
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }
    //Get cart
    @GetMapping("/user")
    private ResponseEntity<ApiResult<Cart>> getUserCart(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        ApiResult<Cart> apiResult = mapResult.map(cartService.getCartByUserId(user.getId()), "Get user cart successfully");
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }
    //Clear cart
    @DeleteMapping()
    private ResponseEntity<String> clearUserCart(@RequestHeader("Authorization") String jwt) throws Exception{
        User user = userService.findUserByJwtToken(jwt);
        cartService.clearCart(user.getId());
        return new ResponseEntity<>("Clear cart successfully", HttpStatus.OK);
    }
    //Delete item from cart
    @PutMapping()
    private ResponseEntity<ApiResult<Cart>> deleteItemFromCart(@RequestHeader("Authorization") String jwt, @RequestParam String productId) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        ApiResult apiResult = mapResult.map(cartService.deleteItemFromCart(user.getId(), productId),
                "Delete item with id: " + productId + " successfully");
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }
    @PutMapping("/quantity")
    private ResponseEntity<ApiResult<Cart>> updateItemQuantity(@RequestHeader("Authorization") String jwt,
                                                               @RequestParam String productId,
                                                               @RequestBody int quantity) throws Exception{
        User user = userService.findUserByJwtToken(jwt);
        ApiResult apiResult = mapResult.map(cartService.updateItemQuantity(user.getId(), productId, quantity), "Update item quantity successfully");
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }
    @PutMapping("/increase")
    private ResponseEntity<ApiResult<Cart>> increaseItemQuantity(@RequestHeader("Authorization") String jwt,
                                                                 @RequestParam String productId) throws Exception{
        User user = userService.findUserByJwtToken(jwt);
        ApiResult apiResult = mapResult.map(cartService.increaseCartItem(user.getId(), productId), "Increase product quantity successfully");
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }
    @PutMapping("/decrease")
    private ResponseEntity<ApiResult<Cart>> decreaseItemQuantity(@RequestHeader("Authorization") String jwt,
                                                                 @RequestParam String productId) throws Exception{
        User user = userService.findUserByJwtToken(jwt);
        ApiResult apiResult = mapResult.map(cartService.decreaseCartItem(user.getId(), productId), "Decrease product quantity successfully");
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }

}
