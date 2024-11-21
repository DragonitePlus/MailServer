package com.example.mailserver.controller;

import com.example.mailserver.entity.Response;
import com.example.mailserver.entity.User;
import com.example.mailserver.service.UserService;
import com.example.mailserver.util.JsonParserUtil;
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

    @Autowired
    private JsonParserUtil jsonParserUtil;

    @GetMapping("/selectAll")
    public ResponseEntity<?> selectAll() {
        return ResponseEntity.ok().body(jsonParserUtil.toJsonFromEntityList(userService.selectAll()));
    }

    @PostMapping("/selectByUsername")
    public ResponseEntity<?> selectByUsername(@RequestBody String jsonString) {
        String username = jsonParserUtil.parseJsonString(jsonString, "username");
        User user = userService.selectByUsername(username);
        if (user != null) {
            return ResponseEntity.ok().body(jsonParserUtil.toJsonFromEntity(user));
        } else {
            return ResponseEntity.status(404).body(new Response("User not found"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        boolean success = userService.loginUser(user.getUsername(), user.getPassword());
        if (success) {
            boolean isAdmin = userService.getUserRole(user.getUsername()).equals("admin");
            String token = tokenUtil.generateToken(user.getUsername(),isAdmin);
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

    @PostMapping("/setUser")
    public ResponseEntity<?> setUser(@RequestBody String jsonString) {
        String username = jsonParserUtil.parseJsonString(jsonString, "username");
        if (userService.updateUserRole(username, "user")) {
            return ResponseEntity.ok().body(new Response("Role updated successfully"));
        } else {
            return ResponseEntity.status(400).body(new Response("Role update failed"));
        }
    }

    @PostMapping("/setAdmin")
    public ResponseEntity<?> setAdmin(@RequestBody String jsonString) {
        String username = jsonParserUtil.parseJsonString(jsonString, "username");
        if (userService.updateUserRole(username, "admin")) {
            return ResponseEntity.ok().body(new Response("Role updated successfully"));
        } else {
            return ResponseEntity.status(400).body(new Response("Role update failed"));
        }
    }

    @PostMapping("/ban")
    public ResponseEntity<?> banUser(@RequestBody String jsonString) {
        String username = jsonParserUtil.parseJsonString(jsonString, "username");
        if (userService.updateUserStatus(username, "banned")) {
            return ResponseEntity.ok().body(new Response("Status updated successfully"));
        } else {
            return ResponseEntity.status(400).body(new Response("Status update failed"));
        }
    }

    @PostMapping("/activate")
    public ResponseEntity<?> activateUser(@RequestBody String jsonString) {
        String username = jsonParserUtil.parseJsonString(jsonString, "username");
        if (userService.updateUserStatus(username, "active")) {
            return ResponseEntity.ok().body(new Response("Status updated successfully"));
        } else {
            return ResponseEntity.status(400).body(new Response("Status update failed"));
        }
    }

    @PostMapping("/updateDetails")
    public ResponseEntity<?> updateUserDetails(@RequestBody String jsonString) {
        System.out.println(jsonString);
        User user = new User();
        int userId = jsonParserUtil.parseJsonInt(jsonString, "userId");
        String username = jsonParserUtil.parseJsonString(jsonString, "username");
        String email = jsonParserUtil.parseJsonString(jsonString, "email");
        String role = jsonParserUtil.parseJsonString(jsonString, "role");
        String status = jsonParserUtil.parseJsonString(jsonString, "status");
        user.setUserId(userId);
        user.setUsername(username);
        user.setEmail(email);
        user.setRole(role);
        user.setStatus(status);
        if (userService.updateUser(user)) {
            return ResponseEntity.ok().body("User updated successfully");
        } else {
            return ResponseEntity.status(400).body("User update failed");
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestBody String jsonString) {
        String username = jsonParserUtil.parseJsonString(jsonString, "username");
        if (userService.deleteUser(username)) {
            return ResponseEntity.ok().body("User deleted successfully");
        } else {
            return ResponseEntity.status(400).body("User deletion failed");
        }
    }
}
