package com.Alumni.RGUKT_DIALOGUE.controller;

import com.Alumni.RGUKT_DIALOGUE.dto.AdminLoginRequest;
import com.Alumni.RGUKT_DIALOGUE.dto.AdminLoginResponse;
import com.Alumni.RGUKT_DIALOGUE.dto.AdminRegisterUsersRequest;
import com.Alumni.RGUKT_DIALOGUE.model.Admin;
import com.Alumni.RGUKT_DIALOGUE.model.User;
import com.Alumni.RGUKT_DIALOGUE.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoints:
 * POST   /api/admin/register      -> Register new admin (HOD)
 * GET    /api/admin/all           -> Get all admins
 * DELETE /api/admin/{id}          -> Delete admin
 * POST   /api/admin/login         -> Admin login (returns JWT)
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminController {

    private final AdminService adminService;

    /** Register a new admin */
    @PostMapping("/register")
    public ResponseEntity<Admin> registerAdmin(@RequestBody Admin admin) {
        return ResponseEntity.ok(adminService.registerAdmin(admin));
    }

    // ---------------- Bulk register users ----------------
    @PostMapping("/users/registers")
    public ResponseEntity<List<User>> registerUsers(@RequestBody AdminRegisterUsersRequest request) {
        List<User> savedUsers = adminService.registerUsers(request.getUsers());
        return ResponseEntity.ok(savedUsers);
    }


    /** Get all admins */
    @GetMapping("/all")
    public ResponseEntity<List<Admin>> getAllAdmins() {
        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    /** Delete an admin */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.ok().build();
    }

    /** Login admin */
    @PostMapping("/login")
    public ResponseEntity<AdminLoginResponse> login(@RequestBody AdminLoginRequest request) {
        return ResponseEntity.ok(adminService.login(request));
    }


}
