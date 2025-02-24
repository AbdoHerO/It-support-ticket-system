package com.example.frontend.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private Long id;
    private String username;
    private Role role;

    // Default constructor
    public User() {}

    // Constructor with required fields
    public User(Long id, String username, Role role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public enum Role {
        EMPLOYEE, IT_SUPPORT
    }
}