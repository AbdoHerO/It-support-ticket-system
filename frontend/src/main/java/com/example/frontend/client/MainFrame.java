package com.example.frontend.client;

import com.example.frontend.client.model.User;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private String username;
    private String password;
    private User.Role role;
    private JPanel mainPanel;

    public MainFrame(String username, String password, User.Role role) {
        this.username = username;
        this.password = password;
        this.role = role;

        setTitle("IT Support Ticket System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            // UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "UI Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        initUI();
    }

    private void initUI() {
        try {
            System.out.println("Initializing UI for role: " + role);
            JMenuBar menuBar = new JMenuBar();
            JMenu fileMenu = new JMenu("File");

            // Add Home button
            JMenuItem homeItem = new JMenuItem("Home");
            homeItem.addActionListener(e -> showWelcomeScreen());
            fileMenu.add(homeItem);

            JMenuItem logoutItem = new JMenuItem("Logout");
            logoutItem.addActionListener(e -> {
                dispose();
                LoginDialog newLogin = new LoginDialog(null);
                newLogin.setVisible(true);
                if (newLogin.isSucceeded()) {
                    MainFrame frame = new MainFrame(
                            newLogin.getUsername(),
                            newLogin.getPassword(),
                            newLogin.getRole()
                    );
                    frame.setVisible(true);
                } else {
                    System.exit(0);
                }
            });
            fileMenu.add(logoutItem);
            menuBar.add(fileMenu);

            JMenu ticketsMenu = new JMenu("Tickets");
            if (role == User.Role.EMPLOYEE) {
                System.out.println("Setting up EMPLOYEE menu");
                JMenuItem createTicketItem = new JMenuItem("Create Ticket");
                createTicketItem.addActionListener(e -> showCreateTicketDialog());
                ticketsMenu.add(createTicketItem);

                JMenuItem viewMyTicketsItem = new JMenuItem("View My Tickets");
                viewMyTicketsItem.addActionListener(e -> showTicketList(false));
                ticketsMenu.add(viewMyTicketsItem);

                JMenuItem searchMyTicketsItem = new JMenuItem("Search My Tickets");
                searchMyTicketsItem.addActionListener(e -> showSearchDialog(false));
                ticketsMenu.add(searchMyTicketsItem);
            } else if (role == User.Role.IT_SUPPORT) {
                System.out.println("Setting up IT_SUPPORT menu");
                JMenuItem viewAllTicketsItem = new JMenuItem("View All Tickets");
                viewAllTicketsItem.addActionListener(e -> showTicketList(true));
                ticketsMenu.add(viewAllTicketsItem);

                JMenuItem searchTicketsItem = new JMenuItem("Search Tickets");
                searchTicketsItem.addActionListener(e -> showSearchDialog(true));
                ticketsMenu.add(searchTicketsItem);
            } else {
                throw new IllegalStateException("Unknown role: " + role);
            }
            menuBar.add(ticketsMenu);
            setJMenuBar(menuBar);

            mainPanel = new JPanel(new BorderLayout());
            showWelcomeScreen(); // Set initial view
            add(mainPanel);
        } catch (Exception e) {
            System.err.println("Error initializing UI: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "UI Initialization Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    // method to show welcome screen
    // private void showWelcomeScreen() {
    //     mainPanel.removeAll();
    //     JLabel userBanner = new JLabel("Logged in as: " + username + " (" + role + ")", SwingConstants.CENTER);
    //     userBanner.setFont(new Font("Arial", Font.BOLD, 14));
    //     mainPanel.add(userBanner, BorderLayout.NORTH);
    //     mainPanel.add(new JLabel("Welcome to IT Support Ticket System", SwingConstants.CENTER), BorderLayout.CENTER);
    //     mainPanel.revalidate();
    //     mainPanel.repaint();
    // }

    private void showWelcomeScreen() {
        mainPanel.removeAll();
        mainPanel.setLayout(new BorderLayout());

        JLabel userBanner = new JLabel("Logged in as: " + username + " (" + role + ")", SwingConstants.CENTER);
        userBanner.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(userBanner, BorderLayout.NORTH);

        // Panel of shortcut buttons
        JPanel shortcuts = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        if (role == User.Role.EMPLOYEE) {
            JButton createTicketBtn = new JButton("Create Ticket");
            createTicketBtn.addActionListener(e -> showCreateTicketDialog());
            shortcuts.add(createTicketBtn, gbc);
        }

        if (role == User.Role.EMPLOYEE) {
            gbc.gridy++;
            JButton myTicketsBtn = new JButton("My Tickets");
            myTicketsBtn.addActionListener(e -> showTicketList(false));
            shortcuts.add(myTicketsBtn, gbc);

            gbc.gridy++;
            JButton searchMyTicketsBtn = new JButton("Search My Tickets");
            searchMyTicketsBtn.addActionListener(e -> showSearchDialog(false));
            shortcuts.add(searchMyTicketsBtn, gbc);
        } else if (role == User.Role.IT_SUPPORT) {
            gbc.gridy++;
            JButton allTicketsBtn = new JButton("All Tickets");
            allTicketsBtn.addActionListener(e -> showTicketList(true));
            shortcuts.add(allTicketsBtn, gbc);

            gbc.gridy++;
            JButton searchTicketsBtn = new JButton("Search Tickets");
            searchTicketsBtn.addActionListener(e -> showSearchDialog(true));
            shortcuts.add(searchTicketsBtn, gbc);
        }

        mainPanel.add(shortcuts, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void showCreateTicketDialog() {
        CreateTicketDialog dialog = new CreateTicketDialog(this, username, password);
        dialog.setVisible(true);
    }

    public void showTicketList(boolean allTickets) {
        mainPanel.removeAll();
        TicketListPanel panel = new TicketListPanel(this, username, password, allTickets);
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void showSearchDialog(boolean allTickets) {
        SearchDialog dialog = new SearchDialog(this, username, password, allTickets);
        dialog.setVisible(true);
    }
}