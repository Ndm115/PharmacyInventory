package pharmacyinventory;

import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {

    private String adminName;

    public AdminDashboard(String adminName) {

        this.adminName = adminName;

        // Window settings
        setTitle("HealthFirst Pharmacy - Admin Dashboard");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel
        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(new Color(240, 248, 255));

        // Title
        JLabel titleLabel = new JLabel("Admin Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setBounds(30, 20, 300, 35);
        mainPanel.add(titleLabel);

        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome, " + adminName);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        welcomeLabel.setBounds(30, 55, 400, 25);
        mainPanel.add(welcomeLabel);

        // --------------------------------
        // TABBED PANE
        // --------------------------------

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(30, 100, 770, 370);

        // --------------------------------
        // MEDICINES TAB
        // --------------------------------

        JPanel medicinesPanel = new JPanel(null);
        medicinesPanel.setBackground(Color.WHITE);

        JLabel medicinesTitle = new JLabel("Medicine Management");
        medicinesTitle.setFont(new Font("Arial", Font.BOLD, 20));
        medicinesTitle.setBounds(30, 30, 300, 30);
        medicinesPanel.add(medicinesTitle);

        JLabel medicinesInfo = new JLabel(
                "Add, update, delete and view pharmacy medicines."
        );
        medicinesInfo.setBounds(30, 75, 400, 25);
        medicinesPanel.add(medicinesInfo);

        JButton medicinesButton = new JButton("Manage Medicines");
        medicinesButton.setBounds(30, 130, 200, 45);
        medicinesPanel.add(medicinesButton);

        medicinesButton.addActionListener(e -> {
            new MedicineManagement().setVisible(true);
        });

        // --------------------------------
        // SUPPLIERS TAB
        // --------------------------------

        JPanel suppliersPanel = new JPanel(null);
        suppliersPanel.setBackground(Color.WHITE);

        JLabel suppliersTitle = new JLabel("Supplier Management");
        suppliersTitle.setFont(new Font("Arial", Font.BOLD, 20));
        suppliersTitle.setBounds(30, 30, 300, 30);
        suppliersPanel.add(suppliersTitle);

        JLabel suppliersInfo = new JLabel(
                "Add, update, delete and view pharmacy suppliers."
        );
        suppliersInfo.setBounds(30, 75, 400, 25);
        suppliersPanel.add(suppliersInfo);

        JButton suppliersButton = new JButton("Manage Suppliers");
        suppliersButton.setBounds(30, 130, 200, 45);
        suppliersPanel.add(suppliersButton);

        suppliersButton.addActionListener(e -> {
            new SupplierManagement().setVisible(true);
        });

        // --------------------------------
        // USERS TAB
        // --------------------------------

        JPanel usersPanel = new JPanel(null);
        usersPanel.setBackground(Color.WHITE);

        JLabel usersTitle = new JLabel("User Management");
        usersTitle.setFont(new Font("Arial", Font.BOLD, 20));
        usersTitle.setBounds(30, 30, 300, 30);
        usersPanel.add(usersTitle);

        JLabel usersInfo = new JLabel(
                "Create, update and delete Cashier accounts."
        );
        usersInfo.setBounds(30, 75, 400, 25);
        usersPanel.add(usersInfo);

        JButton usersButton = new JButton("Manage Users");
        usersButton.setBounds(30, 130, 200, 45);
        usersPanel.add(usersButton);

        usersButton.addActionListener(e -> {
            new UserManagement().setVisible(true);
        });

        // --------------------------------
        // REPORTS TAB
        // --------------------------------

        JPanel reportsPanel = new JPanel(null);
        reportsPanel.setBackground(Color.WHITE);

        JLabel reportsTitle = new JLabel("Reports");
        reportsTitle.setFont(new Font("Arial", Font.BOLD, 20));
        reportsTitle.setBounds(30, 30, 300, 30);
        reportsPanel.add(reportsTitle);

        JLabel reportsInfo = new JLabel(
                "View sales, item-wise, low stock and expiry reports."
        );
        reportsInfo.setBounds(30, 75, 450, 25);
        reportsPanel.add(reportsInfo);

        JButton reportsButton = new JButton("View Reports");
        reportsButton.setBounds(30, 130, 200, 45);
        reportsPanel.add(reportsButton);

        reportsButton.addActionListener(e -> {
            new ReportsFrame().setVisible(true);
        });

        // Add tabs
        tabbedPane.addTab("Medicines", medicinesPanel);
        tabbedPane.addTab("Suppliers", suppliersPanel);
        tabbedPane.addTab("Users", usersPanel);
        tabbedPane.addTab("Reports", reportsPanel);

        mainPanel.add(tabbedPane);

        // --------------------------------
        // LOGOUT
        // --------------------------------

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(650, 495, 150, 40);
        mainPanel.add(logoutButton);

        logoutButton.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        add(mainPanel);
    }
}