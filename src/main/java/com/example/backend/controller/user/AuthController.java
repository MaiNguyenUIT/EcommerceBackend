package com.example.backend.controller.user;

import com.example.backend.DTO.UserAccountDTO;
import com.example.backend.DTO.request.RefreshTokenRequest;
import com.example.backend.DTO.respone.TokenRespone;
import com.example.backend.ENUM.USER_ROLE;
import com.example.backend.config.JwtProvider;
import com.example.backend.exception.BadRequestException;
import com.example.backend.exception.NotFoundException;
import com.example.backend.mapper.UserAccountMapper;
import com.example.backend.model.RefreshToken;
import com.example.backend.model.User;
import com.example.backend.repository.RefreshTokenRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.DTO.request.ChangePasswordRequest;
import com.example.backend.DTO.request.LoginRequest;
import com.example.backend.DTO.respone.AuthRespone;
import com.example.backend.service.AuthService;
import com.example.backend.service.UserService;
import com.example.backend.serviceImpl.CustomerUserDetailsService;
import com.example.backend.serviceImpl.RefreshTokenService;
import com.example.backend.utils.ValidationAccount;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Collection;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication API")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;


    @PostMapping("/signup")
    public ResponseEntity<AuthRespone> createUserHandler(@RequestBody UserAccountDTO userAccountDTO) throws Exception {
        return new ResponseEntity<>(authService.signUp(userAccountDTO), HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthRespone> signin(@RequestBody LoginRequest request) throws Exception {
        return new ResponseEntity<>(authService.signIn(request), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String jwt) throws Exception{
        authService.logOut(jwt);
        return new ResponseEntity<>("Successfully logged out", HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenRespone> refreshToken(@RequestBody RefreshTokenRequest request){
        return new ResponseEntity<>(authService.refreshToken(request), HttpStatus.OK);
    }

    @PutMapping("/changePass")
    public ResponseEntity<String> changePass(@RequestHeader("Authorization") String jwt, @RequestBody ChangePasswordRequest changePasswordRequest) throws Exception{
        User user = userService.findUserByJwtToken(jwt);
        userService.changePassword(changePasswordRequest, user);
        return new ResponseEntity<>("Change password successfully", HttpStatus.OK);
    }
}
