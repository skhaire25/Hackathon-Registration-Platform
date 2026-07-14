package com.hackathon.ui;

import com.hackathon.model.Admin;
import com.hackathon.model.User;
import com.hackathon.service.AuthService;

import java.sql.SQLException;
import java.util.Scanner;

public class ConsoleUI {
    private Scanner scanner;
    private AuthService authService;
    
    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
        this.authService = new AuthService();
    }
    
    public void displayMainMenu() {
        while (true) {
            System.out.println("\n╔════════════════════════════════════════════════╗");
            System.out.println("║   HACKATHON REGISTRATION & MANAGEMENT SYSTEM   ║");
            System.out.println("╚════════════════════════════════════════════════╝");
            System.out.println("1. Register as Participant");
            System.out.println("2. Participant Login");
            System.out.println("3. Admin Login");
            System.out.println("4. Exit");
            System.out.print("\nSelect an option: ");
            
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    handleParticipantRegistration();
                    break;
                case "2":
                    handleParticipantLogin();
                    break;
                case "3":
                    handleAdminLogin();
                    break;
                case "4":
                    System.out.println("\nThank you for using the Hackathon Platform. Goodbye!");
                    return;
                default:
                    System.out.println("\n❌ Invalid option. Please try again.");
            }
        }
    }
    
    private void handleParticipantRegistration() {
        System.out.println("\n--- PARTICIPANT REGISTRATION ---");
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();
        
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();
        
        try {
            User user = authService.registerParticipant(username, password);
            if (user != null) {
                System.out.println("\n✅ Registration successful! Welcome, " + user.getUsername());
                System.out.println("Your User ID: " + user.getUserId());
                System.out.println("You can now log in.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("\n❌ Registration failed: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("\n❌ Database error: " + e.getMessage());
        }
    }
    
    private void handleParticipantLogin() {
        System.out.println("\n--- PARTICIPANT LOGIN ---");
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();
        
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();
        
        try {
            User user = authService.loginParticipant(username, password);
            if (user != null) {
                System.out.println("\n✅ Login successful! Welcome back, " + user.getUsername());
                ParticipantMenu participantMenu = new ParticipantMenu(user, scanner);
                participantMenu.displayMenu();
            } else {
                System.out.println("\n❌ Invalid credentials. Please try again.");
            }
        } catch (SQLException e) {
            System.out.println("\n❌ Database error: " + e.getMessage());
        }
    }
    
    private void handleAdminLogin() {
        System.out.println("\n--- ADMIN LOGIN ---");
        System.out.print("Enter admin username: ");
        String username = scanner.nextLine().trim();
        
        System.out.print("Enter admin password: ");
        String password = scanner.nextLine().trim();
        
        try {
            Admin admin = authService.loginAdmin(username, password);
            if (admin != null) {
                System.out.println("\n✅ Admin login successful! Welcome, " + admin.getUsername());
                AdminMenu adminMenu = new AdminMenu(admin, scanner);
                adminMenu.displayMenu();
            } else {
                System.out.println("\n❌ Invalid admin credentials. Please try again.");
            }
        } catch (SQLException e) {
            System.out.println("\n❌ Database error: " + e.getMessage());
        }
    }
    
    public void close() {
        scanner.close();
    }
}