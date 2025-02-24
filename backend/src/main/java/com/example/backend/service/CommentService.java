package com.example.backend.service;

import com.example.backend.model.*;
import com.example.backend.repository.AuditLogRepository;
import com.example.backend.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AuditLogRepository auditLogRepository;

    public Comment addComment(Ticket ticket, String content, User user) {
        if (user.getRole() != Role.IT_SUPPORT) {
            return null; // Or throw an exception
        }
        Comment comment = new Comment();
        comment.setTicket(ticket);
        comment.setContent(content);
        comment.setCreatedBy(user);
        commentRepository.save(comment);
        createAuditLog(ticket, "Comment added: " + content, user);
        return comment;
    }

    private void createAuditLog(Ticket ticket, String action, User user) {
        AuditLog auditLog = new AuditLog();
        auditLog.setTicket(ticket);
        auditLog.setAction(action);
        auditLog.setUser(user);
        auditLogRepository.save(auditLog);
    }

    public List<Comment> getCommentsByTicket(Ticket ticket) {
        return commentRepository.findByTicket(ticket);
    }
}