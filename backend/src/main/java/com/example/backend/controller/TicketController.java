package com.example.backend.controller;

import com.example.backend.model.*;
import com.example.backend.security.CustomUserDetails;
import com.example.backend.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @PostMapping("/create")
    public Ticket createTicket(@RequestBody Ticket ticket, Authentication auth) {
        User user = ((CustomUserDetails) auth.getPrincipal()).getUser();
        return ticketService.createTicket(ticket, user);
    }

    @GetMapping("/my")
    public List<Ticket> getMyTickets(Authentication auth) {
        User user = ((CustomUserDetails) auth.getPrincipal()).getUser();
        return ticketService.getTicketsByUser(user);
    }

    @GetMapping("/all")
    public List<Ticket> getAllTickets() {
        return ticketService.getAllTickets();
    }

    @GetMapping("/{id}")
    public Ticket getTicketById(@PathVariable Long id) {
        Ticket ticket = ticketService.getTicketById(id);
        if (ticket == null) {
            throw new RuntimeException("Ticket not found");
        }
        return ticket;
    }

    @PutMapping("/{id}/status")
    public Ticket changeStatus(@PathVariable Long id, @RequestParam Status status, Authentication auth) {
        User user = ((CustomUserDetails) auth.getPrincipal()).getUser();
        return ticketService.changeStatus(id, status, user);
    }

    @GetMapping("/search")
    public List<Ticket> searchTickets(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) Status status,
            Authentication auth
    ) {
        User user = ((CustomUserDetails) auth.getPrincipal()).getUser();
        List<Ticket> tickets = (user.getRole() == Role.IT_SUPPORT)
                ? ticketService.getAllTickets()
                : ticketService.getTicketsByUser(user);

        if (id != null || status != null) {
            return tickets.stream()
                    .filter(t -> (id == null    || t.getId().equals(id))
                            && (status == null || t.getStatus() == status))
                    .collect(Collectors.toList());
        }
        return tickets;
    }

    @GetMapping("/{id}/audit")
    public List<AuditLog> getAuditLogs(@PathVariable Long id) {
        Ticket ticket = ticketService.getTicketById(id);
        if (ticket == null) {
            throw new RuntimeException("Ticket not found");
        }
        return ticket.getAuditLogs();
    }
}