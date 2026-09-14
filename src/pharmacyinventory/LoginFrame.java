package pharmacyinventory;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.sql.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {

        // Window settings
        setTitle("HealthFirst Pharmacy - Login");
        setSize(500, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(240, 248, 252));

        
        // HEADER
        //------------------------
        
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(null);
        headerPanel.setBackground(new Color(45, 105, 135));
        headerPanel.setBounds(0, 0, 500, 105);

        JLabel titleLabel = new JLabel(
                "HealthFirst Pharmacy",
                SwingConstants.CENTER
        );
        titleLabel.setFont(new Font("Arial", Font.BOLD, 25));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(0, 22, 500, 35);
        headerPanel.add(titleLabel);

        JLabel subtitleLabel = new JLabel(
                "Inventory Management System",
                SwingConstants.CENTER
        );
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(225, 240, 248));
        subtitleLabel.setBounds(0, 57, 500, 25);
        headerPanel.add(subtitleLabel);

        panel.add(headerPanel);

        
        // LOGIN CARD
        //------------------------
        
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(null);
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setBorder(new LineBorder(new Color(205, 215, 220)));
        loginPanel.setBounds(65, 130, 355, 215);

        JLabel loginTitle = new JLabel("User Login");
        loginTitle.setFont(new Font("Arial", Font.BOLD, 18));
        loginTitle.setForeground(new Color(45, 105, 135));
        loginTitle.setBounds(25, 15, 150, 30);
        loginPanel.add(loginTitle);

        // Username label
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 13));
        usernameLabel.setBounds(25, 60, 90, 30);
        loginPanel.add(usernameLabel);

        // Username field
        usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setBounds(115, 60, 210, 32);
        loginPanel.add(usernameField);

        // Password label
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 13));
        passwordLabel.setBounds(25, 105, 90, 30);
        loginPanel.add(passwordLabel);

        // Password field
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBounds(115, 105, 210, 32);
        loginPanel.add(passwordField);

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(new Color(45, 105, 135));
        loginButton.setFocusPainted(false);
        loginButton.setBounds(115, 155, 210, 35);
        loginPanel.add(loginButton);

        // Run login method when button is clicked
        loginButton.addActionListener(e -> login());

        // Pressing Enter also logs in
        getRootPane().setDefaultButton(loginButton);

        panel.add(loginPanel);

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