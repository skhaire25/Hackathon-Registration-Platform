package com.hackathon.ui;

import com.hackathon.model.Admin;
import com.hackathon.model.Project;
import com.hackathon.service.ProjectService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class AdminMenu {
    private Admin currentAdmin;
    private Scanner scanner;
    private ProjectService projectService;
    
    public AdminMenu(Admin admin, Scanner scanner) {
        this.currentAdmin = admin;
        this.scanner = scanner;
        this.projectService = new ProjectService();
    }
    
    public void displayMenu() {
        while (true) {
            System.out.println("\n╔════════════════════════════════════════════════╗");
            System.out.println("║             ADMIN DASHBOARD                    ║");
            System.out.println("║  Logged in as: " + String.format("%-32s", currentAdmin.getUsername()) + "║");
            System.out.println("╚════════════════════════════════════════════════╝");
            System.out.println("1. View All Submitted Projects");
            System.out.println("2. Update Project Status");
            System.out.println("3. Logout");
            System.out.print("\nSelect an option: ");
            
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    handleViewAllProjects();
                    break;
                case "2":
                    handleUpdateProjectStatus();
                    break;
                case "3":
                    System.out.println("\n✅ Admin logged out successfully.");
                    return;
                default:
                    System.out.println("\n❌ Invalid option. Please try again.");
            }
        }
    }
    
    private void handleViewAllProjects() {
        System.out.println("\n--- ALL SUBMITTED PROJECTS ---");
        
        try {
            List<Project> projects = projectService.getAllProjects();
            
            if (projects.isEmpty()) {
                System.out.println("No projects have been submitted yet.");
                return;
            }
            
            System.out.println(String.format("%-38s %-25s %-15s %-12s", 
                "Project ID", "Team Name", "Title", "Status"));
            System.out.println("─".repeat(95));
            
            for (Project project : projects) {
                String statusIcon = getStatusIcon(project.getAdminStatus());
                String truncatedTitle = project.getTitle().length() > 15 ? 
                    project.getTitle().substring(0, 12) + "..." : project.getTitle();
                    
                System.out.println(String.format("%-38s %-25s %-15s %s %-10s", 
                    project.getProjectId(),
                    project.getTeamName(),
                    truncatedTitle,
                    statusIcon,
                    project.getAdminStatus()));
            }
            
            System.out.println("\nTotal projects: " + projects.size());
            
        } catch (SQLException e) {
            System.out.println("\n❌ Database error: " + e.getMessage());
        }
    }
    
    private void handleUpdateProjectStatus() {
        System.out.println("\n--- UPDATE PROJECT STATUS ---");
        
        System.out.print("Enter Project ID: ");
        String projectId = scanner.nextLine().trim();
        
        System.out.println("\nSelect new status:");
        System.out.println("1. APPROVED");
        System.out.println("2. REJECTED");
        System.out.println("3. PENDING");
        System.out.print("Enter choice (1-3): ");
        
        String statusChoice = scanner.nextLine().trim();
        String newStatus;
        
        switch (statusChoice) {
            case "1":
                newStatus = "APPROVED";
                break;
            case "2":
                newStatus = "REJECTED";
                break;
            case "3":
                newStatus = "PENDING";
                break;
            default:
                System.out.println("\n❌ Invalid status choice.");
                return;
        }
        
        try {
            boolean success = projectService.updateProjectStatus(projectId, newStatus);
            if (success) {
                System.out.println("\n✅ Project status updated to: " + newStatus);
            } else {
                System.out.println("\n❌ Project not found or update failed.");
            }
        } catch (SQLException e) {
            System.out.println("\n❌ Database error: " + e.getMessage());
        }
    }
    
    private String getStatusIcon(String status) {
        switch (status) {
            case "PENDING":
                return "⏳";
            case "APPROVED":
                return "✅";
            case "REJECTED":
                return "❌";
            default:
                return "  ";
        }
    }
}