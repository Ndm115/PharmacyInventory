package pharmacyinventory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class MedicineManagement extends JFrame {

    private JTextField nameField;
    private JTextField companyField;
    private JComboBox<String> typeBox;
    private JTextField priceField;
    private JTextField stockField;
    private JTextField reorderField;
    private JTextField expiryField;
    private JComboBox<String> supplierBox;

    private JTable medicineTable;
    private DefaultTableModel tableModel;

    public MedicineManagement() {

        setTitle("HealthFirst Pharmacy - Manage Medicines");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(240, 248, 255));

        JLabel titleLabel = new JLabel("Manage Medicines");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 25));
        titleLabel.setBounds(30, 20, 300, 35);
        panel.add(titleLabel);

        // Medicine name
        JLabel nameLabel = new JLabel("Medicine Name:");
        nameLabel.setBounds(30, 80, 120, 25);
        panel.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(150, 80, 200, 28);
        panel.add(nameField);

        // Company
        JLabel companyLabel = new JLabel("Company:");
        companyLabel.setBounds(30, 120, 120, 25);
        panel.add(companyLabel);

        companyField = new JTextField();
        companyField.setBounds(150, 120, 200, 28);
        panel.add(companyField);

        // Medicine type
        JLabel typeLabel = new JLabel("Type:");
        typeLabel.setBounds(30, 160, 120, 25);
        panel.add(typeLabel);

        typeBox = new JComboBox<>(
                new String[]{"Tablet", "Capsule", "Syrup",
                    "Injection", "Cream"}
        );
        typeBox.setBounds(150, 160, 200, 28);
        panel.add(typeBox);

        // Price
        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setBounds(400, 80, 120, 25);
        panel.add(priceLabel);

        priceField = new JTextField();
        priceField.setBounds(520, 80, 180, 28);
        panel.add(priceField);

        // Stock
        JLabel stockLabel = new JLabel("Stock Quantity:");
        stockLabel.setBounds(400, 120, 120, 25);
        panel.add(stockLabel);

        stockField = new JTextField();
        stockField.setBounds(520, 120, 180, 28);
        panel.add(stockField);

        // Reorder level
        JLabel reorderLabel = new JLabel("Reorder Level:");
        reorderLabel.setBounds(400, 160, 120, 25);
        panel.add(reorderLabel);

        reorderField = new JTextField();
        reorderField.setBounds(520, 160, 180, 28);
        panel.add(reorderField);

        // Expiry date
        JLabel expiryLabel = new JLabel("Expiry (YYYY-MM-DD):");
        expiryLabel.setBounds(730, 80, 150, 25);
        panel.add(expiryLabel);

        expiryField = new JTextField();
        expiryField.setBounds(730, 110, 200, 28);
        panel.add(expiryField);

        // Supplier
        JLabel supplierLabel = new JLabel("Supplier:");
        supplierLabel.setBounds(730, 150, 100, 25);
        panel.add(supplierLabel);

        supplierBox = new JComboBox<>();
        supplierBox.setBounds(730, 180, 200, 28);
        panel.add(supplierBox);

        // Buttons
        JButton addButton = new JButton("Add Medicine");
        addButton.setBounds(80, 230, 170, 35);
        panel.add(addButton);

        JButton updateButton = new JButton("Update Medicine");
        updateButton.setBounds(270, 230, 170, 35);
        panel.add(updateButton);

        JButton deleteButton = new JButton("Delete Medicine");
        deleteButton.setBounds(460, 230, 170, 35);
        panel.add(deleteButton);

        JButton clearButton = new JButton("Clear");
        clearButton.setBounds(650, 230, 120, 35);
        panel.add(clearButton);

        // Medicine table
        tableModel = new DefaultTableModel();

        tableModel.setColumnIdentifiers(new String[]{
            "ID", "Name", "Company", "Type",
            "Price", "Stock", "Reorder", "Expiry", "Supplier ID"
        });

        medicineTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(medicineTable);
        scrollPane.setBounds(30, 290, 920, 280);
        panel.add(scrollPane);

        // Button actions
        addButton.addActionListener(e -> addMedicine());
        updateButton.addActionListener(e -> updateMedicine());
        deleteButton.addActionListener(e -> deleteMedicine());
        clearButton.addActionListener(e -> clearFields());

        // When a table row is clicked
        medicineTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillFieldsFromTable();
            }
        });

        add(panel);

        loadSuppliers();
        loadMedicines();
    }

    // Load suppliers into the dropdown
    private void loadSuppliers() {

        supplierBox.removeAllItems();

        String sql = "SELECT supplier_id, name FROM suppliers";

        try (
                Connection connection = DatabaseConnection.getConnection();
                Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery(sql)) {

            while (result.next()) {

                supplierBox.addItem(
                        result.getInt("supplier_id")
                        + " - "
                        + result.getString("name")
                );
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Could not load suppliers.");
        }
    }

    // Load medicines from database into JTable
    private void loadMedicines() {

        tableModel.setRowCount(0);

        String sql = "SELECT * FROM medicines";

        try (
                Connection connection = DatabaseConnection.getConnection();
                Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery(sql)) {

            while (result.next()) {

                tableModel.addRow(new Object[]{
                    result.getInt("medicine_id"),
                    result.getString("name"),
                    result.getString("company"),
                    result.getString("medicine_type"),
                    result.getDouble("price"),
                    result.getInt("quantity_in_stock"),
                    result.getInt("reorder_level"),
                    result.getDate("expiry_date"),
                    result.getInt("supplier_id")
                });
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(this,
                    "Could not load medicines.");
        }
    }

    // Add new medicine
    private void addMedicine() {

        if (!fieldsValid()) {
            return;
        }

        String sql = "INSERT INTO medicines "
                + "(name, company, medicine_type, price, "
                + "quantity_in_stock, reorder_level, expiry_date, supplier_id) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, nameField.getText());
            statement.setString(2, companyField.getText());
            statement.setString(3, typeBox.getSelectedItem().toString());
            statement.setDouble(4, Double.parseDouble(priceField.getText()));
            statement.setInt(5, Integer.parseInt(stockField.getText()));
            statement.setInt(6, Integer.parseInt(reorderField.getText()));
            statement.setDate(7, Date.valueOf(expiryField.getText()));
            statement.setInt(8, getSelectedSupplierID());

            statement.executeUpdate();

            JOptionPane.showMessageDialog(this,
                    "Medicine added successfully.");

            loadMedicines();
            clearFields();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this,
                    "Could not add medicine. Check the entered values.");
        }
    }

    // Update selected medicine
    private void updateMedicine() {

        int row = medicineTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a medicine to update.");
            return;
        }

        if (!fieldsValid()) {
            return;
        }

        int medicineID = Integer.parseInt(
                tableModel.getValueAt(row, 0).toString()
        );

        String sql = "UPDATE medicines SET "
                + "name=?, company=?, medicine_type=?, price=?, "
                + "quantity_in_stock=?, reorder_level=?, "
                + "expiry_date=?, supplier_id=? "
                + "WHERE medicine_id=?";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, nameField.getText());
            statement.setString(2, companyField.getText());
            statement.setString(3, typeBox.getSelectedItem().toString());
            statement.setDouble(4, Double.parseDouble(priceField.getText()));
            statement.setInt(5, Integer.parseInt(stockField.getText()));
            statement.setInt(6, Integer.parseInt(reorderField.getText()));
            statement.setDate(7, Date.valueOf(expiryField.getText()));
            statement.setInt(8, getSelectedSupplierID());
            statement.setInt(9, medicineID);

            statement.executeUpdate();

            JOptionPane.showMessageDialog(this,
                    "Medicine updated successfully.");

            loadMedicines();
            clearFields();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this,
                    "Could not update medicine.");
        }
    }

    // Delete selected medicine
    private void deleteMedicine() {

        int row = medicineTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a medicine to delete.");
            return;
        }

        int answer = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this medicine?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (answer != JOptionPane.YES_OPTION) {
            return;
        }

        int medicineID = Integer.parseInt(
                tableModel.getValueAt(row, 0).toString()
        );

        String sql = "DELETE FROM medicines WHERE medicine_id=?";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, medicineID);
            statement.executeUpdate();

            JOptionPane.showMessageDialog(this,
                    "Medicine deleted successfully.");

            loadMedicines();
            clearFields();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(this,
                    "Could not delete medicine.");
        }
    }

    // Put selected table row into the fields
    private void fillFieldsFromTable() {

        int row = medicineTable.getSelectedRow();

        if (row == -1) {
            return;
        }

        nameField.setText(tableModel.getValueAt(row, 1).toString());
        companyField.setText(tableModel.getValueAt(row, 2).toString());
        typeBox.setSelectedItem(tableModel.getValueAt(row, 3).toString());
        priceField.setText(tableModel.getValueAt(row, 4).toString());
        stockField.setText(tableModel.getValueAt(row, 5).toString());
        reorderField.setText(tableModel.getValueAt(row, 6).toString());
        expiryField.setText(tableModel.getValueAt(row, 7).toString());

        int supplierID = Integer.parseInt(
                tableModel.getValueAt(row, 8).toString()
        );

        // Find matching supplier in dropdown
        for (int i = 0; i < supplierBox.getItemCount(); i++) {

            if (supplierBox.getItemAt(i).startsWith(supplierID + " -")) {
                supplierBox.setSelectedIndex(i);
                break;
            }
        }
    }

    // Get supplier ID from dropdown text
    private int getSelectedSupplierID() {

        String supplier = supplierBox.getSelectedItem().toString();

        return Integer.parseInt(
                supplier.substring(0, supplier.indexOf(" "))
        );
    }

    // Basic input validation
    private boolean fieldsValid() {

        if (nameField.getText().isEmpty()
                || companyField.getText().isEmpty()
                || priceField.getText().isEmpty()
                || stockField.getText().isEmpty()
                || reorderField.getText().isEmpty()
                || expiryField.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Please complete all medicine fields.");

            return false;
        }

        return true;
    }

    // Clear input fields
    private void clearFields() {

        nameField.setText("");
        companyField.setText("");
        priceField.setText("");
        stockField.setText("");
        reorderField.setText("");
        expiryField.setText("");

        typeBox.setSelectedIndex(0);

        if (supplierBox.getItemCount() > 0) {
            supplierBox.setSelectedIndex(0);
        }

        medicineTable.clearSelection();
    }
}