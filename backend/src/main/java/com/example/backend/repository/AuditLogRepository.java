package com.example.backend.repository;

import com.example.backend.model.AuditLog;
import com.example.backend.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByTicket(Ticket ticket);
}