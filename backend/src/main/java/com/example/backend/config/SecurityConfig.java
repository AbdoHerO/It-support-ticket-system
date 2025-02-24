package com.example.backend.config;

import com.example.backend.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/tickets/create").hasRole("EMPLOYEE")
                        .requestMatchers("/api/tickets/my").hasRole("EMPLOYEE")
                        .requestMatchers("/api/tickets/all").hasRole("IT_SUPPORT")
                        .requestMatchers("/api/tickets/{id}/status").hasRole("IT_SUPPORT")
                        .requestMatchers("/api/tickets/{id}/audit").hasRole("IT_SUPPORT")
                        .requestMatchers("/api/tickets/{id}/comments").authenticated() // Allow both roles to view comments
                        .requestMatchers("/api/tickets/{id}").authenticated() // Allow both roles
                        .requestMatchers("/api/tickets/search").authenticated()
                        .requestMatchers("/api/user").authenticated()
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll() // Enhanced Swagger paths                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults()); // NEW WAY: Use withDefaults() instead of httpBasic()

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(List.of(provider));
    }
}
