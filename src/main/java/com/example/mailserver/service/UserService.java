package com.example.mailserver.service;

import com.example.mailserver.entity.User;

import java.util.List;

public interface UserService {
    List<User> selectAll();

    boolean loginUser(String username, String password);
    boolean registerUser(User user);
    boolean findByEmail(String username);
    String getUserRole(String username);
}
