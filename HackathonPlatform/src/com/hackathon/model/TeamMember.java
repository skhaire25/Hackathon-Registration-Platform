package com.hackathon.model;

import java.sql.Timestamp;

public class TeamMember {
    private String teamId;
    private String userId;
    private String username; // For display purposes
    private String role; // TEAM_LEADER or MEMBER
    private Timestamp joinedAt;
    
    public TeamMember() {}
    
    public TeamMember(String teamId, String userId, String username, String role, Timestamp joinedAt) {
        this.teamId = teamId;
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.joinedAt = joinedAt;
    }
    
    // Getters and Setters
    public String getTeamId() { return teamId; }
    public void setTeamId(String teamId) { this.teamId = teamId; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public Timestamp getJoinedAt() { return joinedAt; }
    public void setJoinedAt(Timestamp joinedAt) { this.joinedAt = joinedAt; }
    
    @Override
    public String toString() {
        return "TeamMember{username='" + username + "', role='" + role + "'}";
    }
}