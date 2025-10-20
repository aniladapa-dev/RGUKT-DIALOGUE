package com.Alumni.RGUKT_DIALOGUE.config;

import com.Alumni.RGUKT_DIALOGUE.security.JwtFilter;
import com.Alumni.RGUKT_DIALOGUE.security.AdminJwtFilter;
import com.Alumni.RGUKT_DIALOGUE.service.AdminService;
import com.Alumni.RGUKT_DIALOGUE.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtService jwtService;
    private final AdminService adminService;  // no @Lazy here


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // ✅ Define AdminJwtFilter as bean here
    @Bean
    public AdminJwtFilter adminJwtFilter() {
        return new AdminJwtFilter(jwtService, adminService);
    }

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter(jwtService, null); // inject UserService if needed
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/login", "/api/users/register",
                                "/api/admin/login", "/api/admin/register").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/users/**").authenticated()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // ✅ Add filters (Admin first, then User)
        http.addFilterBefore(adminJwtFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
