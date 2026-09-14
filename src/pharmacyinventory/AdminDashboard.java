package pharmacyinventory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
        mainPanel.setBackground(new Color(240, 248, 252));

        
        // HEADER
        //------------------------

        JPanel headerPanel = new JPanel(null);
        headerPanel.setBackground(new Color(45, 105, 135));
        headerPanel.setBounds(0, 0, 850, 105);

        JLabel titleLabel = new JLabel("Admin Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(30, 20, 350, 35);
        headerPanel.add(titleLabel);

        JLabel welcomeLabel = new JLabel("Welcome, " + adminName);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        welcomeLabel.setForeground(new Color(225, 240, 248));
        welcomeLabel.setBounds(30, 58, 400, 25);
        headerPanel.add(welcomeLabel);

        JLabel systemLabel = new JLabel(
                "HealthFirst Pharmacy",
                SwingConstants.RIGHT
        );
        systemLabel.setFont(new Font("Arial", Font.BOLD, 15));
        systemLabel.setForeground(Color.WHITE);
        systemLabel.setBounds(550, 35, 250, 30);
        headerPanel.add(systemLabel);

        mainPanel.add(headerPanel);

       
        // TABBED PANE
        //------------------------

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 13));
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.setBounds(30, 130, 770, 350);

        
        // MEDICINES TAB
        //------------------------

        JPanel medicinesPanel = createTabPanel();

        JLabel medicinesTitle = createTitle("Medicine Management");
        medicinesPanel.add(medicinesTitle);

        JLabel medicinesInfo = createInfo(
                "Add, update, delete and view pharmacy medicines."
        );
        medicinesPanel.add(medicinesInfo);

        JButton medicinesButton = createMainButton("Manage Medicines");
        medicinesPanel.add(medicinesButton);

        medicinesButton.addActionListener(e -> {
            new MedicineManagement().setVisible(true);
        });

        
        // SUPPLIERS TAB
        //------------------------

        JPanel suppliersPanel = createTabPanel();

        JLabel suppliersTitle = createTitle("Supplier Management");
        suppliersPanel.add(suppliersTitle);

        JLabel suppliersInfo = createInfo(
                "Add, update, delete and view pharmacy suppliers."
        );
        suppliersPanel.add(suppliersInfo);

        JButton suppliersButton = createMainButton("Manage Suppliers");
        suppliersPanel.add(suppliersButton);

        suppliersButton.addActionListener(e -> {
            new SupplierManagement().setVisible(true);
        });

        
        // USERS TAB
        //------------------------

        JPanel usersPanel = createTabPanel();

        JLabel usersTitle = createTitle("User Management");
        usersPanel.add(usersTitle);

        JLabel usersInfo = createInfo(
                "Create, update and delete Cashier accounts."
        );
        usersPanel.add(usersInfo);

        JButton usersButton = createMainButton("Manage Users");
        usersPanel.add(usersButton);

        usersButton.addActionListener(e -> {
            new UserManagement().setVisible(true);
        });

        
        // REPORTS TAB
        //------------------------

        JPanel reportsPanel = createTabPanel();

        JLabel reportsTitle = createTitle("Reports");
        reportsPanel.add(reportsTitle);

        JLabel reportsInfo = createInfo(
                "View sales, item-wise, low stock and expiry reports."
        );
        reportsPanel.add(reportsInfo);

        JButton reportsButton = createMainButton("View Reports");
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

       
        // LOGOUT
        //------------------------

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 13));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(new Color(180, 70, 70));
        logoutButton.setFocusPainted(false);
        logoutButton.setBounds(650, 505, 150, 38);
        mainPanel.add(logoutButton);

        logoutButton.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        add(mainPanel);
    }

    
    // CREATE TAB PANEL
    //------------------------

    private JPanel createTabPanel() {

        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        return panel;
    }

    
    // CREATE TAB TITLE
    //------------------------

    private JLabel createTitle(String text) {

        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 21));
        label.setForeground(new Color(45, 105, 135));
        label.setBounds(35, 35, 350, 30);

        return label;
    }

   
    // CREATE DESCRIPTION
    //------------------------

    private JLabel createInfo(String text) {

        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(new Color(70, 70, 70));
        label.setBounds(35, 80, 500, 25);

        return label;
    }

   
    // CREATE MANAGEMENT BUTTON
    //------------------------

    private JButton createMainButton(String text) {

        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(45, 105, 135));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBounds(35, 135, 210, 42);

        return button;
    }
}