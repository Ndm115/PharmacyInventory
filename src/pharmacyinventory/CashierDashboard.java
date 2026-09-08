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

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(240, 248, 255));

        
        // HEADING
        

        JLabel titleLabel = new JLabel("Point of Sale");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setBounds(30, 20, 250, 35);
        panel.add(titleLabel);

        JLabel cashierLabel = new JLabel("Cashier: " + cashierName);
        cashierLabel.setBounds(30, 55, 300, 25);
        panel.add(cashierLabel);

        
        // AVAILABLE MEDICINES
       

        JLabel medicineLabel = new JLabel("Available Medicines");
        medicineLabel.setFont(new Font("Arial", Font.BOLD, 16));
        medicineLabel.setBounds(30, 95, 200, 25);
        panel.add(medicineLabel);

        medicineModel = new DefaultTableModel();

        medicineModel.setColumnIdentifiers(new String[]{
            "ID", "Medicine", "Type", "Price", "Available Stock"
        });

        medicineTable = new JTable(medicineModel);

        JScrollPane medicineScroll = new JScrollPane(medicineTable);
        medicineScroll.setBounds(30, 125, 600, 190);
        panel.add(medicineScroll);

        // Quantity
        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setBounds(670, 140, 80, 30);
        panel.add(quantityLabel);

        quantitySpinner = new JSpinner(
                new SpinnerNumberModel(1, 1, 100, 1)
        );

        quantitySpinner.setBounds(750, 140, 100, 30);
        panel.add(quantitySpinner);

        JButton addCartButton = new JButton("Add to Cart");
        addCartButton.setBounds(670, 190, 180, 40);
        panel.add(addCartButton);

        JButton stockCheckButton = new JButton("Check Stock");
        stockCheckButton.setBounds(670, 245, 180, 40);
        panel.add(stockCheckButton);

        
        // SHOPPING CART
        

        JLabel cartLabel = new JLabel("Shopping Cart");
        cartLabel.setFont(new Font("Arial", Font.BOLD, 16));
        cartLabel.setBounds(30, 340, 200, 25);
        panel.add(cartLabel);

        cartModel = new DefaultTableModel();

        cartModel.setColumnIdentifiers(new String[]{
            "Medicine ID", "Medicine", "Price", "Quantity", "Subtotal"
        });

        cartTable = new JTable(cartModel);

        JScrollPane cartScroll = new JScrollPane(cartTable);
        cartScroll.setBounds(30, 370, 650, 190);
        panel.add(cartScroll);

        // Total
        totalLabel = new JLabel("Total: R0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 20));
        totalLabel.setBounds(730, 370, 250, 40);
        panel.add(totalLabel);

        // Checkout button
        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.setBounds(730, 430, 180, 40);
        panel.add(checkoutButton);

        // Clear cart button
        JButton clearCartButton = new JButton("Clear Cart");
        clearCartButton.setBounds(730, 485, 180, 40);
        panel.add(clearCartButton);

        // Logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(730, 540, 180, 40);
        panel.add(logoutButton);

        
        // BUTTON ACTIONS
        

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

    
    // LOAD MEDICINES
   

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
    

    private void clearCart() {

        cartModel.setRowCount(0);

        total = 0.00;

        totalLabel.setText("Total: R0.00");

        quantitySpinner.setValue(1);
    }

    
    // CHECKOUT
    

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
        

        closeButton.addActionListener(e -> {

            billFrame.dispose();
        });

        billFrame.add(panel);

        billFrame.setVisible(true);
    }
}