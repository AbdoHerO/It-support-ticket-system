package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "users")
@JsonIgnoreProperties({"tickets", "comments", "auditLogs"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password; // Hashed with BCrypt

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Ticket> tickets;

    @OneToMany(mappedBy = "createdBy")
    private List<Comment> comments;

    @OneToMany(mappedBy = "user")
    private List<AuditLog> auditLogs;

    public User() {}

    // Constructor for controller response
    public User(Long id, String username, String password, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

}