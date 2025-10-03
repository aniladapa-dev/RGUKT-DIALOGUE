package com.Alumni.RGUKT_DIALOGUE.service;

import com.Alumni.RGUKT_DIALOGUE.model.*;
import com.Alumni.RGUKT_DIALOGUE.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service to handle Profile operations:
 * - Create profile automatically for new users
 * - Fetch student or alumni profile
 * - Update profiles
 * - Add skills and certifications
 * - Add and fetch alumni work experiences
 */
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final StudentProfileRepository studentProfileRepository;
    private final AlumniProfileRepository alumniProfileRepository;
    private final SkillRepository skillRepository;
    private final CertificationRepository certificationRepository;
    private final WorkExperienceRepository workExperienceRepository;

    // ----------------- CREATE PROFILE -----------------
    @Override
    public Profile createProfileForUser(User user) {
        if (user.getRole() == User.Role.STUDENT) {
            // Return existing profile if exists, else create new
            return studentProfileRepository.findByUser(user)
                    .orElseGet(() -> {
                        StudentProfile profile = new StudentProfile();
                        profile.setUser(user);
                        profile.setBio("");
                        profile.setAvatarUrl("");
                        profile.setDepartment("");
                        profile.setEnrollmentYear(null);
                        profile.setCurrentSemester(null);
                        profile.setGpa(null);
                        return studentProfileRepository.save(profile);
                    });
        } else if (user.getRole() == User.Role.ALUMNI) {
            // Return existing profile if exists, else create new
            return alumniProfileRepository.findByUser(user)
                    .orElseGet(() -> {
                        AlumniProfile profile = new AlumniProfile();
                        profile.setUser(user);
                        profile.setBio("");
                        profile.setAvatarUrl("");
                        profile.setStudentId("AL" + user.getId()); // unique ID
                        profile.setName(user.getName());
                        profile.setPersonalEmail(user.getEmail());
                        profile.setUsername(user.getEmail());
                        return alumniProfileRepository.save(profile);
                    });
        } else {
            throw new RuntimeException("Cannot create profile for role: " + user.getRole());
        }
    }

    // ----------------- FETCH PROFILES -----------------
    @Override
    public Profile getProfileByUserId(Long userId) {
        // Try fetching student profile first
        return studentProfileRepository.findByUserId(userId)
                .map(profile -> (Profile) profile)
                .orElseGet(() -> alumniProfileRepository.findByUserId(userId)
                        .orElseThrow(() -> new RuntimeException("Profile not found for user ID: " + userId)));
    }

    @Override
    public StudentProfile getStudentProfileByUserId(Long userId) {
        return studentProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Student profile not found for user ID: " + userId));
    }

    @Override
    public AlumniProfile getAlumniProfileByUserId(Long userId) {
        return alumniProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Alumni profile not found for user ID: " + userId));
    }

    // ----------------- UPDATE PROFILES -----------------
    @Override
    public StudentProfile updateStudentProfile(StudentProfile profile) {
        // Save updated student profile
        return studentProfileRepository.save(profile);
    }

    @Override
    public AlumniProfile updateAlumniProfile(AlumniProfile profile) {
        // Save updated alumni profile
        return alumniProfileRepository.save(profile);
    }

    // ----------------- ADD SKILLS -----------------
    @Override
    public void addSkillsToProfile(Long userId, Set<String> skillNames) {
        Profile profile = getProfileByUserId(userId);
        Set<Skill> skills = new HashSet<>();

        // Fetch or create skills by name
        for (String name : skillNames) {
            Skill skill = skillRepository.findByName(name)
                    .orElseGet(() -> skillRepository.save(new Skill(null, name, new HashSet<>(), new HashSet<>())));
            skills.add(skill);
        }

        // Add skills to profile and save
        if (profile instanceof StudentProfile studentProfile) {
            studentProfile.setSkills(skills);
            studentProfileRepository.save(studentProfile);
        } else if (profile instanceof AlumniProfile alumniProfile) {
            alumniProfile.setSkills(skills);
            alumniProfileRepository.save(alumniProfile);
        }
    }

    // ----------------- ADD CERTIFICATIONS -----------------
    @Override
    public void addCertificationsToProfile(Long userId, Set<String> certNames) {
        Profile profile = getProfileByUserId(userId);
        Set<Certification> certifications = new HashSet<>();

        // Fetch or create certifications by name
        for (String name : certNames) {
            Certification cert = certificationRepository.findByName(name)
                    .orElseGet(() -> certificationRepository.save(new Certification(null, name, new HashSet<>(), new HashSet<>())));
            certifications.add(cert);
        }

        // Add certifications to profile and save
        if (profile instanceof StudentProfile studentProfile) {
            studentProfile.setCertifications(certifications);
            studentProfileRepository.save(studentProfile);
        } else if (profile instanceof AlumniProfile alumniProfile) {
            alumniProfile.setCertifications(certifications);
            alumniProfileRepository.save(alumniProfile);
        }
    }

    // ----------------- ALUMNI WORK EXPERIENCE -----------------
    @Override
    public void addWorkExperience(String studentId, WorkExperience experience) {
        // Fetch alumni profile by studentId
        AlumniProfile alumni = alumniProfileRepository.findByStudentId(studentId)
                .orElseThrow(() -> new RuntimeException("Alumni profile not found for student ID: " + studentId));

        // Link experience to alumni and save
        experience.setAlumniProfile(alumni);
        workExperienceRepository.save(experience);
    }

    @Override
    public List<WorkExperience> getWorkExperience(String studentId) {
        // Fetch alumni profile by studentId
        AlumniProfile alumni = alumniProfileRepository.findByStudentId(studentId)
                .orElseThrow(() -> new RuntimeException("Alumni profile not found for student ID: " + studentId));

        // Fetch all experiences for alumni
        return workExperienceRepository.findByAlumniProfile(alumni);
    }
}
