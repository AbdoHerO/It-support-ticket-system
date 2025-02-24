package com.example.backend.service;

import com.example.backend.model.*;
import com.example.backend.repository.AuditLogRepository;
import com.example.backend.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private AuditLogRepository auditLogRepository;

    public Ticket createTicket(Ticket ticket, User user) {
        ticket.setCreatedBy(user);
        ticket.setStatus(Status.NEW); // Set via @PrePersist, but ensuring here
        return ticketRepository.save(ticket);
    }

    public List<Ticket> getTicketsByUser(User user) {
        // return ticketRepository.findByCreatedBy(user);
        return ticketRepository.findByCreatedByOrderByCreationDateDesc(user);
    }

    public List<Ticket> getAllTickets() {
        // return ticketRepository.findAll();
        return ticketRepository.findAllByOrderByCreationDateDesc();
    }

    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id).orElse(null);
    }

    public List<Ticket> getTicketsByStatus(Status status) {
        return ticketRepository.findByStatus(status);
    }

    @Transactional
    public Ticket changeStatus(Long ticketId, Status newStatus, User user) {
        Ticket ticket = getTicketById(ticketId);
        if (ticket != null && user.getRole() == Role.IT_SUPPORT) {
            ticket.setStatus(newStatus);
            ticketRepository.save(ticket);
            createAuditLog(ticket, "Status changed to " + newStatus, user);
            return ticket;
        }
        return null; // Or throw an exception
    }

    private void createAuditLog(Ticket ticket, String action, User user) {
        AuditLog auditLog = new AuditLog();
        auditLog.setTicket(ticket);
        auditLog.setAction(action);
        auditLog.setUser(user);
        auditLogRepository.save(auditLog);
    }
}