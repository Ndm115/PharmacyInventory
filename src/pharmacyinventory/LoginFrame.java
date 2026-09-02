package pharmacyinventory;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {

        // Window settings
        setTitle("HealthFirst Pharmacy - Login");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(240, 248, 255));

        // Title
        JLabel titleLabel = new JLabel("HealthFirst Pharmacy");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBounds(100, 35, 280, 35);
        panel.add(titleLabel);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Inventory Management System");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setBounds(125, 70, 250, 25);
        panel.add(subtitleLabel);

        // Username label
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(70, 120, 100, 30);
        panel.add(usernameLabel);

        // Username field
        usernameField = new JTextField();
        usernameField.setBounds(170, 120, 200, 30);
        panel.add(usernameField);

        // Password label
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(70, 165, 100, 30);
        panel.add(passwordLabel);

        // Password field
        passwordField = new JPasswordField();
        passwordField.setBounds(170, 165, 200, 30);
        panel.add(passwordField);

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(170, 220, 120, 35);
        panel.add(loginButton);

        // Run login method when button is clicked
        loginButton.addActionListener(e -> login());

        add(panel);
    }

    // Login method
    private void login() {

        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // Make sure fields are not empty
        if (username.isEmpty() || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter username and password."
            );

            return;
        }

        // Check username and password in database
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
            ) {

            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet result = statement.executeQuery();

            // Correct username and password
            if (result.next()) {

                String role = result.getString("role");
                String fullName = result.getString("full_name");
                int userID = result.getInt("user_id");

                JOptionPane.showMessageDialog(
                        this,
                        "Welcome, " + fullName + "!"
                );

                // Open correct dashboard depending on role
                if (role.equals("Admin")) {

                    new AdminDashboard(fullName).setVisible(true);
                    dispose();

                } else if (role.equals("Cashier")) {

                    new CashierDashboard(userID, fullName).setVisible(true);
                    dispose();
                }

            } else {

                // Wrong username or password
                JOptionPane.showMessageDialog(
                        this,
                        "Invalid username or password.",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database error: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}