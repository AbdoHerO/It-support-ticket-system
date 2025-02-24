package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @Column(name = "\"CREATED_AT\"")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    @JsonBackReference("ticket-comments")
    private Ticket ticket;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}