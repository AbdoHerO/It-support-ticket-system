package com.example.backend.controller;

import com.example.backend.model.Comment;
import com.example.backend.model.Ticket;
import com.example.backend.model.User;
import com.example.backend.security.CustomUserDetails;
import com.example.backend.service.CommentService;
import com.example.backend.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets/{ticketId}/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private TicketService ticketService;

    @PostMapping
    public Comment addComment(@PathVariable Long ticketId, @RequestBody String content, Authentication auth) {
        User user = ((CustomUserDetails) auth.getPrincipal()).getUser();
        Ticket ticket = ticketService.getTicketById(ticketId);
        if (ticket == null) {
            throw new RuntimeException("Ticket not found");
        }
        return commentService.addComment(ticket, content, user);
    }

    @GetMapping
    public List<Comment> getComments(@PathVariable Long ticketId) {
        Ticket ticket = ticketService.getTicketById(ticketId);
        if (ticket == null) {
            throw new RuntimeException("Ticket not found");
        }
        return commentService.getCommentsByTicket(ticket);
    }
}