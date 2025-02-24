package com.example.frontend.client;

import com.example.frontend.client.model.User;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.http.HttpResponse;

public class LoginDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private boolean succeeded;
    private String username;
    private String password;
    private User.Role role;

    public LoginDialog(Frame parent) {
        super(parent, "Login", true);
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();
        cs.insets = new Insets(5, 5, 5, 5);

        cs.gridx = 0;
        cs.gridy = 0;
        panel.add(new JLabel("Username:"), cs);
        cs.gridx = 1;
        usernameField = new JTextField(20);
        panel.add(usernameField, cs);

        cs.gridx = 0;
        cs.gridy = 1;
        panel.add(new JLabel("Password:"), cs);
        cs.gridx = 1;
        passwordField = new JPasswordField(20);
        panel.add(passwordField, cs);

        loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            if (authenticate(usernameField.getText(), new String(passwordField.getPassword()))) {
                succeeded = true;
                username = usernameField.getText();
                password = new String(passwordField.getPassword());
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
                usernameField.setText("");
                passwordField.setText("");
            }
        });

        JPanel bp = new JPanel();
        bp.add(loginButton);
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);

        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    private boolean authenticate(String username, String password) {
        try {
            System.out.println("Attempting login with Username: " + username + ", Password: " + password);
            HttpResponse<String> response = HttpUtil.sendGetRequest("/user", username, password);
            System.out.println("Status: " + response.statusCode());
            System.out.println("Body: " + response.body());
            if (response.statusCode() == 200) {
                User user = HttpUtil.getMapper().readValue(response.body(), User.class);
                this.role = user.getRole();
                System.out.println("Login succeeded, Role: " + this.role);
                return true;
            }
            System.out.println("Login failed, Status: " + response.statusCode());
            JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean isSucceeded() { return succeeded; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public User.Role getRole() { return role; }
}