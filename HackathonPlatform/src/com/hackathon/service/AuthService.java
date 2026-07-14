package com.hackathon.service;

import com.hackathon.dao.AdminDao;
import com.hackathon.dao.UserDao;
import com.hackathon.model.Admin;
import com.hackathon.model.User;

import java.sql.SQLException;

public class AuthService {
    private UserDao userDao;
    private AdminDao adminDao;
    
    public AuthService() {
        this.userDao = new UserDao();
        this.adminDao = new AdminDao();
    }
    
    public User registerParticipant(String username, String password) throws SQLException {
        // Validate username
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        
        // Check if username already exists
        if (userDao.usernameExists(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        return userDao.registerUser(username, password);
    }
    
    public User loginParticipant(String username, String password) throws SQLException {
        return userDao.authenticateUser(username, password);
    }
    
    public Admin loginAdmin(String username, String password) throws SQLException {
        return adminDao.authenticateAdmin(username, password);
    }
}