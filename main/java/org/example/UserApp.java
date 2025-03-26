package org.example;


import mdlaf.MaterialLookAndFeel;

import javax.swing.*;
import java.awt.*;

public class UserApp {
    private JFrame frame;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JTextField loginUsernameField;
    JTextField regUsernameField;
    JTextField emailField;
    JTextField phoneField;
    private JPasswordField loginPasswordField;
    JPasswordField regPasswordField;

    public UserApp() {
        try {
            UIManager.setLookAndFeel(new MaterialLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }

        frame = new JFrame("StudentMS");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);
        frame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        cardPanel.add(CenteredPanel(createLoginPanel()), "login");
        cardPanel.add(CenteredPanel(createSignUpPanel()), "signup");

        frame.add(cardPanel);
        frame.setVisible(true);
    }

    public JPanel CenteredPanel(JPanel panel) {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.add(panel);
        return wrapper;
    }

    public void styleComponent(JComponent component) {
        component.setFont(new Font("Arial", Font.PLAIN, 15));
        component.setPreferredSize(new Dimension(200, 35));
    }

    public JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        JLabel welcomeLabel = new JLabel(
                "<html><center><h1>Student Management System</h1>" +
                        "<p>Please log in to continue.</p></center></html>", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 15));

        loginPanel.add(welcomeLabel);
        loginPanel.add(new JLabel("Username:"));
        loginUsernameField = new JTextField();
        styleComponent(loginUsernameField);
        loginPanel.add(loginUsernameField);

        loginPanel.add(new JLabel("Password:"));
        loginPasswordField = new JPasswordField();
        styleComponent(loginPasswordField);
        loginPanel.add(loginPasswordField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> handleLogin(loginUsernameField.getText(), new String(loginPasswordField.getPassword())));
        JButton toSignupButton = new JButton("Sign Up");
        toSignupButton.addActionListener(e -> cardLayout.show(cardPanel, "signup"));

        buttonPanel.add(loginButton);
        buttonPanel.add(toSignupButton);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);
        mainPanel.add(loginPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    public JPanel createSignUpPanel() {
        JPanel signUpPanel = new JPanel(new GridLayout(5, 2, 10, 10));

        signUpPanel.add(new JLabel("Username:"));
        regUsernameField = new JTextField();
        styleComponent(regUsernameField);
        signUpPanel.add(regUsernameField);

        signUpPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        styleComponent(emailField);
        signUpPanel.add(emailField);

        signUpPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        styleComponent(phoneField);
        signUpPanel.add(phoneField);

        signUpPanel.add(new JLabel("Password:"));
        regPasswordField = new JPasswordField();
        styleComponent(regPasswordField);
        signUpPanel.add(regPasswordField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton signUpButton = new JButton("Sign Up");
        signUpButton.addActionListener(e -> handleSignUp());
        JButton backToLoginButton = new JButton("Back");
        backToLoginButton.addActionListener(e -> cardLayout.show(cardPanel, "login"));

        buttonPanel.add(signUpButton);
        buttonPanel.add(backToLoginButton);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.add(signUpPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    public boolean handleLogin(String username, String password) {
        if (DatabaseHelper.validateLogin(username, password)) {
            new StudentMS();
            frame.dispose();
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid Name or Password!", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
        return true;
    }

    public void handleSignUp() {
        String username = regUsernameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String password = new String(regPasswordField.getPassword());

        if (username.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (DatabaseHelper.registerUser(username, email, phone, password)) {
            JOptionPane.showMessageDialog(frame, "User created successfully!");
            cardLayout.show(cardPanel, "login");
        } else {
            JOptionPane.showMessageDialog(frame, "Error creating user.", "Error", JOptionPane.ERROR_MESSAGE);

        }


    }


}
