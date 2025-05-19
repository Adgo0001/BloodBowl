package com.example.bloodbowl.DAO;

import com.example.bloodbowl.DatabaseUtil;
import com.example.bloodbowl.Model.Race;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RaceDAO {

    // Get all races
    public List<Race> getAllRaces() throws SQLException {
        List<Race> races = new ArrayList<>();
        String sql = "SELECT race_id, name, is_pariah FROM Races";

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Race race = new Race(
                        rs.getInt("race_id"),
                        rs.getString("name"),
                        rs.getBoolean("is_pariah")
                );
                races.add(race);
            }
        }

        return races;
    }

    // Get all Pariah races
    public List<Race> getPariaRaces() throws SQLException {
        List<Race> races = new ArrayList<>();
        String sql = "SELECT race_id, name, is_pariah FROM Races WHERE is_pariah = TRUE";

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Race race = new Race(
                        rs.getInt("race_id"),
                        rs.getString("name"),
                        rs.getBoolean("is_pariah")
                );
                races.add(race);
            }
        }

        return races;
    }

    // Get race by ID
    public Race getRaceById(int raceId) throws SQLException {
        String sql = "SELECT race_id, name, is_pariah FROM Races WHERE race_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, raceId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Race(
                            rs.getInt("race_id"),
                            rs.getString("name"),
                            rs.getBoolean("is_pariah")
                    );
                }
            }
        }

        return null;
    }

    // Create a new race
    public int createRace(Race race) throws SQLException {
        String sql = "INSERT INTO Races (name, is_pariah) VALUES (?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, race.getName());
            pstmt.setBoolean(2, race.isPariah());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating race failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating race failed, no ID obtained.");
                }
            }
        }
    }

    // Update an existing race
    public boolean updateRace(Race race) throws SQLException {
        String sql = "UPDATE Races SET name = ?, is_pariah = ? WHERE race_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, race.getName());
            pstmt.setBoolean(2, race.isPariah());
            pstmt.setInt(3, race.getRaceId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    // Delete a race
    public boolean deleteRace(int raceId) throws SQLException {
        String sql = "DELETE FROM Races WHERE race_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, raceId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }
}