package com.hackathon.service;

import com.hackathon.dao.TeamDao;
import com.hackathon.model.Team;
import com.hackathon.model.TeamMember;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class TeamService {
    private TeamDao teamDao;
    
    public TeamService() {
        this.teamDao = new TeamDao();
    }
    
    public Team createTeam(String teamName, String creatorUserId) throws SQLException {
        // Validate team name
        if (teamName == null || teamName.trim().isEmpty()) {
            throw new IllegalArgumentException("Team name cannot be empty");
        }
        
        // Check if user is already in a team
        if (teamDao.isUserInTeam(creatorUserId)) {
            throw new IllegalArgumentException("You are already in a team. A user can only be in one team.");
        }
        
        return teamDao.createTeam(teamName, creatorUserId);
    }
    
    public boolean joinTeam(String teamId, String userId) throws SQLException {
        // Validate team exists
        if (!teamDao.teamExists(teamId)) {
            throw new IllegalArgumentException("Team ID not found");
        }
        
        // Check if user is already in a team
        if (teamDao.isUserInTeam(userId)) {
            throw new IllegalArgumentException("You are already in a team. A user can only be in one team.");
        }
        
        try {
            return teamDao.addMemberToTeam(teamId, userId);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new IllegalArgumentException("You are already in this team or another team");
        }
    }
    
    public Team getUserTeam(String userId) throws SQLException {
        return teamDao.getUserTeam(userId);
    }
    
    public List<TeamMember> getTeamMembers(String teamId) throws SQLException {
        return teamDao.getTeamMembers(teamId);
    }
    
    public boolean isUserTeamLeader(String userId, String teamId) throws SQLException {
        String role = teamDao.getUserRoleInTeam(userId, teamId);
        return "TEAM_LEADER".equals(role);
    }
}