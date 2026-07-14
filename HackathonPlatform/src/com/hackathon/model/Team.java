package com.hackathon.model;

import java.sql.Timestamp;

public class Team {
    private String teamId;
    private String teamName;
    private Timestamp createdAt;
    
    public Team() {}
    
    public Team(String teamId, String teamName, Timestamp createdAt) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public String getTeamId() { return teamId; }
    public void setTeamId(String teamId) { this.teamId = teamId; }
    
    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }
    
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    
    @Override
    public String toString() {
        return "Team{teamId='" + teamId + "', teamName='" + teamName + "'}";
    }
}