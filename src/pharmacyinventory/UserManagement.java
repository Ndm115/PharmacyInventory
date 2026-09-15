package pharmacyinventory;

import javax.swing.*;
import javax.swing.border.LineBorder;
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

        // Window settings
        setTitle("HealthFirst Pharmacy - Manage Users");
        setSize(850, 590);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(240, 248, 252));

        
        // HEADER
        // --------------------------------

        JPanel headerPanel = new JPanel(null);
        headerPanel.setBackground(new Color(45, 105, 135));
        headerPanel.setBounds(0, 0, 850, 90);

        JLabel titleLabel = new JLabel("Cashier Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 25));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(30, 17, 350, 35);
        headerPanel.add(titleLabel);

        JLabel subtitleLabel = new JLabel(
                "Create, update and manage cashier accounts"
        );
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(225, 240, 248));
        subtitleLabel.setBounds(30, 50, 400, 25);
        headerPanel.add(subtitleLabel);

        panel.add(headerPanel);

        
        // CASHIER DETAILS
        // --------------------------------

        JPanel formPanel = new JPanel(null);
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new LineBorder(new Color(205, 215, 220)));
        formPanel.setBounds(30, 110, 770, 145);

        JLabel detailsLabel = new JLabel("Cashier Details");
        detailsLabel.setFont(new Font("Arial", Font.BOLD, 17));
        detailsLabel.setForeground(new Color(45, 105, 135));
        detailsLabel.setBounds(20, 10, 200, 25);
        formPanel.add(detailsLabel);

        // Full name
        JLabel fullNameLabel = new JLabel("Full Name:");
        fullNameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        fullNameLabel.setBounds(20, 50, 100, 25);
        formPanel.add(fullNameLabel);

        fullNameField = new JTextField();
        fullNameField.setBounds(120, 50, 230, 28);
        formPanel.add(fullNameField);

        // Username
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        usernameLabel.setBounds(20, 95, 100, 25);
        formPanel.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(120, 95, 230, 28);
        formPanel.add(usernameField);

        // Password
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 12));
        passwordLabel.setBounds(400, 50, 100, 25);
        formPanel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(500, 50, 230, 28);
        formPanel.add(passwordField);

        // Role
        JLabel roleTitleLabel = new JLabel("Role:");
        roleTitleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        roleTitleLabel.setBounds(400, 95, 100, 25);
        formPanel.add(roleTitleLabel);

        JLabel roleLabel = new JLabel("Cashier");
        roleLabel.setFont(new Font("Arial", Font.BOLD, 13));
        roleLabel.setForeground(new Color(45, 105, 135));
        roleLabel.setBounds(500, 95, 150, 25);
        formPanel.add(roleLabel);

        panel.add(formPanel);

        
        // ACTION BUTTONS
        // --------------------------------

        JButton addButton = createButton(
                "Add Cashier",
                new Color(45, 105, 135)
        );
        addButton.setBounds(70, 280, 165, 36);
        panel.add(addButton);

        JButton updateButton = createButton(
                "Update Cashier",
                new Color(65, 125, 155)
        );
        updateButton.setBounds(250, 280, 165, 36);
        panel.add(updateButton);

        JButton deleteButton = createButton(
                "Delete Cashier",
                new Color(180, 70, 70)
        );
        deleteButton.setBounds(430, 280, 165, 36);
        panel.add(deleteButton);

        JButton clearButton = createButton(
                "Clear Fields",
                new Color(100, 110, 120)
        );
        clearButton.setBounds(610, 280, 165, 36);
        panel.add(clearButton);

        
        // CASHIER TABLE
        // --------------------------------

        JLabel tableTitle = new JLabel("Cashier Accounts");
        tableTitle.setFont(new Font("Arial", Font.BOLD, 17));
        tableTitle.setForeground(new Color(45, 105, 135));
        tableTitle.setBounds(30, 345, 250, 25);
        panel.add(tableTitle);

        tableModel = new DefaultTableModel();

        tableModel.setColumnIdentifiers(new String[]{
            "User ID", "Username", "Full Name", "Role"
        });

        userTable = new JTable(tableModel);
        userTable.setFont(new Font("Arial", Font.PLAIN, 12));
        userTable.setRowHeight(25);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        userTable.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 12)
        );

        userTable.getTableHeader().setBackground(
                new Color(225, 238, 245)
        );

        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBounds(30, 380, 770, 135);
        scrollPane.setBorder(
                new LineBorder(new Color(190, 205, 215))
        );
        panel.add(scrollPane);

        
        // BUTTON ACTIONS
        // --------------------------------

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

    // Create consistent buttons
    private JButton createButton(String text, Color colour) {

        JButton button = new JButton(text);

        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(colour);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
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