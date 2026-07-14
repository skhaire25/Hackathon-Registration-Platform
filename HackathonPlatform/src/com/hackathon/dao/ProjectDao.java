package com.hackathon.dao;

import com.hackathon.model.Project;
import com.hackathon.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProjectDao {
    
    public Project submitProject(String teamId, String title, String description) throws SQLException {
        String projectId = UUID.randomUUID().toString();
        String sql = "INSERT INTO projects (project_id, team_id, title, description, admin_status) " +
                    "VALUES (?, ?, ?, ?, 'PENDING')";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, projectId);
            pstmt.setString(2, teamId);
            pstmt.setString(3, title);
            pstmt.setString(4, description);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                return getProjectById(projectId);
            }
            return null;
        }
    }
    
    public Project getProjectById(String projectId) throws SQLException {
        String sql = "SELECT p.*, t.team_name FROM projects p " +
                    "INNER JOIN teams t ON p.team_id = t.team_id " +
                    "WHERE p.project_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, projectId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractProjectFromResultSet(rs);
                }
            }
        }
        return null;
    }
    
    public Project getProjectByTeamId(String teamId) throws SQLException {
        String sql = "SELECT p.*, t.team_name FROM projects p " +
                    "INNER JOIN teams t ON p.team_id = t.team_id " +
                    "WHERE p.team_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, teamId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractProjectFromResultSet(rs);
                }
            }
        }
        return null;
    }
    
    public List<Project> getAllProjects() throws SQLException {
        String sql = "SELECT p.*, t.team_name FROM projects p " +
                    "INNER JOIN teams t ON p.team_id = t.team_id " +
                    "ORDER BY p.submitted_at DESC";
        
        List<Project> projects = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                projects.add(extractProjectFromResultSet(rs));
            }
        }
        return projects;
    }
    
    public boolean updateProjectStatus(String projectId, String newStatus) throws SQLException {
        String sql = "UPDATE projects SET admin_status = ? WHERE project_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newStatus);
            pstmt.setString(2, projectId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    public boolean teamHasSubmittedProject(String teamId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM projects WHERE team_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, teamId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    private Project extractProjectFromResultSet(ResultSet rs) throws SQLException {
        Project project = new Project();
        project.setProjectId(rs.getString("project_id"));
        project.setTeamId(rs.getString("team_id"));
        project.setTeamName(rs.getString("team_name"));
        project.setTitle(rs.getString("title"));
        project.setDescription(rs.getString("description"));
        project.setAdminStatus(rs.getString("admin_status"));
        project.setSubmittedAt(rs.getTimestamp("submitted_at"));
        return project;
    }
}