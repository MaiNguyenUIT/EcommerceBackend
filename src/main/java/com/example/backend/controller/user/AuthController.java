package com.example.backend.controller.user;

import com.example.backend.DTO.UserAccountDTO;
import com.example.backend.DTO.request.RefreshTokenRequest;
import com.example.backend.DTO.response.TokenResponse;
import com.example.backend.model.User;
import com.example.backend.DTO.request.ChangePasswordRequest;
import com.example.backend.DTO.request.LoginRequest;
import com.example.backend.DTO.response.AuthResponse;
import com.example.backend.service.AuthService;
import com.example.backend.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication API")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody UserAccountDTO userAccountDTO) throws Exception {
        return new ResponseEntity<>(authService.signUp(userAccountDTO), HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody LoginRequest request) throws Exception {
        return new ResponseEntity<>(authService.signIn(request), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String jwt) throws Exception{
        authService.logOut(jwt);
        return new ResponseEntity<>("Successfully logged out", HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody RefreshTokenRequest request){
        return new ResponseEntity<>(authService.refreshToken(request), HttpStatus.OK);
    }

    @PutMapping("/changePass")
    public ResponseEntity<String> changePass(@RequestHeader("Authorization") String jwt, @RequestBody ChangePasswordRequest changePasswordRequest) throws Exception{
        User user = userService.findUserByJwtToken(jwt);
        userService.changePassword(changePasswordRequest, user);
        return new ResponseEntity<>("Change password successfully", HttpStatus.OK);
    }
}
