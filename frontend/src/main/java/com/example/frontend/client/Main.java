package com.example.frontend.client;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginDialog loginDlg = new LoginDialog(null);
            loginDlg.setVisible(true);
            if (loginDlg.isSucceeded()) {
                try {
                    System.out.println("Launching MainFrame with " + loginDlg.getUsername() + ", Role: " + loginDlg.getRole());
                    MainFrame frame = new MainFrame(loginDlg.getUsername(), loginDlg.getPassword(), loginDlg.getRole());
                    frame.setVisible(true);
                    // Keep the dialog disposed but ensure frame stays
                    loginDlg.dispose();
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error launching app: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                System.out.println("Login failed, exiting");
                System.exit(0);
            }
        });
    }
}