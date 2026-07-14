package com.hackathon.dao;

import com.hackathon.model.Team;
import com.hackathon.model.TeamMember;
import com.hackathon.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TeamDao {
    
    public Team createTeam(String teamName, String creatorUserId) throws SQLException {
        String teamId = UUID.randomUUID().toString();
        Connection conn = null;
        
        try {
            conn = DatabaseConnection.getInstance().getConnection();
            conn.setAutoCommit(false); // Start transaction
            
            // Insert team
            String teamSql = "INSERT INTO teams (team_id, team_name) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(teamSql)) {
                pstmt.setString(1, teamId);
                pstmt.setString(2, teamName);
                pstmt.executeUpdate();
            }
            
            // Add creator as TEAM_LEADER
            String memberSql = "INSERT INTO team_members (team_id, user_id, role) VALUES (?, ?, 'TEAM_LEADER')";
            try (PreparedStatement pstmt = conn.prepareStatement(memberSql)) {
                pstmt.setString(1, teamId);
                pstmt.setString(2, creatorUserId);
                pstmt.executeUpdate();
            }
            
            conn.commit(); // Commit transaction
            return getTeamById(teamId);
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public boolean addMemberToTeam(String teamId, String userId) throws SQLException {
        String sql = "INSERT INTO team_members (team_id, user_id, role) VALUES (?, ?, 'MEMBER')";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, teamId);
            pstmt.setString(2, userId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    public Team getTeamById(String teamId) throws SQLException {
        String sql = "SELECT * FROM teams WHERE team_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, teamId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractTeamFromResultSet(rs);
                }
            }
        }
        return null;
    }
    
    public Team getUserTeam(String userId) throws SQLException {
        String sql = "SELECT t.* FROM teams t " +
                    "INNER JOIN team_members tm ON t.team_id = tm.team_id " +
                    "WHERE tm.user_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractTeamFromResultSet(rs);
                }
            }
        }
        return null;
    }
    
    public List<TeamMember> getTeamMembers(String teamId) throws SQLException {
        String sql = "SELECT tm.*, u.username FROM team_members tm " +
                    "INNER JOIN users u ON tm.user_id = u.user_id " +
                    "WHERE tm.team_id = ? ORDER BY tm.role DESC, tm.joined_at ASC";
        
        List<TeamMember> members = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, teamId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    TeamMember member = new TeamMember();
                    member.setTeamId(rs.getString("team_id"));
                    member.setUserId(rs.getString("user_id"));
                    member.setUsername(rs.getString("username"));
                    member.setRole(rs.getString("role"));
                    member.setJoinedAt(rs.getTimestamp("joined_at"));
                    members.add(member);
                }
            }
        }
        return members;
    }
    
    public boolean isUserInTeam(String userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM team_members WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    public String getUserRoleInTeam(String userId, String teamId) throws SQLException {
        String sql = "SELECT role FROM team_members WHERE user_id = ? AND team_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            pstmt.setString(2, teamId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("role");
                }
            }
        }
        return null;
    }
    
    public boolean teamExists(String teamId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM teams WHERE team_id = ?";
        
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
    
    private Team extractTeamFromResultSet(ResultSet rs) throws SQLException {
        Team team = new Team();
        team.setTeamId(rs.getString("team_id"));
        team.setTeamName(rs.getString("team_name"));
        team.setCreatedAt(rs.getTimestamp("created_at"));
        return team;
    }
}