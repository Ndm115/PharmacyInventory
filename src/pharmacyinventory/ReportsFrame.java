package pharmacyinventory;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ReportsFrame extends JFrame {

    private JTable reportTable;
    private DefaultTableModel tableModel;
    private JLabel reportTitle;

    public ReportsFrame() {

        // Window settings
        setTitle("HealthFirst Pharmacy - Reports");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(240, 248, 252));

        
        // HEADER
        // -----------------------------------

        JPanel headerPanel = new JPanel(null);
        headerPanel.setBackground(new Color(45, 105, 135));
        headerPanel.setBounds(0, 0, 1000, 90);

        JLabel titleLabel = new JLabel("Pharmacy Reports");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 25));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(30, 17, 350, 35);
        headerPanel.add(titleLabel);

        JLabel subtitleLabel = new JLabel(
                "View sales, stock and expiry information"
        );
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(225, 240, 248));
        subtitleLabel.setBounds(30, 50, 400, 25);
        headerPanel.add(subtitleLabel);

        panel.add(headerPanel);

        
        // REPORT SELECTION
        // -----------------------------------

        JLabel selectionLabel = new JLabel("Select Report");
        selectionLabel.setFont(new Font("Arial", Font.BOLD, 17));
        selectionLabel.setForeground(new Color(45, 105, 135));
        selectionLabel.setBounds(30, 115, 200, 25);
        panel.add(selectionLabel);

        JButton salesButton = createButton("Sales Report");
        salesButton.setBounds(30, 150, 200, 40);
        panel.add(salesButton);

        JButton itemButton = createButton("Item-Wise Report");
        itemButton.setBounds(250, 150, 200, 40);
        panel.add(itemButton);

        JButton lowStockButton = createButton("Low Stock Report");
        lowStockButton.setBounds(470, 150, 200, 40);
        panel.add(lowStockButton);

        JButton expiryButton = createButton("Expiry Report");
        expiryButton.setBounds(690, 150, 200, 40);
        panel.add(expiryButton);

        
        // CURRENT REPORT
        // -----------------------------------

        JPanel reportPanel = new JPanel(null);
        reportPanel.setBackground(Color.WHITE);
        reportPanel.setBorder(
                new LineBorder(new Color(205, 215, 220))
        );
        reportPanel.setBounds(30, 215, 920, 325);

        reportTitle = new JLabel("Select a report to view");
        reportTitle.setFont(new Font("Arial", Font.BOLD, 18));
        reportTitle.setForeground(new Color(45, 105, 135));
        reportTitle.setBounds(20, 15, 500, 30);
        reportPanel.add(reportTitle);

        // Report table
        tableModel = new DefaultTableModel();
        reportTable = new JTable(tableModel);

        reportTable.setFont(new Font("Arial", Font.PLAIN, 12));
        reportTable.setRowHeight(25);
        reportTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        reportTable.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 12)
        );

        reportTable.getTableHeader().setBackground(
                new Color(225, 238, 245)
        );

        JScrollPane scrollPane = new JScrollPane(reportTable);
        scrollPane.setBounds(20, 55, 880, 245);
        scrollPane.setBorder(
                new LineBorder(new Color(190, 205, 215))
        );
        reportPanel.add(scrollPane);

        panel.add(reportPanel);

        
        // CLOSE BUTTON
        // -----------------------------------

        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Arial", Font.BOLD, 13));
        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(new Color(100, 110, 120));
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.setBounds(800, 560, 150, 36);
        panel.add(closeButton);

        
        // BUTTON ACTIONS
        // -----------------------------------

        salesButton.addActionListener(e -> loadSalesReport());
        itemButton.addActionListener(e -> loadItemWiseReport());
        lowStockButton.addActionListener(e -> loadLowStockReport());
        expiryButton.addActionListener(e -> loadExpiryReport());

        closeButton.addActionListener(e -> dispose());

        add(panel);
    }

    // Create consistent report buttons
    private JButton createButton(String text) {

        JButton button = new JButton(text);

        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(45, 105, 135));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    
    // SALES REPORT
    // -----------------------------------

    private void loadSalesReport() {

        reportTitle.setText("Sales Report");

        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);

        tableModel.setColumnIdentifiers(new String[]{
            "Sale ID", "Date", "Cashier", "Total Amount"
        });

        String sql =
                "SELECT s.sale_id, s.sale_date, u.full_name, s.total_amount "
                + "FROM sales s "
                + "JOIN users u ON s.user_id = u.user_id "
                + "ORDER BY s.sale_date DESC";

        try (
                Connection connection = DatabaseConnection.getConnection();
                Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery(sql)
        ) {

            while (result.next()) {

                tableModel.addRow(new Object[]{
                    result.getInt("sale_id"),
                    result.getTimestamp("sale_date"),
                    result.getString("full_name"),
                    result.getDouble("total_amount")
                });
            }

        } catch (SQLException e) {

            showError(e);
        }
    }

    
    // ITEM-WISE REPORT
    // -----------------------------------

    private void loadItemWiseReport() {

        reportTitle.setText("Item-Wise Sales Report");

        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);

        tableModel.setColumnIdentifiers(new String[]{
            "Medicine", "Quantity Sold", "Total Sales"
        });

        String sql =
                "SELECT m.name, "
                + "SUM(si.quantity_sold) AS total_quantity, "
                + "SUM(si.quantity_sold * si.price_at_sale) AS total_sales "
                + "FROM sale_items si "
                + "JOIN medicines m ON si.medicine_id = m.medicine_id "
                + "GROUP BY m.medicine_id, m.name "
                + "ORDER BY total_quantity DESC";

        try (
                Connection connection = DatabaseConnection.getConnection();
                Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery(sql)
        ) {

            while (result.next()) {

                tableModel.addRow(new Object[]{
                    result.getString("name"),
                    result.getInt("total_quantity"),
                    result.getDouble("total_sales")
                });
            }

        } catch (SQLException e) {

            showError(e);
        }
    }

    
    // LOW STOCK REPORT
    // -----------------------------------

    private void loadLowStockReport() {

        reportTitle.setText("Low Stock Report");

        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);

        tableModel.setColumnIdentifiers(new String[]{
            "Medicine ID", "Medicine", "Current Stock", "Reorder Level"
        });

        String sql =
                "SELECT medicine_id, name, quantity_in_stock, reorder_level "
                + "FROM medicines "
                + "WHERE quantity_in_stock <= reorder_level "
                + "ORDER BY quantity_in_stock";

        try (
                Connection connection = DatabaseConnection.getConnection();
                Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery(sql)
        ) {

            while (result.next()) {

                tableModel.addRow(new Object[]{
                    result.getInt("medicine_id"),
                    result.getString("name"),
                    result.getInt("quantity_in_stock"),
                    result.getInt("reorder_level")
                });
            }

        } catch (SQLException e) {

            showError(e);
        }
    }

    
    // EXPIRY REPORT
    // -----------------------------------

    private void loadExpiryReport() {

        reportTitle.setText("Expiry Report - Next One Month");

        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);

        tableModel.setColumnIdentifiers(new String[]{
            "Medicine ID", "Medicine", "Expiry Date", "Stock"
        });

        String sql =
                "SELECT medicine_id, name, expiry_date, quantity_in_stock "
                + "FROM medicines "
                + "WHERE expiry_date BETWEEN CURDATE() "
                + "AND DATE_ADD(CURDATE(), INTERVAL 1 MONTH) "
                + "ORDER BY expiry_date";

        try (
                Connection connection = DatabaseConnection.getConnection();
                Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery(sql)
        ) {

            while (result.next()) {

                tableModel.addRow(new Object[]{
                    result.getInt("medicine_id"),
                    result.getString("name"),
                    result.getDate("expiry_date"),
                    result.getInt("quantity_in_stock")
                });
            }

        } catch (SQLException e) {

            showError(e);
        }
    }

    // Show database errors
    private void showError(SQLException e) {

        JOptionPane.showMessageDialog(
                this,
                "Could not load report: " + e.getMessage(),
                "Report Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}