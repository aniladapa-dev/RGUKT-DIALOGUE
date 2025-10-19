package com.Alumni.RGUKT_DIALOGUE.service;

import com.Alumni.RGUKT_DIALOGUE.model.*;

import java.util.List;
import java.util.Set;

/**
 * Service interface for managing Student and Alumni profiles
 */
public interface ProfileService {

    //  PROFILE FETCH
    Profile getProfileByUserId(Long userId);

    StudentProfile getStudentProfileByUserId(Long userId);

    AlumniProfile getAlumniProfileByUserId(Long userId);

    //  PROFILE UPDATE
    StudentProfile updateStudentProfile(StudentProfile profile);

    AlumniProfile updateAlumniProfile(AlumniProfile profile);

    //  SKILLS & CERTIFICATIONS
    void addSkillsToProfile(Long userId, Set<String> skillNames);

    void addCertificationsToProfile(Long userId, Set<String> certNames);

    //  ALUMNI WORK EXPERIENCE
    void addWorkExperience(String studentId, WorkExperience experience);

    List<WorkExperience> getWorkExperience(String studentId);

    //  CREATE PROFILE
    Profile createProfileForUser(User user);
}
