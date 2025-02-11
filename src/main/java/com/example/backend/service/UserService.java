package com.example.backend.service;

import com.example.backend.DTO.UserInforDTO;
import com.example.backend.model.User;
import com.example.backend.DTO.request.ChangePasswordRequest;

import java.util.List;

public interface UserService {
    public User findUserByJwtToken(String jwt) throws Exception;

    public User findUserByEmail(String username) throws Exception;

    public List<User> getAllUser();

    public User updateUserInformation(UserInforDTO userInfor, User user);

    public void changePassword(ChangePasswordRequest changePasswordRequest, User user) throws Exception;
}
