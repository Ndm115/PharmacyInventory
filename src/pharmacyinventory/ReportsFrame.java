package pharmacyinventory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ReportsFrame extends JFrame {

    private JTable reportTable;
    private DefaultTableModel tableModel;
    private JLabel reportTitle;

    public ReportsFrame() {

        setTitle("HealthFirst Pharmacy - Reports");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(240, 248, 255));

        // Main title
        JLabel titleLabel = new JLabel("Pharmacy Reports");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setBounds(30, 20, 300, 35);
        panel.add(titleLabel);

        // Report buttons
        JButton salesButton = new JButton("Sales Report");
        salesButton.setBounds(30, 80, 180, 40);
        panel.add(salesButton);

        JButton itemButton = new JButton("Item-Wise Report");
        itemButton.setBounds(230, 80, 180, 40);
        panel.add(itemButton);

        JButton lowStockButton = new JButton("Low Stock Report");
        lowStockButton.setBounds(430, 80, 180, 40);
        panel.add(lowStockButton);

        JButton expiryButton = new JButton("Expiry Report");
        expiryButton.setBounds(630, 80, 180, 40);
        panel.add(expiryButton);

        // Current report heading
        reportTitle = new JLabel("Select a report");
        reportTitle.setFont(new Font("Arial", Font.BOLD, 18));
        reportTitle.setBounds(30, 145, 400, 30);
        panel.add(reportTitle);

        // Report table
        tableModel = new DefaultTableModel();
        reportTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(reportTable);
        scrollPane.setBounds(30, 185, 920, 370);
        panel.add(scrollPane);

        // Close button
        JButton closeButton = new JButton("Close");
        closeButton.setBounds(820, 570, 130, 35);
        panel.add(closeButton);

        // Button actions
        salesButton.addActionListener(e -> loadSalesReport());
        itemButton.addActionListener(e -> loadItemWiseReport());
        lowStockButton.addActionListener(e -> loadLowStockReport());
        expiryButton.addActionListener(e -> loadExpiryReport());

        closeButton.addActionListener(e -> dispose());

        add(panel);
    }

    // -----------------------------------
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

    // -----------------------------------
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

    // -----------------------------------
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

    // -----------------------------------
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