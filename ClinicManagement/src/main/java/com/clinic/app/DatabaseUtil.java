package com.clinic.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtil {

    // SQLite connection string - creates/uses a file named clinic_data.db in the project folder
    private static final String JDBC_URL = "jdbc:sqlite:clinic_data.db";

    /**
     * Establishes and returns a SQLite database connection.
     */
    public static Connection getConnection() throws SQLException {
        try {
            // SQLite driver is automatically loaded by the driver manager
            return DriverManager.getConnection(JDBC_URL);
        } catch (SQLException e) {
            // Use an alert or logging framework in a real application
            System.err.println("Database connection failed: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Initializes the database schema (creates the PATIENTS table).
     */
    public static void initializeDatabase() {
        // Note: SQLite uses INTEGER PRIMARY KEY for auto-incrementing ID
        String sql = "CREATE TABLE IF NOT EXISTS PATIENTS (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                     "first_name TEXT NOT NULL, " +
                     "last_name TEXT NOT NULL, " +
                     "age INTEGER, " +
                     "sex TEXT, " +
                     "last_visit TEXT" + // Store LocalDate as TEXT (YYYY-MM-DD)
                     ");";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(sql);
            System.out.println("PATIENTS table ready.");
            
        } catch (SQLException e) {
            System.err.println("Database initialization failed: " + e.getMessage());
        }
    }
}
