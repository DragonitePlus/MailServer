package com.example.mailserver.service;

import com.example.mailserver.entity.User;

public interface UserService {
    boolean loginUser(String username, String password);
    boolean registerUser(User user);
    boolean findEmail(String username);
}
