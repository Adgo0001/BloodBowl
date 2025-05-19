package com.example.bloodbowl.DAO;

import com.example.bloodbowl.DatabaseUtil;
import com.example.bloodbowl.Model.TournamentResult;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TournamentResultDAO {

    // Get all tournament results
    public List<TournamentResult> getAllResults() throws SQLException {
        List<TournamentResult> results = new ArrayList<>();
        String sql = "SELECT result_id, tournament_id, coach_id, team_id, placement, " +
                "matches_played, matches_won, matches_drawn, matches_lost, " +
                "touchdowns_for, touchdowns_against, casualties_for, casualties_against " +
                "FROM TournamentResults";

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                TournamentResult result = new TournamentResult(
                        rs.getInt("result_id"),
                        rs.getInt("tournament_id"),
                        rs.getInt("coach_id"),
                        rs.getInt("team_id"),
                        rs.getInt("placement"),
                        rs.getInt("matches_played"),
                        rs.getInt("matches_won"),
                        rs.getInt("matches_drawn"),
                        rs.getInt("matches_lost"),
                        rs.getInt("touchdowns_for"),
                        rs.getInt("touchdowns_against"),
                        rs.getInt("casualties_for"),
                        rs.getInt("casualties_against")
                );
                results.add(result);
            }
        }

        return results;
    }

    // Get tournament result by ID
    public TournamentResult getResultById(int resultId) throws SQLException {
        String sql = "SELECT result_id, tournament_id, coach_id, team_id, placement, " +
                "matches_played, matches_won, matches_drawn, matches_lost, " +
                "touchdowns_for, touchdowns_against, casualties_for, casualties_against " +
                "FROM TournamentResults WHERE result_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, resultId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new TournamentResult(
                            rs.getInt("result_id"),
                            rs.getInt("tournament_id"),
                            rs.getInt("coach_id"),
                            rs.getInt("team_id"),
                            rs.getInt("placement"),
                            rs.getInt("matches_played"),
                            rs.getInt("matches_won"),
                            rs.getInt("matches_drawn"),
                            rs.getInt("matches_lost"),
                            rs.getInt("touchdowns_for"),
                            rs.getInt("touchdowns_against"),
                            rs.getInt("casualties_for"),
                            rs.getInt("casualties_against")
                    );
                }
            }
        }

        return null;
    }

    // Get results by tournament ID
    public List<TournamentResult> getResultsByTournamentId(int tournamentId) throws SQLException {
        List<TournamentResult> results = new ArrayList<>();
        String sql = "SELECT result_id, tournament_id, coach_id, team_id, placement, " +
                "matches_played, matches_won, matches_drawn, matches_lost, " +
                "touchdowns_for, touchdowns_against, casualties_for, casualties_against " +
                "FROM TournamentResults WHERE tournament_id = ? ORDER BY placement ASC";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, tournamentId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    TournamentResult result = new TournamentResult(
                            rs.getInt("result_id"),
                            rs.getInt("tournament_id"),
                            rs.getInt("coach_id"),
                            rs.getInt("team_id"),
                            rs.getInt("placement"),
                            rs.getInt("matches_played"),
                            rs.getInt("matches_won"),
                            rs.getInt("matches_drawn"),
                            rs.getInt("matches_lost"),
                            rs.getInt("touchdowns_for"),
                            rs.getInt("touchdowns_against"),
                            rs.getInt("casualties_for"),
                            rs.getInt("casualties_against")
                    );
                    results.add(result);
                }
            }
        }

        return results;
    }

    // Get results by coach ID
    public List<TournamentResult> getResultsByCoachId(int coachId) throws SQLException {
        List<TournamentResult> results = new ArrayList<>();
        String sql = "SELECT result_id, tournament_id, coach_id, team_id, placement, " +
                "matches_played, matches_won, matches_drawn, matches_lost, " +
                "touchdowns_for, touchdowns_against, casualties_for, casualties_against " +
                "FROM TournamentResults WHERE coach_id = ? ORDER BY tournament_id ASC";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, coachId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    TournamentResult result = new TournamentResult(
                            rs.getInt("result_id"),
                            rs.getInt("tournament_id"),
                            rs.getInt("coach_id"),
                            rs.getInt("team_id"),
                            rs.getInt("placement"),
                            rs.getInt("matches_played"),
                            rs.getInt("matches_won"),
                            rs.getInt("matches_drawn"),
                            rs.getInt("matches_lost"),
                            rs.getInt("touchdowns_for"),
                            rs.getInt("touchdowns_against"),
                            rs.getInt("casualties_for"),
                            rs.getInt("casualties_against")
                    );
                    results.add(result);
                }
            }
        }

        return results;
    }

    // Create a new tournament result
    public int createResult(TournamentResult result) throws SQLException {
        String sql = "INSERT INTO TournamentResults (tournament_id, coach_id, team_id, placement, " +
                "matches_played, matches_won, matches_drawn, matches_lost, " +
                "touchdowns_for, touchdowns_against, casualties_for, casualties_against) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, result.getTournamentId());
            pstmt.setInt(2, result.getCoachId());
            pstmt.setInt(3, result.getTeamId());
            pstmt.setInt(4, result.getPlacement());
            pstmt.setInt(5, result.getMatchesPlayed());
            pstmt.setInt(6, result.getMatchesWon());
            pstmt.setInt(7, result.getMatchesDrawn());
            pstmt.setInt(8, result.getMatchesLost());
            pstmt.setInt(9, result.getTouchdownsFor());
            pstmt.setInt(10, result.getTouchdownsAgainst());
            pstmt.setInt(11, result.getCasualtiesFor());
            pstmt.setInt(12, result.getCasualtiesAgainst());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating tournament result failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating tournament result failed, no ID obtained.");
                }
            }
        }
    }

    // Update an existing tournament result
    public boolean updateResult(TournamentResult result) throws SQLException {
        String sql = "UPDATE TournamentResults SET tournament_id = ?, coach_id = ?, team_id = ?, placement = ?, " +
                "matches_played = ?, matches_won = ?, matches_drawn = ?, matches_lost = ?, " +
                "touchdowns_for = ?, touchdowns_against = ?, casualties_for = ?, casualties_against = ? " +
                "WHERE result_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, result.getTournamentId());
            pstmt.setInt(2, result.getCoachId());
            pstmt.setInt(3, result.getTeamId());
            pstmt.setInt(4, result.getPlacement());
            pstmt.setInt(5, result.getMatchesPlayed());
            pstmt.setInt(6, result.getMatchesWon());
            pstmt.setInt(7, result.getMatchesDrawn());
            pstmt.setInt(8, result.getMatchesLost());
            pstmt.setInt(9, result.getTouchdownsFor());
            pstmt.setInt(10, result.getTouchdownsAgainst());
            pstmt.setInt(11, result.getCasualtiesFor());
            pstmt.setInt(12, result.getCasualtiesAgainst());
            pstmt.setInt(13, result.getResultId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    // Delete a tournament result
    public boolean deleteResult(int resultId) throws SQLException {
        String sql = "DELETE FROM TournamentResults WHERE result_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, resultId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }
}