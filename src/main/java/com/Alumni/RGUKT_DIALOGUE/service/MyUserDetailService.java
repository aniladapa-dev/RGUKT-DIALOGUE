package com.Alumni.RGUKT_DIALOGUE.service;

import com.Alumni.RGUKT_DIALOGUE.model.User;
import com.Alumni.RGUKT_DIALOGUE.repository.UserRepository;
import com.Alumni.RGUKT_DIALOGUE.security.UserPrinciple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service to load user details by email (for authentication)
 */
@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Find user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return new UserPrinciple(user); // Wrap user in UserPrinciple
    }
}
