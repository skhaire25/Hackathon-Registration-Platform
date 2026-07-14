package com.hackathon.model;

import java.sql.Timestamp;

public class Project {
    private String projectId;
    private String teamId;
    private String teamName; // For display purposes
    private String title;
    private String description;
    private String adminStatus; // PENDING, APPROVED, REJECTED
    private Timestamp submittedAt;
    
    public Project() {}
    
    public Project(String projectId, String teamId, String title, String description, 
                   String adminStatus, Timestamp submittedAt) {
        this.projectId = projectId;
        this.teamId = teamId;
        this.title = title;
        this.description = description;
        this.adminStatus = adminStatus;
        this.submittedAt = submittedAt;
    }
    
    // Getters and Setters
    public String getProjectId() { return projectId; }
    public void setProjectId(String projectId) { this.projectId = projectId; }
    
    public String getTeamId() { return teamId; }
    public void setTeamId(String teamId) { this.teamId = teamId; }
    
    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getAdminStatus() { return adminStatus; }
    public void setAdminStatus(String adminStatus) { this.adminStatus = adminStatus; }
    
    public Timestamp getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(Timestamp submittedAt) { this.submittedAt = submittedAt; }
    
    @Override
    public String toString() {
        return "Project{projectId='" + projectId + "', title='" + title + 
               "', status='" + adminStatus + "'}";
    }
}