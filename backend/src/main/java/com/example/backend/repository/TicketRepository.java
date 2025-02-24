package com.example.backend.repository;

import com.example.backend.model.Status;
import com.example.backend.model.Ticket;
import com.example.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByCreatedBy(User user);
    List<Ticket> findByStatus(Status status);

    List<Ticket> findByCreatedByOrderByCreationDateDesc(User user);
    List<Ticket> findAllByOrderByCreationDateDesc();
}