package pharmacyinventory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Details used to connect to our MySQL database
    private static final String URL =
            "jdbc:mysql://localhost:3306/pharmacy_inventory";

    private static final String USER = "root";
    private static final String PASSWORD = "Ringo#07860";

    // Returns a connection to the database
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}