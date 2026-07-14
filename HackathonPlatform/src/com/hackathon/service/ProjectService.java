package com.hackathon.service;

import com.hackathon.dao.ProjectDao;
import com.hackathon.dao.TeamDao;
import com.hackathon.model.Project;
import com.hackathon.model.Team;

import java.sql.SQLException;
import java.util.List;

public class ProjectService {
    private ProjectDao projectDao;
    private TeamDao teamDao;
    
    public ProjectService() {
        this.projectDao = new ProjectDao();
        this.teamDao = new TeamDao();
    }
    
    public Project submitProject(String userId, String title, String description) throws SQLException {
        // Validate inputs
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Project title cannot be empty");
        }
        
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Project description cannot be empty");
        }
        
        // Get user's team
        Team team = teamDao.getUserTeam(userId);
        if (team == null) {
            throw new IllegalArgumentException("You must be in a team to submit a project");
        }
        
        // Check if user is team leader
        String role = teamDao.getUserRoleInTeam(userId, team.getTeamId());
        if (!"TEAM_LEADER".equals(role)) {
            throw new IllegalArgumentException("Only the TEAM_LEADER can submit a project");
        }
        
        // Check if team has already submitted a project
        if (projectDao.teamHasSubmittedProject(team.getTeamId())) {
            throw new IllegalArgumentException("Your team has already submitted a project");
        }
        
        return projectDao.submitProject(team.getTeamId(), title, description);
    }
    
    public List<Project> getAllProjects() throws SQLException {
        return projectDao.getAllProjects();
    }
    
    public boolean updateProjectStatus(String projectId, String newStatus) throws SQLException {
        // Validate status
        if (!newStatus.equals("APPROVED") && !newStatus.equals("REJECTED") && !newStatus.equals("PENDING")) {
            throw new IllegalArgumentException("Invalid status. Use APPROVED, REJECTED, or PENDING");
        }
        
        return projectDao.updateProjectStatus(projectId, newStatus);
    }
    
    public Project getProjectByTeamId(String teamId) throws SQLException {
        return projectDao.getProjectByTeamId(teamId);
    }
}