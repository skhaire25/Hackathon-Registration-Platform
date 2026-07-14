package com.hackathon.model;

import java.sql.Timestamp;

public class Admin {
    private String adminId;
    private String username;
    private String passwordHash;
    private Timestamp lastLogin;
    
    public Admin() {}
    
    public Admin(String adminId, String username, String passwordHash, Timestamp lastLogin) {
        this.adminId = adminId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.lastLogin = lastLogin;
    }
    
    // Getters and Setters
    public String getAdminId() { return adminId; }
    public void setAdminId(String adminId) { this.adminId = adminId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    
    public Timestamp getLastLogin() { return lastLogin; }
    public void setLastLogin(Timestamp lastLogin) { this.lastLogin = lastLogin; }
    
    @Override
    public String toString() {
        return "Admin{adminId='" + adminId + "', username='" + username + "'}";
    }
}