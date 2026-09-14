package pharmacyinventory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CashierDashboard extends JFrame {

    private JTable medicineTable;
    private JTable cartTable;

    private DefaultTableModel medicineModel;
    private DefaultTableModel cartModel;

    private JSpinner quantitySpinner;
    private JLabel totalLabel;

    private double total = 0.00;
    private int cashierID;
    private String cashierName;

    public CashierDashboard(int cashierID, String cashierName) {

        this.cashierID = cashierID;
        this.cashierName = cashierName;

        // Window settings
        setTitle("HealthFirst Pharmacy - Cashier POS");
        setSize(1050, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(240, 248, 252));

        
        // HEADER
        // --------------------------------

        JPanel headerPanel = new JPanel(null);
        headerPanel.setBackground(new Color(45, 105, 135));
        headerPanel.setBounds(0, 0, 1050, 90);

        JLabel titleLabel = new JLabel("Point of Sale");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 25));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(30, 17, 300, 35);
        headerPanel.add(titleLabel);

        JLabel cashierLabel = new JLabel("Cashier: " + cashierName);
        cashierLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        cashierLabel.setForeground(new Color(225, 240, 248));
        cashierLabel.setBounds(30, 50, 350, 25);
        headerPanel.add(cashierLabel);

        JLabel pharmacyLabel = new JLabel(
                "HealthFirst Pharmacy",
                SwingConstants.RIGHT
        );
        pharmacyLabel.setFont(new Font("Arial", Font.BOLD, 15));
        pharmacyLabel.setForeground(Color.WHITE);
        pharmacyLabel.setBounds(750, 30, 250, 30);
        headerPanel.add(pharmacyLabel);

        panel.add(headerPanel);

        
        // AVAILABLE MEDICINES
        // --------------------------------

        JLabel medicineLabel = new JLabel("Available Medicines");
        medicineLabel.setFont(new Font("Arial", Font.BOLD, 17));
        medicineLabel.setForeground(new Color(45, 105, 135));
        medicineLabel.setBounds(30, 110, 250, 25);
        panel.add(medicineLabel);

        medicineModel = new DefaultTableModel();

        medicineModel.setColumnIdentifiers(new String[]{
            "ID", "Medicine", "Type", "Price", "Available Stock"
        });

        medicineTable = new JTable(medicineModel);
        medicineTable.setFont(new Font("Arial", Font.PLAIN, 12));
        medicineTable.setRowHeight(25);
        medicineTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        medicineTable.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 12)
        );

        medicineTable.getTableHeader().setBackground(
                new Color(225, 238, 245)
        );

        JScrollPane medicineScroll = new JScrollPane(medicineTable);
        medicineScroll.setBounds(30, 145, 650, 190);
        medicineScroll.setBorder(
                BorderFactory.createLineBorder(
                        new Color(190, 205, 215)
                )
        );
        panel.add(medicineScroll);

        
        // SALE OPTIONS
        // --------------------------------

        JPanel optionsPanel = new JPanel(null);
        optionsPanel.setBackground(Color.WHITE);
        optionsPanel.setBorder(
                BorderFactory.createLineBorder(
                        new Color(205, 215, 220)
                )
        );
        optionsPanel.setBounds(710, 145, 290, 190);

        JLabel optionsTitle = new JLabel("Sale Options");
        optionsTitle.setFont(new Font("Arial", Font.BOLD, 16));
        optionsTitle.setForeground(new Color(45, 105, 135));
        optionsTitle.setBounds(20, 15, 180, 25);
        optionsPanel.add(optionsTitle);

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setFont(new Font("Arial", Font.BOLD, 12));
        quantityLabel.setBounds(20, 55, 80, 30);
        optionsPanel.add(quantityLabel);

        quantitySpinner = new JSpinner(
                new SpinnerNumberModel(1, 1, 100, 1)
        );
        quantitySpinner.setBounds(105, 55, 145, 30);
        optionsPanel.add(quantitySpinner);

        JButton addCartButton = createPOSButton(
                "Add to Cart",
                new Color(45, 105, 135)
        );
        addCartButton.setBounds(20, 105, 230, 32);
        optionsPanel.add(addCartButton);

        JButton stockCheckButton = createPOSButton(
                "Check Stock",
                new Color(65, 125, 155)
        );
        stockCheckButton.setBounds(20, 145, 230, 32);
        optionsPanel.add(stockCheckButton);

        panel.add(optionsPanel);

        
        // SHOPPING CART
        // --------------------------------

        JLabel cartLabel = new JLabel("Shopping Cart");
        cartLabel.setFont(new Font("Arial", Font.BOLD, 17));
        cartLabel.setForeground(new Color(45, 105, 135));
        cartLabel.setBounds(30, 360, 200, 25);
        panel.add(cartLabel);

        cartModel = new DefaultTableModel();

        cartModel.setColumnIdentifiers(new String[]{
            "Medicine ID", "Medicine", "Price", "Quantity", "Subtotal"
        });

        cartTable = new JTable(cartModel);
        cartTable.setFont(new Font("Arial", Font.PLAIN, 12));
        cartTable.setRowHeight(25);
        cartTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        cartTable.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 12)
        );

        cartTable.getTableHeader().setBackground(
                new Color(225, 238, 245)
        );

        JScrollPane cartScroll = new JScrollPane(cartTable);
        cartScroll.setBounds(30, 395, 650, 200);
        cartScroll.setBorder(
                BorderFactory.createLineBorder(
                        new Color(190, 205, 215)
                )
        );
        panel.add(cartScroll);

        
        // SALE SUMMARY
        // --------------------------------

        JPanel checkoutPanel = new JPanel(null);
        checkoutPanel.setBackground(Color.WHITE);
        checkoutPanel.setBorder(
                BorderFactory.createLineBorder(
                        new Color(205, 215, 220)
                )
        );
        checkoutPanel.setBounds(710, 395, 290, 200);

        JLabel summaryLabel = new JLabel("Sale Summary");
        summaryLabel.setFont(new Font("Arial", Font.BOLD, 16));
        summaryLabel.setForeground(new Color(45, 105, 135));
        summaryLabel.setBounds(20, 15, 180, 25);
        checkoutPanel.add(summaryLabel);

        totalLabel = new JLabel("Total: R0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 22));
        totalLabel.setForeground(new Color(45, 105, 135));
        totalLabel.setBounds(20, 50, 240, 35);
        checkoutPanel.add(totalLabel);

        JButton checkoutButton = createPOSButton(
                "Checkout",
                new Color(45, 105, 135)
        );
        checkoutButton.setBounds(20, 95, 230, 32);
        checkoutPanel.add(checkoutButton);

        JButton clearCartButton = createPOSButton(
                "Clear Cart",
                new Color(100, 110, 120)
        );
        clearCartButton.setBounds(20, 135, 110, 32);
        checkoutPanel.add(clearCartButton);

        JButton logoutButton = createPOSButton(
                "Logout",
                new Color(180, 70, 70)
        );
        logoutButton.setBounds(140, 135, 110, 32);
        checkoutPanel.add(logoutButton);

        panel.add(checkoutPanel);

        
        // BUTTON ACTIONS
        // --------------------------------

        addCartButton.addActionListener(e -> addToCart());
        stockCheckButton.addActionListener(e -> checkStock());
        clearCartButton.addActionListener(e -> clearCart());
        checkoutButton.addActionListener(e -> checkout());

        logoutButton.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        add(panel);

        // Load medicines when screen opens
        loadMedicines();
    }

    // Create consistent POS buttons
    private JButton createPOSButton(String text, Color colour) {

        JButton button = new JButton(text);

        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(colour);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    
    // LOAD MEDICINES
    // --------------------------------

    private void loadMedicines() {

        medicineModel.setRowCount(0);

        String sql
                = "SELECT medicine_id, name, medicine_type, "
                + "price, quantity_in_stock FROM medicines";

        try (
                Connection connection = DatabaseConnection.getConnection();
                Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery(sql)
        ) {

            while (result.next()) {

                medicineModel.addRow(new Object[]{
                    result.getInt("medicine_id"),
                    result.getString("name"),
                    result.getString("medicine_type"),
                    result.getDouble("price"),
                    result.getInt("quantity_in_stock")
                });
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load medicines."
            );
        }
    }

    
    // ADD MEDICINE TO CART
    // --------------------------------

    private void addToCart() {

        int row = medicineTable.getSelectedRow();

        // Make sure medicine is selected
        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a medicine."
            );

            return;
        }

        int medicineID = Integer.parseInt(
                medicineModel.getValueAt(row, 0).toString()
        );

        String medicineName
                = medicineModel.getValueAt(row, 1).toString();

        double price = Double.parseDouble(
                medicineModel.getValueAt(row, 3).toString()
        );

        int availableStock = Integer.parseInt(
                medicineModel.getValueAt(row, 4).toString()
        );

        int quantity = (Integer) quantitySpinner.getValue();

        // Prevent selling more stock than available
        if (quantity > availableStock) {

            JOptionPane.showMessageDialog(
                    this,
                    "Not enough stock available."
            );

            return;
        }

        double subtotal = price * quantity;

        // Add item to cart
        cartModel.addRow(new Object[]{
            medicineID,
            medicineName,
            price,
            quantity,
            subtotal
        });

        // Update total
        total += subtotal;

        totalLabel.setText(
                String.format("Total: R%.2f", total)
        );
    }

    
    // CHECK MEDICINE STOCK
    // --------------------------------

    private void checkStock() {

        int row = medicineTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a medicine."
            );

            return;
        }

        String name
                = medicineModel.getValueAt(row, 1).toString();

        double price = Double.parseDouble(
                medicineModel.getValueAt(row, 3).toString()
        );

        int stock = Integer.parseInt(
                medicineModel.getValueAt(row, 4).toString()
        );

        JOptionPane.showMessageDialog(
                this,
                "Medicine: " + name
                + "\nPrice: R" + String.format("%.2f", price)
                + "\nAvailable Stock: " + stock,
                "Stock Check",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    
    // CLEAR CART
    // --------------------------------

    private void clearCart() {

        cartModel.setRowCount(0);

        total = 0.00;

        totalLabel.setText("Total: R0.00");

        quantitySpinner.setValue(1);
    }

    
    // CHECKOUT
    // --------------------------------

    private void checkout() {

        // Make sure cart is not empty
        if (cartModel.getRowCount() == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "The cart is empty."
            );

            return;
        }

        Connection connection = null;

        try {

            connection = DatabaseConnection.getConnection();

            // Start database transaction
            connection.setAutoCommit(false);

            
            // SAVE SALE
            // --------------------------------

            String saleSQL
                    = "INSERT INTO sales (total_amount, user_id) "
                    + "VALUES (?, ?)";

            PreparedStatement saleStatement
                    = connection.prepareStatement(
                            saleSQL,
                            Statement.RETURN_GENERATED_KEYS
                    );

            saleStatement.setDouble(1, total);
            saleStatement.setInt(2, cashierID);

            saleStatement.executeUpdate();

            // Get new Sale ID
            ResultSet generatedKeys
                    = saleStatement.getGeneratedKeys();

            if (!generatedKeys.next()) {

                throw new SQLException(
                        "Could not create sale."
                );
            }

            int saleID = generatedKeys.getInt(1);

            
            // SAVE SALE ITEMS
            // --------------------------------

            String itemSQL
                    = "INSERT INTO sale_items "
                    + "(sale_id, medicine_id, quantity_sold, price_at_sale) "
                    + "VALUES (?, ?, ?, ?)";

            String stockSQL
                    = "UPDATE medicines "
                    + "SET quantity_in_stock = quantity_in_stock - ? "
                    + "WHERE medicine_id = ?";

            for (int i = 0;
                    i < cartModel.getRowCount();
                    i++) {

                int medicineID = Integer.parseInt(
                        cartModel.getValueAt(i, 0).toString()
                );

                double price = Double.parseDouble(
                        cartModel.getValueAt(i, 2).toString()
                );

                int quantity = Integer.parseInt(
                        cartModel.getValueAt(i, 3).toString()
                );

                // Save item
                PreparedStatement itemStatement
                        = connection.prepareStatement(itemSQL);

                itemStatement.setInt(1, saleID);
                itemStatement.setInt(2, medicineID);
                itemStatement.setInt(3, quantity);
                itemStatement.setDouble(4, price);

                itemStatement.executeUpdate();

                // Reduce medicine stock
                PreparedStatement stockStatement
                        = connection.prepareStatement(stockSQL);

                stockStatement.setInt(1, quantity);
                stockStatement.setInt(2, medicineID);

                stockStatement.executeUpdate();
            }

            
            // CREATE BILL
            // --------------------------------

            StringBuilder bill = new StringBuilder();

            bill.append("HEALTHFIRST PHARMACY\n");
            bill.append("==============================\n");

            bill.append("Sale ID: ")
                    .append(saleID)
                    .append("\n");

            bill.append("Cashier: ")
                    .append(cashierName)
                    .append("\n");

            bill.append("==============================\n\n");

            // Add cart items to bill
            for (int i = 0;
                    i < cartModel.getRowCount();
                    i++) {

                String medicineName
                        = cartModel.getValueAt(i, 1).toString();

                int quantity = Integer.parseInt(
                        cartModel.getValueAt(i, 3).toString()
                );

                double subtotal = Double.parseDouble(
                        cartModel.getValueAt(i, 4).toString()
                );

                bill.append(medicineName)
                        .append(" x ")
                        .append(quantity)
                        .append(" = R")
                        .append(
                                String.format(
                                        "%.2f",
                                        subtotal
                                )
                        )
                        .append("\n");
            }

            bill.append("\n==============================\n");

            bill.append(
                    String.format(
                            "TOTAL: R%.2f\n",
                            total
                    )
            );

            bill.append("==============================\n");

            bill.append("Thank you!");

            
            // COMPLETE TRANSACTION
            // --------------------------------

            connection.commit();

            // Show bill window
            showBillWindow(
                    bill.toString(),
                    saleID
            );

            // Clear cart
            clearCart();

            // Reload medicines to show new stock
            loadMedicines();

        } catch (SQLException e) {

            // Undo transaction if something failed
            if (connection != null) {

                try {

                    connection.rollback();

                } catch (SQLException rollbackError) {

                    rollbackError.printStackTrace();
                }
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Checkout failed: "
                    + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

        } finally {

            if (connection != null) {

                try {

                    connection.setAutoCommit(true);
                    connection.close();

                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
    }

    
    // BILL WINDOW
    // --------------------------------

    private void showBillWindow(
            String billText,
            int saleID) {

        JFrame billFrame
                = new JFrame("Generated Bill");

        billFrame.setSize(500, 550);

        billFrame.setLocationRelativeTo(this);

        billFrame.setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        JPanel panel
                = new JPanel(
                        new BorderLayout(10, 10)
                );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        );

        
        // BILL TEXT
        // --------------------------------

        JTextArea billArea
                = new JTextArea(billText);

        billArea.setFont(
                new Font(
                        "Monospaced",
                        Font.PLAIN,
                        14
                )
        );

        billArea.setEditable(false);

        JScrollPane scrollPane
                = new JScrollPane(billArea);

        panel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        
        // BILL BUTTONS
        // --------------------------------

        JPanel buttonPanel = new JPanel();

        JButton saveButton
                = new JButton("Save Bill");

        JButton printButton
                = new JButton("Print Bill");

        JButton closeButton
                = new JButton("Close");

        buttonPanel.add(saveButton);
        buttonPanel.add(printButton);
        buttonPanel.add(closeButton);

        panel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        
        // SAVE BILL
        // --------------------------------

        saveButton.addActionListener(e -> {

            JFileChooser fileChooser
                    = new JFileChooser();

            fileChooser.setSelectedFile(
                    new File(
                            "Bill_"
                            + saleID
                            + ".txt"
                    )
            );

            int result
                    = fileChooser.showSaveDialog(
                            billFrame
                    );

            if (result
                    == JFileChooser.APPROVE_OPTION) {

                File file
                        = fileChooser.getSelectedFile();

                try (
                        FileWriter writer
                        = new FileWriter(file)
                ) {

                    writer.write(billText);

                    JOptionPane.showMessageDialog(
                            billFrame,
                            "Bill saved successfully."
                    );

                } catch (IOException ex) {

                    JOptionPane.showMessageDialog(
                            billFrame,
                            "Could not save bill.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        
        // PRINT BILL
        // --------------------------------

        printButton.addActionListener(e -> {

            try {

                boolean printed
                        = billArea.print();

                if (printed) {

                    JOptionPane.showMessageDialog(
                            billFrame,
                            "Bill sent to printer."
                    );
                }

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        billFrame,
                        "Could not print bill.",
                        "Print Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        
        // CLOSE BILL
        // --------------------------------

        closeButton.addActionListener(e -> {

            billFrame.dispose();
        });

        billFrame.add(panel);

        billFrame.setVisible(true);
    }
}