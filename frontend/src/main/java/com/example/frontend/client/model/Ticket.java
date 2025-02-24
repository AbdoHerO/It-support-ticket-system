package com.example.frontend.client.model;

import java.time.LocalDateTime;
import java.util.List;

public class Ticket {
    private Long id;
    private String title;
    private String description;
    private Priority priority;
    private Category category;
    private LocalDateTime creationDate;
    private Status status;
    private User createdBy;
    private List<Comment> comments;
    private List<AuditLog> auditLogs;

    // Default constructor
    public Ticket() {}

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public LocalDateTime getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }
    public List<Comment> getComments() { return comments; }
    public void setComments(List<Comment> comments) { this.comments = comments; }
    public List<AuditLog> getAuditLogs() { return auditLogs; }
    public void setAuditLogs(List<AuditLog> auditLogs) { this.auditLogs = auditLogs; }

    public enum Priority { LOW, MEDIUM, HIGH }
    public enum Category { NETWORK, HARDWARE, SOFTWARE, OTHER }
    public enum Status { NEW, IN_PROGRESS, RESOLVED }
}