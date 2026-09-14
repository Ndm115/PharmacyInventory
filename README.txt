HEALTHFIRST PHARMACY INVENTORY MANAGEMENT SYSTEM
Programming 732 Assignment

--------------------------------------------------
PROJECT DESCRIPTION
--------------------------------------------------

The HealthFirst Pharmacy Inventory Management System is a Java desktop
application developed to help manage the daily operations of a pharmacy.

The system allows administrators to manage medicines, suppliers and
cashier accounts. Cashiers can process medicine sales, check available
stock and generate customer bills.

The application was developed using Java Swing for the user interface,
MySQL for the database and JDBC for database connectivity.


--------------------------------------------------
MAIN FEATURES
--------------------------------------------------

- User login and authentication
- Admin and Cashier user roles
- Medicine management
- Supplier management
- Cashier account management
- Medicine stock checking
- Point of Sale (POS)
- Shopping cart
- Sales processing
- Automatic stock updates after sales
- Bill generation
- Save and print bill functionality
- Sales reports
- Item-wise sales reports
- Low stock reports
- Medicine expiry reports


--------------------------------------------------
USER ROLES
--------------------------------------------------

ADMIN

The Admin can:
- Manage medicines
- Manage suppliers
- Manage Cashier accounts
- View pharmacy reports


CASHIER

The Cashier can:
- View available medicines
- Check medicine stock
- Add medicines to the shopping cart
- Process customer sales
- Generate customer bills
- Save or print bills


--------------------------------------------------
DATABASE
--------------------------------------------------

Database Name:
pharmacy_inventory

The MySQL database contains the following tables:

- users
- suppliers
- medicines
- sales
- sale_items


--------------------------------------------------
TECHNOLOGIES USED
--------------------------------------------------

- Java
- Java Swing
- JDBC
- MySQL
- Apache NetBeans
- MySQL Connector/J


--------------------------------------------------
DEFAULT LOGIN DETAILS
--------------------------------------------------

Admin Account

Username: admin
Password: admin123


Cashier Account

Username: cashier
Password: cash123


--------------------------------------------------
HOW TO RUN THE PROJECT
--------------------------------------------------

1. Start the MySQL Server.
2. Import the supplied database.sql file into MySQL.
3. Open the PharmacyInventory project in Apache NetBeans.
4. Ensure that MySQL Connector/J is included in the project libraries.
5. Check the database connection settings in DatabaseConnection.java.
6. Run PharmacyInventory.java.
7. Log in using either the Admin or Cashier account.


--------------------------------------------------
PROJECT STRUCTURE
--------------------------------------------------

PharmacyInventory.java
- Main application entry point.

DatabaseConnection.java
- Handles the connection between Java and the MySQL database.

LoginFrame.java
- Handles login and redirects users according to their role.

AdminDashboard.java
- Provides access to the administrator functions.

MedicineManagement.java
- Allows medicines to be added, updated and deleted.

SupplierManagement.java
- Allows suppliers to be added, updated and deleted.

UserManagement.java
- Allows Cashier accounts to be managed.

CashierDashboard.java
- Provides the Point of Sale and billing functionality.

ReportsFrame.java
- Displays sales, item-wise, low stock and expiry reports.

