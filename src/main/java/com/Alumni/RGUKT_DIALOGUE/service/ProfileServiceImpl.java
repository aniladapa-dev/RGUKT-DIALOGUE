package com.Alumni.RGUKT_DIALOGUE.service;

import com.Alumni.RGUKT_DIALOGUE.model.*;
import com.Alumni.RGUKT_DIALOGUE.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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
        // Fetch student profile
        Optional<StudentProfile> studentOpt = studentProfileRepository.findByUserId(userId);
        if (studentOpt.isPresent()) {
            return studentOpt.get();
        }

        // Fetch alumni profile
        AlumniProfile alumniProfile = alumniProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found for user ID: " + userId));

        // Fetch and set work experiences for alumni
        List<WorkExperience> experiences = workExperienceRepository.findByAlumniProfile(alumniProfile);
        alumniProfile.setExperiences(Set.copyOf(experiences));

        return alumniProfile; // returns Profile, which is fine
    }



    @Override
    public StudentProfile getStudentProfileByUserId(Long userId) {
        return studentProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Student profile not found for user ID: " + userId));
    }

    @Override
    public AlumniProfile getAlumniProfileByUserId(Long userId) {
        AlumniProfile alumni = alumniProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Alumni profile not found for user ID: " + userId));

        // Load experiences
        List<WorkExperience> experiences = workExperienceRepository.findByAlumniProfile(alumni);
        alumni.setExperiences(Set.copyOf(experiences));

        return alumni;
    }

    // ----------------- UPDATE PROFILES -----------------
    @Override
    public StudentProfile updateStudentProfile(StudentProfile profile) {
        return studentProfileRepository.save(profile);
    }

    @Override
    public AlumniProfile updateAlumniProfile(AlumniProfile profile) {
        return alumniProfileRepository.save(profile);
    }

    // ----------------- ADD SKILLS -----------------
    @Transactional
    @Override
    public void addSkillsToProfile(Long userId, Set<String> skillNames) {
        Profile profile = getProfileByUserId(userId);
        Set<Skill> skillsToAdd = new HashSet<>();

        for (String name : skillNames) {
            Skill skill = skillRepository.findByName(name.toLowerCase())
                    .orElseGet(() -> skillRepository.save(new Skill(name.toLowerCase())));
            skillsToAdd.add(skill);
        }

        if (profile instanceof StudentProfile studentProfile) {
            if (studentProfile.getSkills() == null) studentProfile.setSkills(new HashSet<>());
            studentProfile.getSkills().addAll(skillsToAdd); // owning side updated
            studentProfileRepository.save(studentProfile);
        } else if (profile instanceof AlumniProfile alumniProfile) {
            if (alumniProfile.getSkills() == null) alumniProfile.setSkills(new HashSet<>());
            alumniProfile.getSkills().addAll(skillsToAdd); // owning side updated
            alumniProfileRepository.save(alumniProfile);
        }
    }

    // ----------------- ADD CERTIFICATIONS -----------------

    @Override
    @Transactional
    public void addCertificationsToProfile(Long userId, Set<String> certNames) {
        Profile profile = getProfileByUserId(userId);
        Set<Certification> certifications = new HashSet<>();

        for (String name : certNames) {
            Certification cert = certificationRepository.findByName(name)
                    .orElseGet(() -> {
                        Certification c = new Certification();
                        c.setName(name);
                        return certificationRepository.save(c);
                    });
            certifications.add(cert);
        }

        // Add certifications to profile
        if (profile instanceof StudentProfile studentProfile) {
            studentProfile.getCertifications().addAll(certifications);
            studentProfileRepository.save(studentProfile);
        } else if (profile instanceof AlumniProfile alumniProfile) {
            alumniProfile.getCertifications().addAll(certifications);
            alumniProfileRepository.save(alumniProfile);
        }
    }



    // ----------------- ALUMNI WORK EXPERIENCE -----------------
    @Override
    public void addWorkExperience(String studentId, WorkExperience experience) {
        AlumniProfile alumni = alumniProfileRepository.findByStudentId(studentId)
                .orElseThrow(() -> new RuntimeException("Alumni profile not found for student ID: " + studentId));

        experience.setAlumniProfile(alumni);
        workExperienceRepository.save(experience);
    }

    @Override
    public List<WorkExperience> getWorkExperience(String studentId) {
        AlumniProfile alumni = alumniProfileRepository.findByStudentId(studentId)
                .orElseThrow(() -> new RuntimeException("Alumni profile not found for student ID: " + studentId));
        return workExperienceRepository.findByAlumniProfile(alumni);
    }
}