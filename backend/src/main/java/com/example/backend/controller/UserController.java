package com.example.backend.controller;

import com.example.backend.entity.User;
import com.example.backend.service.UserService;
import com.example.backend.security.JwtUtil;
import com.example.backend.dto.LoginRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.Map;
import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    
    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    private boolean isTokenValid(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }
        String token = authHeader.substring(7);
        return jwtUtil.validateToken(token);
    }

    // create user
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User registeredUser = userService.register(user);
            String token = jwtUtil.generateToken(registeredUser.getUserEmail());
            
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", registeredUser);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // login user
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            User user = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
            String token = jwtUtil.generateToken(user.getUserEmail());
            
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", user);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    // get all users
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/allUsers")
    public ResponseEntity<?> getAllUsers( 
        @Parameter(hidden = true) 
        @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isTokenValid(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Invalid or missing token");
        }

        return ResponseEntity.ok(userService.findAll());
    }

    // get user by id
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(
        @PathVariable Long id,
        @Parameter(hidden = true) 
        @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isTokenValid(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Invalid or missing token");
        }

        return ResponseEntity.ok(userService.findById(id));
    }
    
    // update user
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("update/{id}")
    public ResponseEntity<?> updateUser(
        @PathVariable Long id,
        @RequestBody User updatedUser,
        @Parameter(hidden = true) 
        @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isTokenValid(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Invalid or missing token");
        }

        return ResponseEntity.ok(userService.update(id, updatedUser));
    }

    // delete user
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(
        @PathVariable Long id,
        @Parameter(hidden = true) 
        @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isTokenValid(authHeader)) {    
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Invalid or missing token");
        }

        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
