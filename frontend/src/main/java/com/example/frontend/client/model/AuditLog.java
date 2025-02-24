package com.example.frontend.client.model;

import java.time.LocalDateTime;

public class AuditLog {
    private Long id;
    private String action;
    private LocalDateTime timestamp;
    private Ticket ticket;
    private User user;

    // Default constructor
    public AuditLog() {}

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public Ticket getTicket() { return ticket; }
    public void setTicket(Ticket ticket) { this.ticket = ticket; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}