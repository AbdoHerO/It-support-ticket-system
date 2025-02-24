package com.example.backend;

import com.example.backend.model.*;
import com.example.backend.repository.AuditLogRepository;
import com.example.backend.repository.TicketRepository;
import com.example.backend.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TicketServiceTest {
    @InjectMocks
    private TicketService ticketService;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private AuditLogRepository auditLogRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTicket() {
        User user = new User();
        user.setId(1L);
        Ticket ticket = new Ticket();
        ticket.setTitle("Test Ticket");

        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        Ticket result = ticketService.createTicket(ticket, user);

        assertEquals(Status.NEW, result.getStatus());
        assertEquals(user, result.getCreatedBy());
        verify(ticketRepository, times(1)).save(ticket);
    }

    @Test
    public void testChangeStatus() {
        User itSupport = new User();
        itSupport.setId(2L);
        itSupport.setRole(Role.IT_SUPPORT);
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setStatus(Status.NEW);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        when(auditLogRepository.save(any(AuditLog.class))).thenReturn(new AuditLog());

        Ticket result = ticketService.changeStatus(1L, Status.IN_PROGRESS, itSupport);

        assertEquals(Status.IN_PROGRESS, result.getStatus());
        verify(auditLogRepository, times(1)).save(any(AuditLog.class));
    }
}