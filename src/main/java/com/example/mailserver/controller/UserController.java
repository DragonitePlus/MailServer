package com.example.mailserver.controller;

import com.example.mailserver.entity.Response;
import com.example.mailserver.entity.User;
import com.example.mailserver.service.UserService;
import com.example.mailserver.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenUtil tokenUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        boolean success = userService.loginUser(user.getUsername(), user.getPassword());
        if (success) {
            String token = tokenUtil.generateToken(user.getUsername());
            return ResponseEntity.ok().body(new Response("Login successful", token));
        } else {
            return ResponseEntity.status(401).body(new Response("Invalid credentials"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userService.registerUser(user)) {
            return ResponseEntity.ok().body(new Response("Registration successful"));
        } else {
            return ResponseEntity.status(400).body(new Response("Registration failed"));
        }
    }
}
