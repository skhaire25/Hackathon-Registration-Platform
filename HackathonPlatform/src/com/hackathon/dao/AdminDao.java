package com.hackathon.dao;

import com.hackathon.model.Admin;
import com.hackathon.util.DatabaseConnection;

import java.sql.*;

public class AdminDao {
    
    public Admin authenticateAdmin(String username, String password) throws SQLException {
        String sql = "SELECT * FROM admins WHERE username = ? AND password_hash = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Admin admin = extractAdminFromResultSet(rs);
                    updateLastLogin(admin.getAdminId());
                    return admin;
                }
            }
        }
        return null;
    }
    
    private void updateLastLogin(String adminId) throws SQLException {
        String sql = "UPDATE admins SET last_login = CURRENT_TIMESTAMP WHERE admin_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, adminId);
            pstmt.executeUpdate();
        }
    }
    
    private Admin extractAdminFromResultSet(ResultSet rs) throws SQLException {
        Admin admin = new Admin();
        admin.setAdminId(rs.getString("admin_id"));
        admin.setUsername(rs.getString("username"));
        admin.setPasswordHash(rs.getString("password_hash"));
        admin.setLastLogin(rs.getTimestamp("last_login"));
        return admin;
    }
}