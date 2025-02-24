package com.example.frontend.client;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.http.HttpResponse;

public class CreateTicketDialog extends JDialog {
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JComboBox<String> priorityCombo;
    private JComboBox<String> categoryCombo;

    public CreateTicketDialog(Frame parent, String username, String password) {
        super(parent, "Create Ticket", true);
        setLayout(new MigLayout("fill", "[grow]", "[][][grow][]"));

        add(new JLabel("Title:"), "cell 0 0");
        titleField = new JTextField(20);
        add(titleField, "cell 0 0, growx");

        add(new JLabel("Description:"), "cell 0 1");
        descriptionArea = new JTextArea(5, 20);
        add(new JScrollPane(descriptionArea), "cell 0 1, grow");

        add(new JLabel("Priority:"), "cell 0 2");
        priorityCombo = new JComboBox<>(new String[]{"LOW", "MEDIUM", "HIGH"});
        add(priorityCombo, "cell 0 2");

        add(new JLabel("Category:"), "cell 0 3");
        categoryCombo = new JComboBox<>(new String[]{"NETWORK", "HARDWARE", "SOFTWARE", "OTHER"});
        add(categoryCombo, "cell 0 3");

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            try {
                // Create a map to hold ticket data
                java.util.Map<String, Object> ticketData = new java.util.HashMap<>();
                ticketData.put("title", titleField.getText());
                ticketData.put("description", descriptionArea.getText());
                ticketData.put("priority", priorityCombo.getSelectedItem());
                ticketData.put("category", categoryCombo.getSelectedItem());

                // Convert map to JSON using ObjectMapper
                String json = HttpUtil.getMapper().writeValueAsString(ticketData);

                HttpResponse<String> response = HttpUtil.sendPostRequest("/tickets/create", json, username, password);
                if (response.statusCode() == 200) {
                    JOptionPane.showMessageDialog(this, "Ticket created successfully");
                    dispose(); // Close dialog
                    if (parent instanceof MainFrame) {
                        ((MainFrame) parent).showTicketList(false); // Refresh list for employee
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to create ticket: " + response.statusCode(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error creating ticket: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        add(submitButton, "cell 0 4, center");

        pack();
        setLocationRelativeTo(parent);
    }
}