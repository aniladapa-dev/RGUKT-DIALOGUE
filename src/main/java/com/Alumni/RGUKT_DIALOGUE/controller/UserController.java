package com.Alumni.RGUKT_DIALOGUE.controller;

import com.Alumni.RGUKT_DIALOGUE.dto.LoginRequest;
import com.Alumni.RGUKT_DIALOGUE.dto.LoginResponse;
import com.Alumni.RGUKT_DIALOGUE.model.User;
import com.Alumni.RGUKT_DIALOGUE.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Register single user
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User savedUser = userService.registerUser(user);
        return ResponseEntity.ok(savedUser);
    }

    // Bulk registration
    @PostMapping("/registers")
    public ResponseEntity<List<User>> registerUsers(@RequestBody List<User> users) {
        List<User> savedUsers = userService.registerUsers(users);
        return ResponseEntity.ok(savedUsers);
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    // Update user
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    // Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
