package com.Alumni.RGUKT_DIALOGUE.controller;

import com.Alumni.RGUKT_DIALOGUE.model.*;
import com.Alumni.RGUKT_DIALOGUE.service.ProfileService;
import com.Alumni.RGUKT_DIALOGUE.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * Controller for managing student and alumni profiles.
 * Responsibilities:
 *  - Fetch profile
 *  - Update profile
 *  - Add skills, certifications, and alumni work experiences
 *  - Protected by JWT authentication
 */
@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final JwtService jwtService;

    // ----------------- GET Endpoints -----------------

    /** Fetch profile by user ID (student or alumni) */
    @GetMapping("/{userId}")
    public ResponseEntity<Profile> getProfile(@PathVariable Long userId) {
        Profile profile = profileService.getProfileByUserId(userId);
        return ResponseEntity.ok(profile);
    }

    /** Fetch alumni work experiences by studentId */
    @GetMapping("/alumni/{studentId}/experience")
    public ResponseEntity<List<WorkExperience>> getAlumniWorkExperience(@PathVariable String studentId) {
        List<WorkExperience> experiences = profileService.getWorkExperience(studentId);
        return ResponseEntity.ok(experiences);
    }

    // ----------------- CREATE / ADD Endpoints -----------------

    /** Add skills to student profile */
    @PostMapping("/{userId}/skills")
    public ResponseEntity<String> addSkills(@PathVariable Long userId,
                                            @RequestBody Set<String> skillNames,
                                            @RequestHeader("Authorization") String authHeader) {
        Long loggedInUserId = jwtService.extractUserId(authHeader.substring(7));
        if (!loggedInUserId.equals(userId)) {
            return ResponseEntity.status(403).body("Forbidden: Cannot modify another user's profile");
        }

        // Delegate conversion from String -> Skill to service
        profileService.addSkillsToProfile(userId, skillNames);
        return ResponseEntity.ok("Skills added successfully!");
    }

    /** Add certifications to student profile */
    @PostMapping("/{userId}/certifications")
    public ResponseEntity<String> addCertifications(@PathVariable Long userId,
                                                    @RequestBody Set<String> certNames,
                                                    @RequestHeader("Authorization") String authHeader) {
        Long loggedInUserId = jwtService.extractUserId(authHeader.substring(7));
        if (!loggedInUserId.equals(userId)) {
            return ResponseEntity.status(403).body("Forbidden: Cannot modify another user's profile");
        }

        // Delegate conversion from String -> Certification to service
        profileService.addCertificationsToProfile(userId, certNames);
        return ResponseEntity.ok("Certifications added successfully!");
    }

    /** Add work experience to alumni profile */
    @PostMapping("/alumni/{studentId}/experience")
    public ResponseEntity<String> addAlumniWorkExperience(@PathVariable String studentId,
                                                          @RequestBody WorkExperience experience,
                                                          @RequestHeader("Authorization") String authHeader) {
        Long loggedInUserId = jwtService.extractUserId(authHeader.substring(7));
        AlumniProfile profile = profileService.getAlumniProfileByUserId(loggedInUserId);

        if (!profile.getStudentId().equals(studentId)) {
            return ResponseEntity.status(403).body("Forbidden: Cannot modify another alumni's profile");
        }

        profileService.addWorkExperience(studentId, experience);
        return ResponseEntity.ok("Work experience added successfully!");
    }

    // ----------------- UPDATE Endpoints -----------------

    /** Update student profile */
    @PutMapping("/student/{userId}")
    public ResponseEntity<StudentProfile> updateStudentProfile(@PathVariable Long userId,
                                                               @RequestBody StudentProfile updatedProfile,
                                                               @RequestHeader("Authorization") String authHeader) {
        Long loggedInUserId = jwtService.extractUserId(authHeader.substring(7));
        if (!loggedInUserId.equals(userId)) {
            return ResponseEntity.status(403).build();
        }

        StudentProfile profile = profileService.getStudentProfileByUserId(userId);

        // Update basic fields
        profile.setBio(updatedProfile.getBio());
        profile.setAvatarUrl(updatedProfile.getAvatarUrl());
        profile.setDepartment(updatedProfile.getDepartment());
        profile.setEnrollmentYear(updatedProfile.getEnrollmentYear());
        profile.setCurrentSemester(updatedProfile.getCurrentSemester());
        profile.setGpa(updatedProfile.getGpa());

        // Delegate to service for DB save
        return ResponseEntity.ok(profileService.updateStudentProfile(profile));
    }

    /** Update alumni profile */
    @PutMapping("/alumni/{userId}")
    public ResponseEntity<AlumniProfile> updateAlumniProfile(@PathVariable Long userId,
                                                             @RequestBody AlumniProfile updatedProfile,
                                                             @RequestHeader("Authorization") String authHeader) {
        Long loggedInUserId = jwtService.extractUserId(authHeader.substring(7));
        if (!loggedInUserId.equals(userId)) {
            return ResponseEntity.status(403).build();
        }

        AlumniProfile profile = profileService.getAlumniProfileByUserId(userId);

        // Update basic fields
        profile.setBio(updatedProfile.getBio());
        profile.setAvatarUrl(updatedProfile.getAvatarUrl());
        profile.setCurrentPosition(updatedProfile.getCurrentPosition());
        profile.setCompany(updatedProfile.getCompany());
        profile.setMobileNumber(updatedProfile.getMobileNumber());
        profile.setStudentId(updatedProfile.getStudentId());
        profile.setUsername(updatedProfile.getUsername());

        // Delegate to service for DB save
        return ResponseEntity.ok(profileService.updateAlumniProfile(profile));
    }
}
