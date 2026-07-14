
package com.hackathon;

import com.hackathon.ui.ConsoleUI;
import com.hackathon.util.DatabaseConnection;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        ConsoleUI consoleUI = null;
        
        try {
            // Test database connection
            DatabaseConnection.getInstance().getConnection();
            System.out.println("✅ Database connection established successfully!");
            
            // Start the application
            consoleUI = new ConsoleUI();
            consoleUI.displayMainMenu();
            
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed!");
            System.err.println("Error: " + e.getMessage());
            System.err.println("\nPlease ensure:");
            System.err.println("1. MySQL server is running");
            System.err.println("2. Database 'hackathon_platform' exists");
            System.err.println("3. Credentials in DatabaseConnection.java are correct");
        } finally {
            // Cleanup
            if (consoleUI != null) {
                consoleUI.close();
            }
            
            try {
                DatabaseConnection.getInstance().closeConnection();
                System.out.println("\n✅ Database connection closed.");
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
}