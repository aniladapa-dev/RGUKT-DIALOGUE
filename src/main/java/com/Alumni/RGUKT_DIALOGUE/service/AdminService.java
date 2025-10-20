package com.Alumni.RGUKT_DIALOGUE.service;

import com.Alumni.RGUKT_DIALOGUE.dto.*;
import com.Alumni.RGUKT_DIALOGUE.model.Admin;
import com.Alumni.RGUKT_DIALOGUE.model.User;

import java.util.List;
import java.util.Optional;

public interface AdminService {
    // Admin management
    Admin registerAdmin(Admin admin);
    List<Admin> getAllAdmins();
    void deleteAdmin(Long adminId);
    AdminLoginResponse login(AdminLoginRequest request);

    Optional<Admin> findByEmail(String email);

    // User management
    User registerUser(AdminRegisterUserRequest request);
    List<User> registerUsers(List<AdminRegisterUserRequest> requests);

    void deletePost(Long postId);
}
