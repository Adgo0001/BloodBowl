package com.example.bloodbowl.DAO;

import com.example.bloodbowl.DatabaseUtil;
import com.example.bloodbowl.Model.Team;
import com.example.bloodbowl.Model.Race;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeamDAO {

    private RaceDAO raceDAO = new RaceDAO();

    // Get all teams
    public List<Team> getAllTeams() throws SQLException {
        List<Team> teams = new ArrayList<>();
        String sql = "SELECT team_id, name, race_id, coach_id FROM Teams";

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Team team = new Team(
                        rs.getInt("team_id"),
                        rs.getString("name"),
                        rs.getInt("race_id"),
                        rs.getInt("coach_id")
                );

                // Load race details
                Race race = raceDAO.getRaceById(team.getRaceId());
                team.setRace(race);

                teams.add(team);
            }
        }

        return teams;
    }

    // Get team by ID
    public Team getTeamById(int teamId) throws SQLException {
        String sql = "SELECT team_id, name, race_id, coach_id FROM Teams WHERE team_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, teamId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Team team = new Team(
                            rs.getInt("team_id"),
                            rs.getString("name"),
                            rs.getInt("race_id"),
                            rs.getInt("coach_id")
                    );

                    // Load race details
                    Race race = raceDAO.getRaceById(team.getRaceId());
                    team.setRace(race);

                    return team;
                }
            }
        }

        return null;
    }

    // Get teams by coach ID
    public List<Team> getTeamsByCoachId(int coachId) throws SQLException {
        List<Team> teams = new ArrayList<>();
        String sql = "SELECT team_id, name, race_id, coach_id FROM Teams WHERE coach_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, coachId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Team team = new Team(
                            rs.getInt("team_id"),
                            rs.getString("name"),
                            rs.getInt("race_id"),
                            rs.getInt("coach_id")
                    );

                    // Load race details
                    Race race = raceDAO.getRaceById(team.getRaceId());
                    team.setRace(race);

                    teams.add(team);
                }
            }
        }

        return teams;
    }

    // Create a new team
    public int createTeam(Team team) throws SQLException {
        String sql = "INSERT INTO Teams (name, race_id, coach_id) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, team.getName());
            pstmt.setInt(2, team.getRaceId());
            pstmt.setInt(3, team.getCoachId());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating team failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating team failed, no ID obtained.");
                }
            }
        }
    }

    // Update an existing team
    public boolean updateTeam(Team team) throws SQLException {
        String sql = "UPDATE Teams SET name = ?, race_id = ?, coach_id = ? WHERE team_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, team.getName());
            pstmt.setInt(2, team.getRaceId());
            pstmt.setInt(3, team.getCoachId());
            pstmt.setInt(4, team.getTeamId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    // Delete a team
    public boolean deleteTeam(int teamId) throws SQLException {
        String sql = "DELETE FROM Teams WHERE team_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, teamId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }
}