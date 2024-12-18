package com.example.mailserver.service;

import com.example.mailserver.entity.User;

import java.util.List;

public interface UserService {
    List<User> selectAll();

    User selectByUsername(String username);

    boolean loginUser(String username, String password);
    boolean registerUser(User user);
    boolean findByEmail(String username);
    String getUserRole(String username);

    boolean updateUserStatus(String username, String status);

    boolean updateUserRole(String username, String role);

    boolean updateUser(User user);

    boolean deleteUser(String username);
}
