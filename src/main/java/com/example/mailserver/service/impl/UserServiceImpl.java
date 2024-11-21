package com.example.mailserver.service.impl;

import com.example.mailserver.mapper.UserMapper;
import com.example.mailserver.entity.User;
import com.example.mailserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> selectAll() {
        return userMapper.selectAll();
    }

    @Override
    public boolean loginUser(String username, String password) {
        User user = userMapper.findByUsername(username);
        return user != null && user.getStatus().equals("active") && BCrypt.checkpw(password, user.getPassword());
    }

    @Override
    public boolean registerUser(User user) {
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        user.setEmail(user.getUsername()+"@example.com");
        user.setStatus("active");
        user.setRole("user");
        return userMapper.insertUser(user) > 0;
    }

    @Override
    public boolean findByEmail(String email) {
    	User user = userMapper.findByEmail(email);
    	return user != null;
    }

    @Override
    public String getUserRole(String username){
        return userMapper.findByUsername(username).getRole();
    }

    @Override
    public boolean updateUserStatus(String username, String status) {
        User user = userMapper.findByUsername(username);
        user.setStatus(status);
        return userMapper.updateUser(user);
    }

    @Override
    public boolean updateUserRole(String username, String role) {
        User user = userMapper.findByUsername(username);
        user.setRole(role);
        return userMapper.updateUser(user);
    }

    @Override
    public boolean updateUser(User user) {
        User selectedUser = userMapper.findByUserId(user.getUserId());
        if (selectedUser != null) {
            selectedUser.setUsername(user.getUsername());
            selectedUser.setEmail(user.getEmail());
            selectedUser.setStatus(user.getStatus());
            selectedUser.setRole(user.getRole());
            return userMapper.updateUser(user);
        }
        return false;
    }

    @Override
    public boolean deleteUser(String username) {
        return userMapper.deleteById(username);
    }
}
