-- hackathon_platform.sql
-- MySQL Database Schema for Hackathon Platform

DROP DATABASE IF EXISTS hackathon_platform;
CREATE DATABASE hackathon_platform;
USE hackathon_platform;

-- Users Table (Participants)
CREATE TABLE users (
    user_id VARCHAR(36) PRIMARY KEY,
    username VARCHAR(60) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Teams Table
CREATE TABLE teams (
    team_id VARCHAR(36) PRIMARY KEY,
    team_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Team Members Junction Table
CREATE TABLE team_members (
    team_id VARCHAR(36) NOT NULL,
    user_id VARCHAR(36) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('TEAM_LEADER', 'MEMBER')),
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (team_id, user_id),
    FOREIGN KEY (team_id) REFERENCES teams(team_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_team (user_id)
);

-- Projects Table
CREATE TABLE projects (
    project_id VARCHAR(36) PRIMARY KEY,
    team_id VARCHAR(36) NOT NULL UNIQUE,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    admin_status VARCHAR(20) DEFAULT 'PENDING' CHECK (admin_status IN ('PENDING', 'APPROVED', 'REJECTED')),
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (team_id) REFERENCES teams(team_id) ON DELETE CASCADE
);

-- Admins Table
CREATE TABLE admins (
    admin_id VARCHAR(36) PRIMARY KEY,
    username VARCHAR(60) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    last_login TIMESTAMP NULL
);

-- Insert default admin account (password: admin123)
INSERT INTO admins (admin_id, username, password_hash) 
VALUES (UUID(), 'vedant', 'vedya123');
INSERT INTO admins (admin_id, username, password_hash) 
VALUES (UUID(), 'ojha', 'ogoj123');