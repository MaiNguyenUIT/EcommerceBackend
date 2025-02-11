package com.example.backend.controller.user;

import com.example.backend.ApiResult.ApiResult;
import com.example.backend.DTO.RatingDTO;
import com.example.backend.DTO.response.RatingResponse;
import com.example.backend.model.Rating;
import com.example.backend.model.User;
import com.example.backend.service.RatingService;
import com.example.backend.service.UserService;
import com.example.backend.utils.MapResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/rating")
public class RatingController {
    @Autowired
    private RatingService ratingService;
    @Autowired
    private UserService userService;
    @Autowired
    private MapResult mapResult;
    @PostMapping()
    public ResponseEntity<ApiResult<RatingResponse>> createRating(@RequestHeader ("Authorization") String jwt,
                                                                  @RequestBody RatingDTO ratingDTO,
                                                                  @RequestParam String productId) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        RatingResponse ratingResponse = ratingService.createRating(ratingDTO, user.getId(), productId);
        ApiResult apiResult = mapResult.map(ratingResponse, "Create rating successfully");
        return new ResponseEntity<>(apiResult, HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRating(@RequestHeader ("Authorization") String jwt, @PathVariable String id){
        ratingService.deleteRating(id);
        return new ResponseEntity<>("Delete rating successfully", HttpStatus.OK);
    }
    @GetMapping()
    public ResponseEntity<List<Rating>> getAllProductRating(@RequestParam String productId){
        return new ResponseEntity<>(ratingService.getAllRatingByProductId(productId), HttpStatus.OK);
    }
}
