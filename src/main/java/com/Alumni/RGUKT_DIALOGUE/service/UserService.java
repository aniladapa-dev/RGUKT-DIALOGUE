package com.Alumni.RGUKT_DIALOGUE.service;

import com.Alumni.RGUKT_DIALOGUE.dto.LoginResponse;
import com.Alumni.RGUKT_DIALOGUE.model.AlumniProfile;
import com.Alumni.RGUKT_DIALOGUE.model.StudentProfile;
import com.Alumni.RGUKT_DIALOGUE.model.User;
import com.Alumni.RGUKT_DIALOGUE.repository.AlumniProfileRepository;
import com.Alumni.RGUKT_DIALOGUE.repository.StudentProfileRepository;
import com.Alumni.RGUKT_DIALOGUE.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service to handle User-related operations:
 * - Register user(s)
 * - Login
 * - Update user info
 * - Delete user
 * - Find user by email
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    @Autowired
    private AlumniProfileRepository alumniProfileRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    // Register Single User
    public User registerUser(User user) {
        // Check if password is provided
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new RuntimeException("Password cannot be null or empty!");
        }

        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Set timestamps
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        // Save user to database
        return userRepository.save(user);
    }

    //  Bulk Registration
    public List<User> registerUsers(List<User> users) {
        for (User user : users) {
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                throw new RuntimeException("Password cannot be null or empty for user: " + user.getEmail());
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
        }
        return userRepository.saveAll(users);
    }

    //  Login
    public LoginResponse login(String email, String password) {
        if (email == null || password == null) {
            throw new RuntimeException("Email and password must be provided!");
        }

        // Find user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid email or password!"));

        // Check password matches
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid email or password!");
        }

        // Auto-create profile if first login
        if (user.getRole() == User.Role.STUDENT) {
            studentProfileRepository.findByUser(user).orElseGet(() -> {
                StudentProfile profile = new StudentProfile();
                profile.setUser(user);
                profile.setEnrollmentYear(LocalDateTime.now().getYear());
                profile.setDepartment("");
                profile.setCurrentSemester(null);
                profile.setGpa(null);
                return studentProfileRepository.save(profile);
            });
        } else if (user.getRole() == User.Role.ALUMNI) {
            alumniProfileRepository.findByUser(user).orElseGet(() -> {
                AlumniProfile profile = new AlumniProfile();
                profile.setUser(user);
                profile.setStudentId("AL" + user.getId());
                profile.setName(user.getName());
                profile.setPersonalEmail(user.getEmail());
                profile.setUsername(user.getEmail());
                return alumniProfileRepository.save(profile);
            });
        }

        // Generate JWT token
        String token = jwtService.generateToken(user);

        // Return token and userId
        return new LoginResponse(token, user.getId());
    }

    //  Update User
    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());

            // Update password if provided
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }

            user.setRole(updatedUser.getRole());
            user.setUpdatedAt(LocalDateTime.now());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    //  Delete User
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    //  Find by Email
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getDetails(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }
}
