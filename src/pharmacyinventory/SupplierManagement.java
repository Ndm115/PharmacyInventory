package pharmacyinventory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class SupplierManagement extends JFrame {

    private JTextField nameField;
    private JTextField contactField;
    private JTextField phoneField;
    private JTextField emailField;
    private JTextField addressField;

    private JTable supplierTable;
    private DefaultTableModel tableModel;

    public SupplierManagement() {

        setTitle("HealthFirst Pharmacy - Manage Suppliers");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(240, 248, 255));

        JLabel titleLabel = new JLabel("Manage Suppliers");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 25));
        titleLabel.setBounds(30, 20, 300, 35);
        panel.add(titleLabel);

        // Supplier name
        JLabel nameLabel = new JLabel("Supplier Name:");
        nameLabel.setBounds(30, 80, 120, 25);
        panel.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(150, 80, 220, 28);
        panel.add(nameField);

        // Contact person
        JLabel contactLabel = new JLabel("Contact Person:");
        contactLabel.setBounds(30, 120, 120, 25);
        panel.add(contactLabel);

        contactField = new JTextField();
        contactField.setBounds(150, 120, 220, 28);
        panel.add(contactField);

        // Phone
        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setBounds(450, 80, 100, 25);
        panel.add(phoneLabel);

        phoneField = new JTextField();
        phoneField.setBounds(550, 80, 250, 28);
        panel.add(phoneField);

        // Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(450, 120, 100, 25);
        panel.add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(550, 120, 250, 28);
        panel.add(emailField);

        // Address
        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setBounds(30, 160, 120, 25);
        panel.add(addressLabel);

        addressField = new JTextField();
        addressField.setBounds(150, 160, 650, 28);
        panel.add(addressField);

        // Buttons
        JButton addButton = new JButton("Add Supplier");
        addButton.setBounds(80, 215, 160, 35);
        panel.add(addButton);

        JButton updateButton = new JButton("Update Supplier");
        updateButton.setBounds(260, 215, 160, 35);
        panel.add(updateButton);

        JButton deleteButton = new JButton("Delete Supplier");
        deleteButton.setBounds(440, 215, 160, 35);
        panel.add(deleteButton);

        JButton clearButton = new JButton("Clear");
        clearButton.setBounds(620, 215, 120, 35);
        panel.add(clearButton);

        // Table
        tableModel = new DefaultTableModel();

        tableModel.setColumnIdentifiers(new String[]{
            "ID", "Supplier Name", "Contact Person",
            "Phone", "Email", "Address"
        });

        supplierTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(supplierTable);
        scrollPane.setBounds(30, 280, 820, 230);
        panel.add(scrollPane);

        // Button actions
        addButton.addActionListener(e -> addSupplier());
        updateButton.addActionListener(e -> updateSupplier());
        deleteButton.addActionListener(e -> deleteSupplier());
        clearButton.addActionListener(e -> clearFields());

        // Fill fields when row selected
        supplierTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillFieldsFromTable();
            }
        });

        add(panel);

        loadSuppliers();
    }

    // Load all suppliers
    private void loadSuppliers() {

        tableModel.setRowCount(0);

        String sql = "SELECT * FROM suppliers";

        try (
                Connection connection = DatabaseConnection.getConnection();
                Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery(sql)) {

            while (result.next()) {

                tableModel.addRow(new Object[]{
                    result.getInt("supplier_id"),
                    result.getString("name"),
                    result.getString("contact_person"),
                    result.getString("phone"),
                    result.getString("email"),
                    result.getString("address")
                });
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load suppliers."
            );
        }
    }

    // Add supplier
    private void addSupplier() {

        if (!fieldsValid()) {
            return;
        }

        String sql = "INSERT INTO suppliers "
                + "(name, contact_person, phone, email, address) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, nameField.getText());
            statement.setString(2, contactField.getText());
            statement.setString(3, phoneField.getText());
            statement.setString(4, emailField.getText());
            statement.setString(5, addressField.getText());

            statement.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Supplier added successfully."
            );

            loadSuppliers();
            clearFields();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not add supplier."
            );
        }
    }

    // Update supplier
    private void updateSupplier() {

        int row = supplierTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a supplier to update."
            );

            return;
        }

        if (!fieldsValid()) {
            return;
        }

        int supplierID = Integer.parseInt(
                tableModel.getValueAt(row, 0).toString()
        );

        String sql = "UPDATE suppliers SET "
                + "name=?, contact_person=?, phone=?, email=?, address=? "
                + "WHERE supplier_id=?";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, nameField.getText());
            statement.setString(2, contactField.getText());
            statement.setString(3, phoneField.getText());
            statement.setString(4, emailField.getText());
            statement.setString(5, addressField.getText());
            statement.setInt(6, supplierID);

            statement.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Supplier updated successfully."
            );

            loadSuppliers();
            clearFields();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not update supplier."
            );
        }
    }

    // Delete supplier
    private void deleteSupplier() {

        int row = supplierTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a supplier to delete."
            );

            return;
        }

        int answer = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this supplier?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (answer != JOptionPane.YES_OPTION) {
            return;
        }

        int supplierID = Integer.parseInt(
                tableModel.getValueAt(row, 0).toString()
        );

        String sql = "DELETE FROM suppliers WHERE supplier_id=?";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, supplierID);
            statement.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Supplier deleted successfully."
            );

            loadSuppliers();
            clearFields();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not delete supplier. "
                    + "The supplier may still have medicines linked to it."
            );
        }
    }

    // Fill fields from selected row
    private void fillFieldsFromTable() {

        int row = supplierTable.getSelectedRow();

        if (row == -1) {
            return;
        }

        nameField.setText(tableModel.getValueAt(row, 1).toString());
        contactField.setText(tableModel.getValueAt(row, 2).toString());
        phoneField.setText(tableModel.getValueAt(row, 3).toString());
        emailField.setText(tableModel.getValueAt(row, 4).toString());
        addressField.setText(tableModel.getValueAt(row, 5).toString());
    }

    // Basic validation
    private boolean fieldsValid() {

        if (nameField.getText().isEmpty()
                || contactField.getText().isEmpty()
                || phoneField.getText().isEmpty()
                || emailField.getText().isEmpty()
                || addressField.getText().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please complete all supplier fields."
            );

            return false;
        }

        return true;
    }

    // Clear fields
    private void clearFields() {

        nameField.setText("");
        contactField.setText("");
        phoneField.setText("");
        emailField.setText("");
        addressField.setText("");

        supplierTable.clearSelection();
    }
}