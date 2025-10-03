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

    @GetMapping("/{userId}")
    public ResponseEntity<Profile> getProfile(@PathVariable Long userId) {
        Profile profile = profileService.getProfileByUserId(userId);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/alumni/{studentId}/experience")
    public ResponseEntity<List<WorkExperience>> getAlumniWorkExperience(@PathVariable String studentId) {
        List<WorkExperience> experiences = profileService.getWorkExperience(studentId);
        return ResponseEntity.ok(experiences);
    }

    // ----------------- CREATE / ADD Endpoints -----------------

    @PostMapping("/{userId}/skills")
    public ResponseEntity<String> addSkills(@PathVariable Long userId,
                                            @RequestBody Set<String> skillNames,
                                            @RequestHeader("Authorization") String authHeader) {
        Long loggedInUserId = jwtService.extractUserId(authHeader.substring(7));
        if (!loggedInUserId.equals(userId)) {
            return ResponseEntity.status(403).build();
        }
        profileService.addSkillsToProfile(userId, skillNames);
        return ResponseEntity.ok("Skills added successfully!");
    }

    @PostMapping("/{userId}/certifications")
    public ResponseEntity<String> addCertifications(@PathVariable Long userId,
                                                    @RequestBody Set<String> certNames,
                                                    @RequestHeader("Authorization") String authHeader) {
        Long loggedInUserId = jwtService.extractUserId(authHeader.substring(7));
        if (!loggedInUserId.equals(userId)) {
            return ResponseEntity.status(403).build();
        }
        profileService.addCertificationsToProfile(userId, certNames);
        return ResponseEntity.ok("Certifications added successfully!");
    }

    @PostMapping("/alumni/{studentId}/experience")
    public ResponseEntity<String> addAlumniWorkExperience(@PathVariable String studentId,
                                                          @RequestBody WorkExperience experience,
                                                          @RequestHeader("Authorization") String authHeader) {
        Long loggedInUserId = jwtService.extractUserId(authHeader.substring(7));
        AlumniProfile profile = profileService.getAlumniProfileByUserId(loggedInUserId);
        if (!profile.getStudentId().equals(studentId)) {
            return ResponseEntity.status(403).build();
        }
        profileService.addWorkExperience(studentId, experience);
        return ResponseEntity.ok("Work experience added successfully!");
    }

    // ----------------- UPDATE Endpoints -----------------

    @PutMapping("/student/{userId}")
    public ResponseEntity<StudentProfile> updateStudentProfile(@PathVariable Long userId,
                                                               @RequestBody StudentProfile updatedProfile,
                                                               @RequestHeader("Authorization") String authHeader) {
        Long loggedInUserId = jwtService.extractUserId(authHeader.substring(7));
        if (!loggedInUserId.equals(userId)) {
            return ResponseEntity.status(403).build();
        }

        StudentProfile profile = profileService.getStudentProfileByUserId(userId);
        profile.setBio(updatedProfile.getBio());
        profile.setAvatarUrl(updatedProfile.getAvatarUrl());
        profile.setDepartment(updatedProfile.getDepartment());
        profile.setEnrollmentYear(updatedProfile.getEnrollmentYear());
        profile.setCurrentSemester(updatedProfile.getCurrentSemester());
        profile.setGpa(updatedProfile.getGpa());

        return ResponseEntity.ok(profileService.updateStudentProfile(profile));
    }

    @PutMapping("/alumni/{userId}")
    public ResponseEntity<AlumniProfile> updateAlumniProfile(@PathVariable Long userId,
                                                             @RequestBody AlumniProfile updatedProfile,
                                                             @RequestHeader("Authorization") String authHeader) {
        Long loggedInUserId = jwtService.extractUserId(authHeader.substring(7));
        if (!loggedInUserId.equals(userId)) {
            return ResponseEntity.status(403).build();
        }

        AlumniProfile profile = profileService.getAlumniProfileByUserId(userId);
        profile.setBio(updatedProfile.getBio());
        profile.setAvatarUrl(updatedProfile.getAvatarUrl());
        profile.setCurrentPosition(updatedProfile.getCurrentPosition());
        profile.setCompany(updatedProfile.getCompany());
        profile.setMobileNumber(updatedProfile.getMobileNumber());
        profile.setStudentId(updatedProfile.getStudentId());
        profile.setUsername(updatedProfile.getUsername());

        return ResponseEntity.ok(profileService.updateAlumniProfile(profile));
    }
}
