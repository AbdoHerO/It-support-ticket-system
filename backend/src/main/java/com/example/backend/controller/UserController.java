package com.example.backend.controller;

import com.example.backend.model.User;
import com.example.backend.model.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class UserController {
    @GetMapping("/user")
    public User getCurrentUser(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return new User(
                null, // ID not needed
                userDetails.getUsername(),
                null, // Don’t return password
                Role.valueOf(userDetails.getAuthorities().iterator().next().getAuthority().replace("ROLE_", ""))
        );
    }
}