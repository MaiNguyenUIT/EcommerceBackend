package com.example.backend.controller.user;

import com.example.backend.ApiResult.ApiResult;
import com.example.backend.DTO.WishListItemDTO;
import com.example.backend.model.User;
import com.example.backend.model.WishList;
import com.example.backend.serviceImpl.UserService;
import com.example.backend.serviceImpl.WishListService;
import com.example.backend.utils.MapResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/wishlist")
public class WishListController {
    @Autowired
    private UserService userService;
    @Autowired
    private WishListService wishListService;
    @Autowired
    private MapResult mapResult;
    @PostMapping()
    public ResponseEntity<ApiResult<WishList>> addItemToWishList(@RequestHeader("Authorization") String jwt, @RequestBody WishListItemDTO wishListItemDTO) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        ApiResult<WishList> apiResult = mapResult.map(wishListService.addToWishList(user.getId(), wishListItemDTO), "Add item to wish list successfully");
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResult<WishList>> removeItemFromWishList(@RequestHeader("Authorization") String jwt, @PathVariable String productId) throws Exception{
        User user = userService.findUserByJwtToken(jwt);
        ApiResult<WishList> apiResult = mapResult.map(wishListService.removeFromWishList(user.getId(), productId), "Remove item from wish list successfully");
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }
    @GetMapping()
    public ResponseEntity<ApiResult<WishList>> getUserWishList(@RequestHeader("Authorization") String jwt) throws Exception{
        User user = userService.findUserByJwtToken(jwt);
        ApiResult<WishList> apiResult = mapResult.map(wishListService.getUserWishList(user.getId()), "Get user wish list successfully");
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }
    @PostMapping("/{productId}")
    public ResponseEntity<ApiResult<WishList>> addWishListItemToCart(@RequestHeader("Authorization") String jwt, @PathVariable String productId) throws Exception{
        User user = userService.findUserByJwtToken(jwt);
        ApiResult<WishList> apiResult = mapResult.map(wishListService.addWishListItemToCart(user.getId(), productId), "Add wish list item to cart successfully");
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }
    @DeleteMapping
    public ResponseEntity<String> clearWishList(@RequestHeader("Authorization") String jwt) throws Exception{
        User user = userService.findUserByJwtToken(jwt);
        wishListService.clearWishList(user.getId());
        return new ResponseEntity<>("Clear wish list successfully", HttpStatus.OK);
    }
    @PostMapping("/merge/{sessionId}")
    public ResponseEntity<ApiResult<WishList>> mergeGuestToUser(@RequestHeader("Authorization") String jwt, @PathVariable String sessionId) throws Exception{
        User user = userService.findUserByJwtToken(jwt);
        ApiResult<WishList> apiResult = mapResult.map(wishListService.mergeGuestToUser(sessionId, user.getId()), "Merge guest to user wish list successfully");
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }
}
