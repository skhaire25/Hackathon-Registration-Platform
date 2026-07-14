package com.hackathon.ui;

import com.hackathon.model.Project;
import com.hackathon.model.Team;
import com.hackathon.model.TeamMember;
import com.hackathon.model.User;
import com.hackathon.service.ProjectService;
import com.hackathon.service.TeamService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ParticipantMenu {
    private User currentUser;
    private Scanner scanner;
    private TeamService teamService;
    private ProjectService projectService;
    
    public ParticipantMenu(User user, Scanner scanner) {
        this.currentUser = user;
        this.scanner = scanner;
        this.teamService = new TeamService();
        this.projectService = new ProjectService();
    }
    
    public void displayMenu() {
        while (true) {
            System.out.println("\n╔════════════════════════════════════════════════╗");
            System.out.println("║          PARTICIPANT DASHBOARD                 ║");
            System.out.println("║  Logged in as: " + String.format("%-32s", currentUser.getUsername()) + "║");
            System.out.println("╚════════════════════════════════════════════════╝");
            System.out.println("1. Create Team");
            System.out.println("2. Join Team");
            System.out.println("3. View My Team");
            System.out.println("4. Submit Project");
            System.out.println("5. View My Team's Project Status");
            System.out.println("6. Logout");
            System.out.print("\nSelect an option: ");
            
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    handleCreateTeam();
                    break;
                case "2":
                    handleJoinTeam();
                    break;
                case "3":
                    handleViewMyTeam();
                    break;
                case "4":
                    handleSubmitProject();
                    break;
                case "5":
                    handleViewProjectStatus();
                    break;
                case "6":
                    System.out.println("\n✅ Logged out successfully.");
                    return;
                default:
                    System.out.println("\n❌ Invalid option. Please try again.");
            }
        }
    }
    
    private void handleCreateTeam() {
        System.out.println("\n--- CREATE TEAM ---");
        System.out.print("Enter team name: ");
        String teamName = scanner.nextLine().trim();
        
        try {
            Team team = teamService.createTeam(teamName, currentUser.getUserId());
            if (team != null) {
                System.out.println("\n✅ Team created successfully!");
                System.out.println("Team ID: " + team.getTeamId());
                System.out.println("Team Name: " + team.getTeamName());
                System.out.println("Your Role: TEAM_LEADER");
                System.out.println("\n⚠️  Share this Team ID with others so they can join!");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("\n❌ " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("\n❌ Database error: " + e.getMessage());
        }
    }
    
    private void handleJoinTeam() {
        System.out.println("\n--- JOIN TEAM ---");
        System.out.print("Enter Team ID: ");
        String teamId = scanner.nextLine().trim();
        
        try {
            boolean success = teamService.joinTeam(teamId, currentUser.getUserId());
            if (success) {
                System.out.println("\n✅ Successfully joined the team!");
                System.out.println("Your Role: MEMBER");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("\n❌ " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("\n❌ Database error: " + e.getMessage());
        }
    }
    
    private void handleViewMyTeam() {
        System.out.println("\n--- MY TEAM ---");
        
        try {
            Team team = teamService.getUserTeam(currentUser.getUserId());
            
            if (team == null) {
                System.out.println("❌ You are not in any team yet.");
                System.out.println("Create a team or join an existing one.");
                return;
            }
            
            System.out.println("Team ID: " + team.getTeamId());
            System.out.println("Team Name: " + team.getTeamName());
            System.out.println("Created At: " + team.getCreatedAt());
            
            List<TeamMember> members = teamService.getTeamMembers(team.getTeamId());
            
            System.out.println("\n--- TEAM MEMBERS ---");
            System.out.println(String.format("%-30s %-15s", "Username", "Role"));
            System.out.println("─".repeat(45));
            
            for (TeamMember member : members) {
                String marker = member.getUserId().equals(currentUser.getUserId()) ? " (You)" : "";
                System.out.println(String.format("%-30s %-15s", 
                    member.getUsername() + marker, member.getRole()));
            }
            
        } catch (SQLException e) {
            System.out.println("\n❌ Database error: " + e.getMessage());
        }
    }
    
    private void handleSubmitProject() {
        System.out.println("\n--- SUBMIT PROJECT ---");
        
        System.out.print("Enter project title: ");
        String title = scanner.nextLine().trim();
        
        System.out.print("Enter project description: ");
        String description = scanner.nextLine().trim();
        
        try {
            Project project = projectService.submitProject(currentUser.getUserId(), title, description);
            if (project != null) {
                System.out.println("\n✅ Project submitted successfully!");
                System.out.println("Project ID: " + project.getProjectId());
                System.out.println("Title: " + project.getTitle());
                System.out.println("Status: " + project.getAdminStatus());
                System.out.println("Submitted At: " + project.getSubmittedAt());
            }
        } catch (IllegalArgumentException e) {
            System.out.println("\n❌ " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("\n❌ Database error: " + e.getMessage());
        }
    }
    
    private void handleViewProjectStatus() {
        System.out.println("\n--- PROJECT STATUS ---");
        
        try {
            Team team = teamService.getUserTeam(currentUser.getUserId());
            
            if (team == null) {
                System.out.println("❌ You are not in any team yet.");
                return;
            }
            
            Project project = projectService.getProjectByTeamId(team.getTeamId());
            
            if (project == null) {
                System.out.println("❌ Your team has not submitted any project yet.");
                return;
            }
            
            System.out.println("Project ID: " + project.getProjectId());
            System.out.println("Team: " + project.getTeamName());
            System.out.println("Title: " + project.getTitle());
            System.out.println("Description: " + project.getDescription());
            System.out.println("Status: " + project.getAdminStatus());
            System.out.println("Submitted At: " + project.getSubmittedAt());
            
            // Visual status indicator
            switch (project.getAdminStatus()) {
                case "PENDING":
                    System.out.println("\n⏳ Your project is pending review.");
                    break;
                case "APPROVED":
                    System.out.println("\n✅ Congratulations! Your project has been approved!");
                    break;
                case "REJECTED":
                    System.out.println("\n❌ Your project was rejected.");
                    break;
            }
            
        } catch (SQLException e) {
            System.out.println("\n❌ Database error: " + e.getMessage());
        }
    }
}