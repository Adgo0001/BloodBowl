package com.example.bloodbowl.DAO;

import com.example.bloodbowl.DatabaseUtil;
import com.example.bloodbowl.Model.Tournament;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TournamentDAO {

    // Get all tournaments
    public List<Tournament> getAllTournaments() throws SQLException {
        List<Tournament> tournaments = new ArrayList<>();
        String sql = "SELECT tournament_id, name, start_date, end_date, location, participant_count, tournament_days FROM Tournaments";

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Tournament tournament = new Tournament(
                        rs.getInt("tournament_id"),
                        rs.getString("name"),
                        rs.getDate("start_date"),
                        rs.getDate("end_date"),
                        rs.getString("location"),
                        rs.getInt("participant_count"),
                        rs.getInt("tournament_days")
                );
                tournaments.add(tournament);
            }
        }

        return tournaments;
    }

    // Get tournament by ID
    public Tournament getTournamentById(int tournamentId) throws SQLException {
        String sql = "SELECT tournament_id, name, start_date, end_date, location, participant_count, tournament_days FROM Tournaments WHERE tournament_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, tournamentId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Tournament(
                            rs.getInt("tournament_id"),
                            rs.getString("name"),
                            rs.getDate("start_date"),
                            rs.getDate("end_date"),
                            rs.getString("location"),
                            rs.getInt("participant_count"),
                            rs.getInt("tournament_days")
                    );
                }
            }
        }

        return null;
    }

    // Get tournaments in a specific year
    public List<Tournament> getTournamentsByYear(int year) throws SQLException {
        List<Tournament> tournaments = new ArrayList<>();
        String sql = "SELECT tournament_id, name, start_date, end_date, location, participant_count, tournament_days " +
                "FROM Tournaments WHERE YEAR(start_date) = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, year);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Tournament tournament = new Tournament(
                            rs.getInt("tournament_id"),
                            rs.getString("name"),
                            rs.getDate("start_date"),
                            rs.getDate("end_date"),
                            rs.getString("location"),
                            rs.getInt("participant_count"),
                            rs.getInt("tournament_days")
                    );
                    tournaments.add(tournament);
                }
            }
        }

        return tournaments;
    }

    // Create a new tournament
    public int createTournament(Tournament tournament) throws SQLException {
        String sql = "INSERT INTO Tournaments (name, start_date, end_date, location, participant_count, tournament_days) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, tournament.getName());
            pstmt.setDate(2, new java.sql.Date(tournament.getStartDate().getTime()));
            pstmt.setDate(3, new java.sql.Date(tournament.getEndDate().getTime()));
            pstmt.setString(4, tournament.getLocation());
            pstmt.setInt(5, tournament.getParticipantCount());
            pstmt.setInt(6, tournament.getTournamentDays());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating tournament failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating tournament failed, no ID obtained.");
                }
            }
        }
    }

    // Update an existing tournament
    public boolean updateTournament(Tournament tournament) throws SQLException {
        String sql = "UPDATE Tournaments SET name = ?, start_date = ?, end_date = ?, location = ?, " +
                "participant_count = ?, tournament_days = ? WHERE tournament_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, tournament.getName());
            pstmt.setDate(2, new java.sql.Date(tournament.getStartDate().getTime()));
            pstmt.setDate(3, new java.sql.Date(tournament.getEndDate().getTime()));
            pstmt.setString(4, tournament.getLocation());
            pstmt.setInt(5, tournament.getParticipantCount());
            pstmt.setInt(6, tournament.getTournamentDays());
            pstmt.setInt(7, tournament.getTournamentId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    // Delete a tournament
    public boolean deleteTournament(int tournamentId) throws SQLException {
        String sql = "DELETE FROM Tournaments WHERE tournament_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, tournamentId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }
}