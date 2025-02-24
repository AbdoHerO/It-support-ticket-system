package com.example.frontend.client;

import com.example.frontend.client.model.Ticket;
import com.fasterxml.jackson.core.type.TypeReference;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

public class SearchDialog extends JDialog {
    private JTable resultTable;
    private DefaultTableModel tableModel;
    private String username;
    private String password;
    private boolean allTickets;

    public SearchDialog(Frame parent, String username, String password, boolean allTickets) {
        super(parent, "Search Tickets", true);
        this.username = username;
        this.password = password;
        this.allTickets = allTickets;
        setLayout(new BorderLayout());

        JPanel searchPanel = new JPanel();
        JTextField idField = new JTextField(10);
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"", "NEW", "IN_PROGRESS", "RESOLVED"});
        JButton searchButton = new JButton("Search");
        JButton clearButton = new JButton("Clear"); // New button
        searchPanel.add(new JLabel("ID:"));
        searchPanel.add(idField);
        searchPanel.add(new JLabel("Status:"));
        searchPanel.add(statusCombo);
        searchPanel.add(searchButton);
        searchPanel.add(clearButton);
        add(searchPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Title", "Priority", "Category", "Status", "Creation Date"};
        tableModel = new DefaultTableModel(new Object[0][6], columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        resultTable = new JTable(tableModel);
        add(new JScrollPane(resultTable), BorderLayout.CENTER);

        searchButton.addActionListener(e -> searchTickets(idField.getText(), (String) statusCombo.getSelectedItem()));
        clearButton.addActionListener(e -> {
            idField.setText("");
            statusCombo.setSelectedIndex(0);
            tableModel.setRowCount(0); // Clear table
        });

        pack();
        setSize(600, 400);
        setLocationRelativeTo(parent);
    }

    private void searchTickets(String idText, String status) {
        try {
            StringBuilder path = new StringBuilder("/tickets/search");
            boolean hasParams = false;
            if (!idText.isEmpty()) {
                path.append("?id=").append(idText);
                hasParams = true;
            }
            if (!status.isEmpty()) {
                path.append(hasParams ? "&" : "?").append("status=").append(status);
            }
            HttpResponse<String> response = HttpUtil.sendGetRequest(path.toString(), username, password);
            System.out.println("Search Response Status: " + response.statusCode() + ", Body: " + response.body());
            if (response.statusCode() == 200) {
                List<Ticket> tickets = HttpUtil.getMapper().readValue(response.body(), new TypeReference<>() {});
                tableModel.setRowCount(0); // Clear previous data
                for (Ticket t : tickets) {
                    tableModel.addRow(new Object[]{t.getId(), t.getTitle(), t.getPriority(), t.getCategory(), t.getStatus(), t.getCreationDate()});
                }
                if (tickets.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No tickets found", "Info", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                tableModel.setRowCount(0);
                JOptionPane.showMessageDialog(this, "Search failed: " + response.statusCode(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
            tableModel.setRowCount(0);
            JOptionPane.showMessageDialog(this, "Error searching tickets: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}