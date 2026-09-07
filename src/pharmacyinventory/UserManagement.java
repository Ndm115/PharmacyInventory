package pharmacyinventory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class UserManagement extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField fullNameField;

    private JTable userTable;
    private DefaultTableModel tableModel;

    public UserManagement() {

        setTitle("HealthFirst Pharmacy - Manage Users");
        setSize(800, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(240, 248, 255));

        // Title
        JLabel titleLabel = new JLabel("Manage Cashier Accounts");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 25));
        titleLabel.setBounds(30, 20, 350, 35);
        panel.add(titleLabel);

        // Full name
        JLabel fullNameLabel = new JLabel("Full Name:");
        fullNameLabel.setBounds(50, 80, 100, 25);
        panel.add(fullNameLabel);

        fullNameField = new JTextField();
        fullNameField.setBounds(150, 80, 220, 28);
        panel.add(fullNameField);

        // Username
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 120, 100, 25);
        panel.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(150, 120, 220, 28);
        panel.add(usernameField);

        // Password
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(420, 80, 100, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(520, 80, 200, 28);
        panel.add(passwordField);

        JLabel roleLabel = new JLabel("Role: Cashier");
        roleLabel.setBounds(420, 120, 150, 25);
        panel.add(roleLabel);

        // Buttons
        JButton addButton = new JButton("Add Cashier");
        addButton.setBounds(60, 180, 150, 35);
        panel.add(addButton);

        JButton updateButton = new JButton("Update Cashier");
        updateButton.setBounds(230, 180, 150, 35);
        panel.add(updateButton);

        JButton deleteButton = new JButton("Delete Cashier");
        deleteButton.setBounds(400, 180, 150, 35);
        panel.add(deleteButton);

        JButton clearButton = new JButton("Clear");
        clearButton.setBounds(570, 180, 120, 35);
        panel.add(clearButton);

        // Table
        tableModel = new DefaultTableModel();

        tableModel.setColumnIdentifiers(new String[]{
            "User ID", "Username", "Full Name", "Role"
        });

        userTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBounds(30, 250, 720, 210);
        panel.add(scrollPane);

        // Button actions
        addButton.addActionListener(e -> addCashier());
        updateButton.addActionListener(e -> updateCashier());
        deleteButton.addActionListener(e -> deleteCashier());
        clearButton.addActionListener(e -> clearFields());

        // Populate fields when row selected
        userTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillFieldsFromTable();
            }
        });

        add(panel);

        loadCashiers();
    }

    // Load cashier accounts into table
    private void loadCashiers() {

        tableModel.setRowCount(0);

        String sql = "SELECT user_id, username, full_name, role "
                + "FROM users WHERE role='Cashier'";

        try (
                Connection connection = DatabaseConnection.getConnection();
                Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery(sql)) {

            while (result.next()) {

                tableModel.addRow(new Object[]{
                    result.getInt("user_id"),
                    result.getString("username"),
                    result.getString("full_name"),
                    result.getString("role")
                });
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load cashier accounts."
            );
        }
    }

    // Add cashier
    private void addCashier() {

        if (!fieldsValid()) {
            return;
        }

        String sql = "INSERT INTO users "
                + "(username, password, role, full_name) "
                + "VALUES (?, ?, 'Cashier', ?)";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, usernameField.getText());
            statement.setString(
                    2,
                    new String(passwordField.getPassword())
            );
            statement.setString(3, fullNameField.getText());

            statement.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Cashier added successfully."
            );

            loadCashiers();
            clearFields();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not add cashier. Username may already exist."
            );
        }
    }

    // Update selected cashier
    private void updateCashier() {

        int row = userTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a cashier to update."
            );

            return;
        }

        if (!fieldsValid()) {
            return;
        }

        int userID = Integer.parseInt(
                tableModel.getValueAt(row, 0).toString()
        );

        String sql = "UPDATE users SET "
                + "username=?, password=?, full_name=? "
                + "WHERE user_id=? AND role='Cashier'";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, usernameField.getText());
            statement.setString(
                    2,
                    new String(passwordField.getPassword())
            );
            statement.setString(3, fullNameField.getText());
            statement.setInt(4, userID);

            statement.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Cashier updated successfully."
            );

            loadCashiers();
            clearFields();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not update cashier."
            );
        }
    }

    // Delete selected cashier
    private void deleteCashier() {

        int row = userTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a cashier to delete."
            );

            return;
        }

        int answer = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this cashier?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (answer != JOptionPane.YES_OPTION) {
            return;
        }

        int userID = Integer.parseInt(
                tableModel.getValueAt(row, 0).toString()
        );

        String sql =
                "DELETE FROM users WHERE user_id=? AND role='Cashier'";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userID);
            statement.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Cashier deleted successfully."
            );

            loadCashiers();
            clearFields();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not delete cashier."
            );
        }
    }

    // Fill fields when cashier selected
    private void fillFieldsFromTable() {

        int row = userTable.getSelectedRow();

        if (row == -1) {
            return;
        }

        usernameField.setText(
                tableModel.getValueAt(row, 1).toString()
        );

        fullNameField.setText(
                tableModel.getValueAt(row, 2).toString()
        );

        // Password is not displayed in the table
        passwordField.setText("");
    }

    // Check required fields
    private boolean fieldsValid() {

        if (usernameField.getText().isEmpty()
                || passwordField.getPassword().length == 0
                || fullNameField.getText().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please complete all fields."
            );

            return false;
        }

        return true;
    }

    // Clear fields
    private void clearFields() {

        usernameField.setText("");
        passwordField.setText("");
        fullNameField.setText("");

        userTable.clearSelection();
    }
}