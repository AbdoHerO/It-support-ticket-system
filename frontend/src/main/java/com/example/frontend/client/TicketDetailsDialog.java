package com.example.frontend.client;

import com.example.frontend.client.model.AuditLog;
import com.example.frontend.client.model.Comment;
import com.example.frontend.client.model.Ticket;
import com.fasterxml.jackson.core.type.TypeReference;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

public class TicketDetailsDialog extends JDialog {
    public TicketDetailsDialog(Frame parent, Long ticketId, String username, String password, boolean isITSupport) {
        super(parent, "Ticket Details", true);
        setLayout(new BorderLayout());

        try {
            // Fetch ticket details
            HttpResponse<String> ticketResponse = HttpUtil.sendGetRequest("/tickets/" + ticketId, username, password);
            System.out.println("Ticket Response Status: " + ticketResponse.statusCode() + ", Body: " + ticketResponse.body());
            if (ticketResponse.statusCode() == 200) {
                Ticket ticket = HttpUtil.getMapper().readValue(ticketResponse.body(), Ticket.class);
                JTextArea detailsArea = new JTextArea(10, 30);
                detailsArea.setText(String.format("ID: %d\nTitle: %s\nDescription: %s\nPriority: %s\nCategory: %s\nStatus: %s\nCreated: %s",
                        ticket.getId(), ticket.getTitle(), ticket.getDescription(), ticket.getPriority(), ticket.getCategory(), ticket.getStatus(), ticket.getCreationDate()));
                detailsArea.setEditable(false);
                add(new JScrollPane(detailsArea), BorderLayout.NORTH);

                // Fetch comments
                HttpResponse<String> commentsResponse = HttpUtil.sendGetRequest("/tickets/" + ticketId + "/comments", username, password);
                System.out.println("Comments Response Status: " + commentsResponse.statusCode() + ", Body: " + commentsResponse.body());
                JTextArea commentsArea = new JTextArea(5, 30);
                if (commentsResponse.statusCode() == 200) {
                    List<Comment> comments = HttpUtil.getMapper().readValue(commentsResponse.body(), new TypeReference<>() {});
                    StringBuilder sb = new StringBuilder();
                    comments.forEach(c -> sb.append(c.getContent()).append(" - ").append(c.getCreatedAt()).append("\n"));
                    commentsArea.setText(sb.length() > 0 ? sb.toString() : "No comments available");
                } else {
                    commentsArea.setText("Failed to load comments: " + commentsResponse.statusCode());
                }
                commentsArea.setEditable(false);
                add(new JScrollPane(commentsArea), BorderLayout.CENTER);

                if (isITSupport) {
                    JPanel actionPanel = new JPanel(new GridLayout(2, 2));
                    JComboBox<String> statusCombo = new JComboBox<>(new String[]{"NEW", "IN_PROGRESS", "RESOLVED"});
                    statusCombo.setSelectedItem(ticket.getStatus().toString());
                    JButton updateStatus = new JButton("Update Status");
                    updateStatus.addActionListener(e -> {
                        try {
                            String newStatus = (String) statusCombo.getSelectedItem();
                            HttpResponse<String> response = HttpUtil.sendPutRequest("/tickets/" + ticketId + "/status?status=" + newStatus, username, password);
                            if (response.statusCode() == 200) {
                                JOptionPane.showMessageDialog(this, "Status updated to " + newStatus);
                                detailsArea.setText(detailsArea.getText().replaceFirst("Status: .*", "Status: " + newStatus));
                                dispose(); // Close dialog
                                ((MainFrame) parent).showTicketList(isITSupport); // Refresh list
                            } else {
                                JOptionPane.showMessageDialog(this, "Failed to update status: " + response.statusCode(), "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(this, "Error updating status: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    });

                    JTextField commentField = new JTextField(20);
                    JButton addComment = new JButton("Add Comment");
                    addComment.addActionListener(e -> {
                        try {
                            String commentText = commentField.getText();
                            if (!commentText.isEmpty()) {
                                HttpResponse<String> response = HttpUtil.sendPostRequest("/tickets/" + ticketId + "/comments", "\"" + commentText + "\"", username, password);
                                if (response.statusCode() == 200) {
                                    // Clear existing "No comments available" message if present
                                    if (commentsArea.getText().equals("No comments available")) {
                                        commentsArea.setText("");
                                    }
                                    // Append the new comment with timestamp
                                    commentsArea.append(commentText + " - " + java.time.LocalDateTime.now() + "\n");
                                    commentField.setText(""); // Clear input field
                                } else {
                                    JOptionPane.showMessageDialog(this, "Failed to add comment: " + response.statusCode(), "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });

                    JButton viewAuditLogButton = new JButton("View Audit Log");
                    viewAuditLogButton.addActionListener(e -> {
                        try {
                            HttpResponse<String> auditResponse = HttpUtil.sendGetRequest("/tickets/" + ticketId + "/audit", username, password);
                            if (auditResponse.statusCode() == 200) {
                                List<AuditLog> auditLogs = HttpUtil.getMapper().readValue(auditResponse.body(), new TypeReference<>() {});
                                StringBuilder sb = new StringBuilder();
                                auditLogs.forEach(log -> sb.append(log.getAction())
                                        .append(" by ")
                                        .append(log.getUser().getUsername())
                                        .append(" at ")
                                        .append(log.getTimestamp())
                                        .append("\n"));
                                JTextArea auditArea = new JTextArea(sb.toString(), 10, 30);
                                auditArea.setEditable(false);
                                JOptionPane.showMessageDialog(this, new JScrollPane(auditArea), "Audit Log", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(this, "Failed to load audit log: " + auditResponse.statusCode(), "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(this, "Error loading audit log: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    });

                    actionPanel.add(new JLabel("Status:"));
                    actionPanel.add(statusCombo);
                    actionPanel.add(updateStatus);
                    actionPanel.add(commentField);
                    actionPanel.add(addComment);
                    actionPanel.add(viewAuditLogButton);
                    add(actionPanel, BorderLayout.SOUTH);
                }
            } else {
                add(new JLabel("Failed to load ticket: " + ticketResponse.statusCode()), BorderLayout.CENTER);
            }
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
            add(new JLabel("Error loading ticket details: " + ex.getMessage()), BorderLayout.CENTER);
        }

        pack();
        setSize(400, 300); // Set a reasonable size
        setLocationRelativeTo(parent);
    }
}