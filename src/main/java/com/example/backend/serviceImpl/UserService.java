package com.example.backend.serviceImpl;

import com.example.backend.DTO.UserInforDTO;
import com.example.backend.config.JwtProvider;
import com.example.backend.exception.NotFoundException;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.DTO.request.ChangePasswordRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements com.example.backend.service.UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User findUserByJwtToken(String jwt) throws Exception {
        String username = jwtProvider.getUserNameFromJwtToken(jwt);
        User user = findUserByEmail(username);
        return user;
    }

    @Override
    public User findUserByEmail(String username) throws Exception {
        User user = userRepository.findByemail(username);

        if(user == null){
            throw new NotFoundException("user not found..." + username);
        }
        return user;
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User updateUserInformation(UserInforDTO userInforDTO, User user) {
        user.setAddress(userInforDTO.getAddress());
        user.setPhone(userInforDTO.getPhone());
        user.setPhoto(userInforDTO.getPhoto());
        return userRepository.save(user);
    }

    @Override
    public void changePassword(ChangePasswordRequest changePasswordRequest, User user) throws Exception {
        if(passwordEncoder.matches(changePasswordRequest.getOldPass(), user.getPassword())){
            if(changePasswordRequest.getNewPass().equals(changePasswordRequest.getOldPass())){
                throw new Exception("Old pass and new pass must different");
            }
            user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPass()));
            userRepository.save(user);
        } else {
            throw new Exception("Password is incorrect....");
        }
    }
}
