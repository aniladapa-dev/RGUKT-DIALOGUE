package com.Alumni.RGUKT_DIALOGUE.security;

import com.Alumni.RGUKT_DIALOGUE.security.UserPrinciple;
import com.Alumni.RGUKT_DIALOGUE.service.JwtService;
import com.Alumni.RGUKT_DIALOGUE.service.MyUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT Filter for Spring Security
 * - Runs once per request
 * - Extracts token from Authorization header
 * - Validates token
 * - Sets Authentication in SecurityContext if valid
 */
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final MyUserDetailService userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // ----------------- 1. Get Authorization Header -----------------
        final String authHeader = request.getHeader("Authorization");

        String email = null;
        String token = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Remove "Bearer " prefix
            try {
                email = jwtService.extractUserName(token); // Extract email
            } catch (Exception e) {
                System.out.println("JWT token parsing failed: " + e.getMessage());
            }
        }

        // ----------------- 2. Validate Token -----------------
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Load user details by email
            UserDetails userDetails = userDetailService.loadUserByUsername(email);

            // Validate token using email
            if (jwtService.validateToken(token, userDetails instanceof com.Alumni.RGUKT_DIALOGUE.security.UserPrinciple
                    ? ((com.Alumni.RGUKT_DIALOGUE.security.UserPrinciple) userDetails).getUser()
                    : null)) {
                // Create authentication token
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // Set authentication in SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // ----------------- 3. Continue Filter Chain -----------------
        filterChain.doFilter(request, response);
    }
}
