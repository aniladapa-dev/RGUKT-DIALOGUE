package com.Alumni.RGUKT_DIALOGUE.service;

import com.Alumni.RGUKT_DIALOGUE.dto.*;
import com.Alumni.RGUKT_DIALOGUE.model.Admin;
import com.Alumni.RGUKT_DIALOGUE.model.Post;
import com.Alumni.RGUKT_DIALOGUE.model.User;
import com.Alumni.RGUKT_DIALOGUE.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final UserService userService;
    private final PostService postService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    // --- Admin management ---
    @Override
    public Admin registerAdmin(Admin admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setCreatedAt(LocalDateTime.now());
        admin.setUpdatedAt(LocalDateTime.now());
        return adminRepository.save(admin);
    }

    @Override
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    @Override
    public void deleteAdmin(Long adminId) {
        if (!adminRepository.existsById(adminId)) throw new RuntimeException("Admin not found with ID: " + adminId);
        adminRepository.deleteById(adminId);
    }

    @Override
    public AdminLoginResponse login(AdminLoginRequest request) {
        // 1. Find admin by email
        Admin admin = adminRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password!"));

        // 2. Check password
        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw new RuntimeException("Invalid email or password!");
        }

        String token = jwtService.generateToken(admin);  // pass the whole admin object

        // 4. Return response with token and other info
        return new AdminLoginResponse(token, admin.getName(), admin.getBranch());
    }


    // --- User management ---
    @Override
    public User registerUser(AdminRegisterUserRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        //user.setRole(User.Role.valueOf(request.getRole().toUpperCase()));
        return userService.registerUser(user);
    }

    @Override
    public List<User> registerUsers(List<AdminRegisterUserRequest> requests) {
        return requests.stream()
                .map(this::registerUser)
                .collect(Collectors.toList());
    }

//    // --- Post management ---
//    @Override
//    public void blockOrUnblockPost(AdminPostActionRequest request) {
//        Post post = postService.getPostById(request.getPostId());
//        post.setBlocked(request.isBlock());
//        postService.savePost(post);
//    }

    @Override
    public void deletePost(Long postId) {
        postService.deletePost(postId);
    }

    @Override
    public Optional<Admin> findByEmail(String email) {
        return adminRepository.findByEmail(email);
    }
}
