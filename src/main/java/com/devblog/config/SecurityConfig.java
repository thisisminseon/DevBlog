package com.devblog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// Spring Security configuration
@Configuration
public class SecurityConfig {

    // BCrypt password encoder bean
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Public access
                .requestMatchers("/", "/hello", "/post/list", "/post/detail/**",
                        "/post/search", "/auth/**",
                        "/upload/**", "/css/**", "/js/**", "/images/**").permitAll()
                // Admin only
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // Authenticated users
                .requestMatchers("/post/write", "/post/edit/**", "/post/delete/**",
                        "/comment/**", "/api/like/**", "/mypage/**").authenticated()
                // All other requests
                .anyRequest().permitAll()
            )
            .formLogin(form -> form
                .loginPage("/auth/login")
                .loginProcessingUrl("/auth/login")
                .defaultSuccessUrl("/hello", true)
                .failureUrl("/auth/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/hello")
                .permitAll()
            );

        return http.build();
    }
}
