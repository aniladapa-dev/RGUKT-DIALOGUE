package com.Alumni.RGUKT_DIALOGUE.security;

import com.Alumni.RGUKT_DIALOGUE.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * UserPrinciple wraps our User entity and implements UserDetails
 * This allows Spring Security to understand our User.
 */
public class UserPrinciple implements UserDetails {

    private final User user;

    public UserPrinciple(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null; // No roles/authorities yet
    }

    @Override
    public String getPassword() {
        return user.getPassword(); // User password
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // Use email as username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Account never expires
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Account never locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Credentials never expire
    }

    @Override
    public boolean isEnabled() {
        return true; // Account always enabled
    }

    public User getUser() {
        return user; // Return original User entity
    }
}
