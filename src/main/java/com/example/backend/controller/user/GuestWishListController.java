package com.example.backend.controller.user;

import com.example.backend.ApiResult.ApiResult;
import com.example.backend.DTO.WishListItemDTO;
import com.example.backend.model.GuestWishList;
import com.example.backend.model.User;
import com.example.backend.model.WishList;
import com.example.backend.serviceImpl.GuestWishListService;
import com.example.backend.serviceImpl.UserService;
import com.example.backend.serviceImpl.WishListService;
import com.example.backend.utils.MapResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/guest/wishList")
public class GuestWishListController {
    @Autowired
    private UserService userService;
    @Autowired
    private GuestWishListService wishListService;
    @Autowired
    private MapResult mapResult;
    @PostMapping("/{sessionId}")
    public ResponseEntity<ApiResult<GuestWishList>> addItemToWishList(@PathVariable String sessionId, @RequestBody WishListItemDTO wishListItemDTO) throws Exception {
        ApiResult<GuestWishList> apiResult = mapResult.map(wishListService.addToWishList(sessionId, wishListItemDTO), "Add item to wish list successfully");
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }
    @DeleteMapping("/product/{sessionId}")
    public ResponseEntity<ApiResult<GuestWishList>> removeItemFromWishList(@PathVariable String sessionId,
                                                                      @RequestParam String productId) throws Exception{
        ApiResult<GuestWishList> apiResult = mapResult.map(wishListService.removeFromWishList(sessionId, productId), "Remove item from wish list successfully");
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }
    @GetMapping("/{sessionId}")
    public ResponseEntity<ApiResult<GuestWishList>> getWishList(@PathVariable String sessionId) throws Exception{
        ApiResult<GuestWishList> apiResult = mapResult.map(wishListService.getWishList(sessionId), "Get wish list successfully");
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }
    @PostMapping("/cart/{sessionId}")
    public ResponseEntity<ApiResult<GuestWishList>> addWishListItemToCart(@PathVariable String sessionId,
                                                                          @RequestParam String productId) throws Exception{
        ApiResult<GuestWishList> apiResult = mapResult.map(wishListService.addWishListItemToCart(sessionId, productId), "Add wish list item to cart successfully");
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }
    @DeleteMapping("/{sessionId}")
    public ResponseEntity<String> clearWishList(@PathVariable String sessionId) throws Exception{
        wishListService.clearWishList(sessionId);
        return new ResponseEntity<>("Clear wish list successfully", HttpStatus.OK);
    }
}
