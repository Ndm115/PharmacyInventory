package pharmacyinventory;

import javax.swing.*;
import javax.swing.border.LineBorder;
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

        // Window settings
        setTitle("HealthFirst Pharmacy - Manage Medicines");
        setSize(1000, 680);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(240, 248, 252));

        
        // HEADER
        // --------------------------------

        JPanel headerPanel = new JPanel(null);
        headerPanel.setBackground(new Color(45, 105, 135));
        headerPanel.setBounds(0, 0, 1000, 90);

        JLabel titleLabel = new JLabel("Medicine Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 25));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(30, 17, 350, 35);
        headerPanel.add(titleLabel);

        JLabel subtitleLabel = new JLabel(
                "Add, update and manage pharmacy medicines"
        );
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(225, 240, 248));
        subtitleLabel.setBounds(30, 50, 400, 25);
        headerPanel.add(subtitleLabel);

        panel.add(headerPanel);

        
        // MEDICINE DETAILS CARD
        // --------------------------------

        JPanel formPanel = new JPanel(null);
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new LineBorder(new Color(205, 215, 220)));
        formPanel.setBounds(30, 110, 920, 180);

        JLabel detailsLabel = new JLabel("Medicine Details");
        detailsLabel.setFont(new Font("Arial", Font.BOLD, 17));
        detailsLabel.setForeground(new Color(45, 105, 135));
        detailsLabel.setBounds(20, 10, 200, 25);
        formPanel.add(detailsLabel);

        // Medicine name
        JLabel nameLabel = new JLabel("Medicine Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        nameLabel.setBounds(20, 50, 110, 25);
        formPanel.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(130, 50, 160, 28);
        formPanel.add(nameField);

        // Company
        JLabel companyLabel = new JLabel("Company:");
        companyLabel.setFont(new Font("Arial", Font.BOLD, 12));
        companyLabel.setBounds(20, 95, 110, 25);
        formPanel.add(companyLabel);

        companyField = new JTextField();
        companyField.setBounds(130, 95, 160, 28);
        formPanel.add(companyField);

        // Type
        JLabel typeLabel = new JLabel("Type:");
        typeLabel.setFont(new Font("Arial", Font.BOLD, 12));
        typeLabel.setBounds(320, 50, 100, 25);
        formPanel.add(typeLabel);

        typeBox = new JComboBox<>(
                new String[]{"Tablet", "Capsule", "Syrup",
                    "Injection", "Cream"}
        );
        typeBox.setBounds(420, 50, 150, 28);
        formPanel.add(typeBox);

        // Price
        JLabel priceLabel = new JLabel("Price (R):");
        priceLabel.setFont(new Font("Arial", Font.BOLD, 12));
        priceLabel.setBounds(320, 95, 100, 25);
        formPanel.add(priceLabel);

        priceField = new JTextField();
        priceField.setBounds(420, 95, 150, 28);
        formPanel.add(priceField);

        // Stock
        JLabel stockLabel = new JLabel("Stock Quantity:");
        stockLabel.setFont(new Font("Arial", Font.BOLD, 12));
        stockLabel.setBounds(600, 50, 110, 25);
        formPanel.add(stockLabel);

        stockField = new JTextField();
        stockField.setBounds(710, 50, 170, 28);
        formPanel.add(stockField);

        // Reorder
        JLabel reorderLabel = new JLabel("Reorder Level:");
        reorderLabel.setFont(new Font("Arial", Font.BOLD, 12));
        reorderLabel.setBounds(600, 95, 110, 25);
        formPanel.add(reorderLabel);

        reorderField = new JTextField();
        reorderField.setBounds(710, 95, 170, 28);
        formPanel.add(reorderField);

        // Expiry
        JLabel expiryLabel = new JLabel("Expiry Date:");
        expiryLabel.setFont(new Font("Arial", Font.BOLD, 12));
        expiryLabel.setBounds(20, 140, 110, 25);
        formPanel.add(expiryLabel);

        expiryField = new JTextField();
        expiryField.setBounds(130, 140, 160, 28);
        expiryField.setToolTipText("YYYY-MM-DD");
        formPanel.add(expiryField);

        // Supplier
        JLabel supplierLabel = new JLabel("Supplier:");
        supplierLabel.setFont(new Font("Arial", Font.BOLD, 12));
        supplierLabel.setBounds(320, 140, 100, 25);
        formPanel.add(supplierLabel);

        supplierBox = new JComboBox<>();
        supplierBox.setBounds(420, 140, 150, 28);
        formPanel.add(supplierBox);

        JLabel dateHint = new JLabel("Format: YYYY-MM-DD");
        dateHint.setFont(new Font("Arial", Font.ITALIC, 11));
        dateHint.setForeground(Color.GRAY);
        dateHint.setBounds(600, 140, 180, 25);
        formPanel.add(dateHint);

        panel.add(formPanel);

        
        // ACTION BUTTONS
        // --------------------------------

        JButton addButton = createButton(
                "Add Medicine",
                new Color(45, 105, 135)
        );
        addButton.setBounds(80, 310, 170, 36);
        panel.add(addButton);

        JButton updateButton = createButton(
                "Update Medicine",
                new Color(65, 125, 155)
        );
        updateButton.setBounds(270, 310, 170, 36);
        panel.add(updateButton);

        JButton deleteButton = createButton(
                "Delete Medicine",
                new Color(180, 70, 70)
        );
        deleteButton.setBounds(460, 310, 170, 36);
        panel.add(deleteButton);

        JButton clearButton = createButton(
                "Clear Fields",
                new Color(100, 110, 120)
        );
        clearButton.setBounds(650, 310, 170, 36);
        panel.add(clearButton);

        
        // MEDICINE TABLE
        // --------------------------------

        JLabel tableTitle = new JLabel("Medicine Inventory");
        tableTitle.setFont(new Font("Arial", Font.BOLD, 17));
        tableTitle.setForeground(new Color(45, 105, 135));
        tableTitle.setBounds(30, 365, 250, 25);
        panel.add(tableTitle);

        tableModel = new DefaultTableModel();

        tableModel.setColumnIdentifiers(new String[]{
            "ID", "Name", "Company", "Type",
            "Price", "Stock", "Reorder", "Expiry", "Supplier ID"
        });

        medicineTable = new JTable(tableModel);

        medicineTable.setFont(new Font("Arial", Font.PLAIN, 12));
        medicineTable.setRowHeight(25);
        medicineTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        medicineTable.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 12)
        );
        medicineTable.getTableHeader().setBackground(
                new Color(225, 238, 245)
        );

        JScrollPane scrollPane = new JScrollPane(medicineTable);
        scrollPane.setBounds(30, 400, 920, 210);
        scrollPane.setBorder(
                new LineBorder(new Color(190, 205, 215))
        );
        panel.add(scrollPane);

        
        // BUTTON ACTIONS
        //------------------------

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