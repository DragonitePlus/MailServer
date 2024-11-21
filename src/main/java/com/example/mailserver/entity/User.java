package com.example.mailserver.entity;

import lombok.Data;

@Data
public class User {
    // Getters and Setters
    private int userId;
    private String username;
    private String password; // 存储加密后的密码
    private String email;
    private String status;
    private String role;
}
