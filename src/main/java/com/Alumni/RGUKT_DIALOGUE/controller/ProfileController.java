package com.Alumni.RGUKT_DIALOGUE.controller;

import java.util.stream.Collectors;

import com.Alumni.RGUKT_DIALOGUE.dto.AlumniProfileUpdateRequest;
import com.Alumni.RGUKT_DIALOGUE.dto.StudentProfileUpdateRequest;
import com.Alumni.RGUKT_DIALOGUE.model.*;
import com.Alumni.RGUKT_DIALOGUE.service.ProfileService;
import com.Alumni.RGUKT_DIALOGUE.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/*
 * Controller for managing student and alumni profiles.
 * Responsibilities:
 *  - Fetch profile
 *  - Update profile
 *  - Add skills, certifications, and alumni work experiences
 *  - Protected by JWT authentication
 */

/*
 * Endpoints:
 * GET    /api/profiles/{userId}                     -> fetch profile of a student or alumni by user ID
 * GET    /api/profiles/alumni/{studentId}/experience -> fetch alumni work experiences by student ID
 * POST   /api/profiles/{userId}/skills             -> add skills to student profile
 * POST   /api/profiles/{userId}/certifications     -> add certifications to student profile
 * POST   /api/profiles/alumni/{studentId}/experience -> add work experience to alumni profile
 * PUT    /api/profiles/student/{userId}           -> update student profile (using DTO)
 * PUT    /api/profiles/alumni/{userId}            -> update alumni profile (using DTO)
 */

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final JwtService jwtService;


    //  GET Endpoints

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

    //Create/Add Endpoints
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

    // Update student profile using DTO
    @PutMapping("/student/{userId}")
    public ResponseEntity<StudentProfile> updateStudentProfile(@PathVariable Long userId,
                                                               @RequestBody StudentProfileUpdateRequest request,
                                                               @RequestHeader("Authorization") String authHeader) {
        Long loggedInUserId = jwtService.extractUserId(authHeader.substring(7));
        if (!loggedInUserId.equals(userId)) {
            return ResponseEntity.status(403).build();
        }

        StudentProfile profile = profileService.getStudentProfileByUserId(userId);

        // Update basic fields
        profile.setBio(request.getBio());
        profile.setAvatarUrl(request.getAvatarUrl());
        profile.setDepartment(request.getDepartment());
        profile.setEnrollmentYear(request.getEnrollmentYear());
        profile.setCurrentSemester(request.getCurrentSemester());
        profile.setGpa(request.getGpa());

        // Update skills and certifications by names
        if (request.getSkills() != null && !request.getSkills().isEmpty()) {
            profileService.addSkillsToProfile(userId, request.getSkills());
        }
        if (request.getCertifications() != null && !request.getCertifications().isEmpty()) {
            profileService.addCertificationsToProfile(userId, request.getCertifications());
        }

        return ResponseEntity.ok(profileService.updateStudentProfile(profile));
    }

    // Update alumni profile using DTO
    @PutMapping("/alumni/{userId}")
    public ResponseEntity<AlumniProfile> updateAlumniProfile(@PathVariable Long userId,
                                                             @RequestBody AlumniProfileUpdateRequest request,
                                                             @RequestHeader("Authorization") String authHeader) {
        Long loggedInUserId = jwtService.extractUserId(authHeader.substring(7));
        if (!loggedInUserId.equals(userId)) {
            return ResponseEntity.status(403).build();
        }

        AlumniProfile profile = profileService.getAlumniProfileByUserId(userId);

        // Update basic fields
        profile.setBio(request.getBio());
        profile.setAvatarUrl(request.getAvatarUrl());
        profile.setCurrentPosition(request.getCurrentPosition());
        profile.setCompany(request.getCompany());
        profile.setMobileNumber(request.getMobileNumber());
        profile.setStudentId(request.getStudentId());
        profile.setUsername(request.getUsername());

        // Update skills and certifications by names
        if (request.getSkills() != null && !request.getSkills().isEmpty()) {
            profileService.addSkillsToProfile(userId, request.getSkills());
        }
        if (request.getCertifications() != null && !request.getCertifications().isEmpty()) {
            profileService.addCertificationsToProfile(userId, request.getCertifications());
        }

        return ResponseEntity.ok(profileService.updateAlumniProfile(profile));
    }

}
