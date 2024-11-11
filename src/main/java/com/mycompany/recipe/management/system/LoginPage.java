package com.mycompany.recipe.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.mycompany.recipe.management.system.DatabaseConnection;

public class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginPage() {
        setTitle("Login");
        setSize(500, 500); // Adjusted size to accommodate the image
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set background color
        getContentPane().setBackground(new Color(240, 240, 240));

        // Create panel with BoxLayout for better component alignment
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(240, 240, 240));

        // Add user image at the top
        JLabel userImageLabel = new JLabel();
        ImageIcon userIcon = new ImageIcon("C:\\Users\\harsh\\Downloads\\user_icon.png"); // Double backslashes
        Image image = userIcon.getImage().getScaledInstance(150,150, Image.SCALE_SMOOTH); // Scale the image
        userImageLabel.setIcon(new ImageIcon(image));
        userImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title label with custom font
        JLabel titleLabel = new JLabel("Login To Recipe Management System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(new Color(50, 50, 50));

        // Username label and field with custom font
        JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setPreferredSize(new Dimension(250, 30));

        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameField);

        // Password label and field with custom font
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(250, 30));

        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);

        // Login button with custom font and color
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(0, 123, 255));
        loginButton.setForeground(Color.WHITE);
        loginButton.setPreferredSize(new Dimension(250, 40));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add components to the panel
        panel.add(Box.createVerticalStrut(20)); // Spacer
        panel.add(userImageLabel); // Add user image
        panel.add(Box.createVerticalStrut(10)); // Spacer
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(20)); // Spacer
        panel.add(usernamePanel);
        panel.add(Box.createVerticalStrut(10)); // Spacer
        panel.add(passwordPanel);
        panel.add(Box.createVerticalStrut(20)); // Spacer
        panel.add(loginButton);

        add(panel);

        // Event listener for login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticateUser();
            }
        });
    }

    private void authenticateUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // Database query to check if username and password are correct
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                dispose(); // Close the login window
                new RecipeManagerGUI().setVisible(true); // Open the Recipe Manager GUI
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error during login.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginPage().setVisible(true);
        });
    }
}
