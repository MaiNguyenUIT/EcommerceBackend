package com.example.backend.controller.user;

import com.example.backend.ApiResult.ApiResult;
import com.example.backend.DTO.CartItemDTO;
import com.example.backend.model.Cart;
import com.example.backend.model.GuestCart;
import com.example.backend.model.User;
import com.example.backend.service.GuestCartService;
import com.example.backend.service.UserService;
import com.example.backend.utils.MapResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/guest/cart")
public class GuestCartController {
    @Autowired
    private UserService userService;
    @Autowired
    private GuestCartService guestCartService;
    @Autowired
    private MapResult mapResult;
    @PostMapping("/{sessionId}")
    private ResponseEntity<ApiResult<GuestCart>> addItemToGuestCart(@PathVariable String sessionId, @RequestBody CartItemDTO cartItemDTO) throws Exception {
        ApiResult<GuestCart> apiResult = mapResult.map(guestCartService.addItemToGuestCart(sessionId, cartItemDTO), "Add " + cartItemDTO.getProductId() + " to cart successfully");
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }
    @GetMapping("/{sessionId}")
    private ResponseEntity<ApiResult<GuestCart>> getGuestCart(@PathVariable String sessionId) throws Exception {
        ApiResult<GuestCart> apiResult = mapResult.map(guestCartService.getCartBySession(sessionId), "Get guest cart successfully");
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }
    @DeleteMapping("/guest/{sessionId}")
    private ResponseEntity<String> clearGuestCart(@PathVariable String sessionId) throws Exception{
        guestCartService.clearCart(sessionId);
        return new ResponseEntity<>("Clear cart successfully", HttpStatus.OK);
    }
    @PutMapping("/{sessionId}")
    private ResponseEntity<ApiResult<GuestCart>> deleteItemFromGuestCart(@PathVariable String sessionId, @RequestParam String productId) throws Exception {
        ApiResult apiResult = mapResult.map(guestCartService.deleteItemFromGuestCart(sessionId, productId),
                "Delete item with id: " + productId + " successfully");
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }
    @PutMapping("/quantity/{sessionId}")
    private ResponseEntity<ApiResult<GuestCart>> updateItemQuantity(@RequestParam String productId,
                                                               @RequestBody int quantity,
                                                               @PathVariable String sessionId){
        ApiResult apiResult = mapResult.map(guestCartService.updateItemQuantity(sessionId, productId, quantity), "Update item quantity successfully");
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }
    @PutMapping("/increase/{sessionId}")
    private ResponseEntity<ApiResult<GuestCart>> increaseItemQuantity(@PathVariable String sessionId,
                                                                 @RequestParam String productId) throws Exception{
        ApiResult apiResult = mapResult.map(guestCartService.increaseCartItem(sessionId, productId), "Increase product quantity successfully");
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }
    @PutMapping("/decrease")
    private ResponseEntity<ApiResult<GuestCart>> decreaseItemQuantity(@PathVariable String sessionId,
                                                                 @RequestParam String productId) throws Exception{
        ApiResult apiResult = mapResult.map(guestCartService.decreaseCartItem(sessionId, productId), "Decrease product quantity successfully");
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }
}
