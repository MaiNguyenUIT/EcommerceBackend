package com.example.backend.serviceImpl;

import com.example.backend.DTO.UserAccountDTO;
import com.example.backend.DTO.request.LoginRequest;
import com.example.backend.DTO.request.RefreshTokenRequest;
import com.example.backend.DTO.response.AuthResponse;
import com.example.backend.DTO.response.TokenResponse;
import com.example.backend.ENUM.USER_ROLE;
import com.example.backend.config.JwtProvider;
import com.example.backend.exception.BadRequestException;
import com.example.backend.mapper.UserAccountMapper;
import com.example.backend.model.RefreshToken;
import com.example.backend.model.User;
import com.example.backend.repository.RefreshTokenRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.utils.ValidationAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collection;

@Service
public class AuthService implements com.example.backend.service.AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private CustomerUserDetailsService customerUserDetailsService;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenBlacklistService tokenBlacklistService;
    @Override
    public AuthResponse signIn(LoginRequest loginRequest) {
        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Authentication authentication = authenticate(username, password);

        User user = userRepository.findByemail(username);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.isEmpty()?null:authorities.iterator().next().getAuthority();
        //get refresh token
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
        String jwt =  jwtProvider.generatedToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setRefreshToken(refreshToken.getToken());
        authResponse.setAccessToken(jwt);
        authResponse.setRole(USER_ROLE.valueOf(role));
        authResponse.setMessage("Sign in success");
        return authResponse;
    }

    @Override
    public AuthResponse signUp(UserAccountDTO userAccountDTO) {
        User isUserNameExist = userRepository.findByemail(userAccountDTO.getEmail());
        if(isUserNameExist != null){
            throw new BadRequestException("Email is already used by another user");
        }
        if(!ValidationAccount.isValidEmail(userAccountDTO.getEmail()) || !ValidationAccount.isValidPassword(userAccountDTO.getPassword())){
            throw new BadRequestException("Email or password is incorrect");
        }
        User createdUser = new User();
        createdUser = UserAccountMapper.INSTANCE.toEntity(userAccountDTO);

        User savedUser = userRepository.save(createdUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userAccountDTO.getEmail(), userAccountDTO.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(savedUser.getId());
        String jwt =  jwtProvider.generatedToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setRole(savedUser.getRole());
        authResponse.setMessage("Register success");
        authResponse.setRefreshToken(refreshToken.getToken());
        authResponse.setAccessToken(jwt);
        return authResponse;
    }

    @Override
    @Transactional
    public String logOut(String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Instant expirationTime = jwtProvider.extractExpiration(jwt).toInstant();
        long expirationMillis = expirationTime.toEpochMilli() - System.currentTimeMillis();
        jwt = jwt.substring(7);

        // Blacklist the access token in Redis
        tokenBlacklistService.blacklistToken(jwt, expirationMillis);
        refreshTokenRepository.deleteByUserId(user.getId());
        return null;
    }

    @Override
    public TokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        RefreshToken refreshToken = refreshTokenRepository.findBytoken(refreshTokenRequest.getToken())
                .orElseThrow(() -> new BadRequestException("Refresh token isn't valid"));
        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new BadRequestException("Refresh token is expiry");
        }
        User user = userRepository.findById(refreshToken.getUserId()).orElseThrow();
        TokenResponse tokenResponse = new TokenResponse();
        String accessToken = jwtProvider.generateAccessToken(user.getEmail(), user.getRole());
//        tokenRespone.setRefreshToken(refreshToken.getToken());
        tokenResponse.setAccessToken(accessToken);
        return tokenResponse;
    }
    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customerUserDetailsService.loadUserByUsername(username);

        if(userDetails == null){
            throw new BadRequestException("Invalid username....");
        }

        if(!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new BadRequestException("Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails,
                null, userDetails.getAuthorities());
    }
}
