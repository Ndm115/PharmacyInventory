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
HOW TO RUN THE APPLICATION
--------------------------------------------------

1. Ensure MySQL Server is installed and running.

2. Import the supplied database.sql file into MySQL.
   The script will create the pharmacy_inventory database,
   required tables and sample data.

3. Ensure the MySQL connection details match the settings
   used in DatabaseConnection.java.

4. Run nadeem_pims.exe.

5. Use one of the following accounts to log in:

   Admin:
   Username: admin
   Password: admin123

   Cashier:
   Username: cashier
   Password: cash123

NOTE:
The dist folder must remain in the same folder as nadeem_pims.exe
because it contains the Java application and required MySQL JDBC
driver files.


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

