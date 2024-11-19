package com.example.mailserver.service.impl;

import com.example.mailserver.mapper.UserMapper;
import com.example.mailserver.entity.User;
import com.example.mailserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean loginUser(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean registerUser(User user) {
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        user.setEmail(user.getUsername()+"@example.com");
        return userMapper.insertUser(user) > 0;
    }
}
