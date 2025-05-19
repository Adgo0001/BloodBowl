package com.example.bloodbowl.DAO;

import com.example.bloodbowl.DatabaseUtil;
import com.example.bloodbowl.model.Coach;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CoachDAO {

    // Get all coaches
    public List<Coach> getAllCoaches() throws SQLException {
        List<Coach> coaches = new ArrayList<>();
        String sql = "SELECT coach_id, name, email FROM Coaches";

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Coach coach = new Coach(
                        rs.getInt("coach_id"),
                        rs.getString("name"),
                        rs.getString("email")
                );
                coaches.add(coach);
            }
        }

        return coaches;
    }

    // Get coach by ID
    public Coach getCoachById(int coachId) throws SQLException {
        String sql = "SELECT coach_id, name, email FROM Coaches WHERE coach_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, coachId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Coach(
                            rs.getInt("coach_id"),
                            rs.getString("name"),
                            rs.getString("email")
                    );
                }
            }
        }

        return null;
    }

    // Create a new coach
    public int createCoach(Coach coach) throws SQLException {
        String sql = "INSERT INTO Coaches (name, email) VALUES (?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, coach.getName());
            pstmt.setString(2, coach.getEmail());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating coach failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating coach failed, no ID obtained.");
                }
            }
        }
    }

    // Update an existing coach
    public boolean updateCoach(Coach coach) throws SQLException {
        String sql = "UPDATE Coaches SET name = ?, email = ? WHERE coach_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, coach.getName());
            pstmt.setString(2, coach.getEmail());
            pstmt.setInt(3, coach.getCoachId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    // Delete a coach
    public boolean deleteCoach(int coachId) throws SQLException {
        String sql = "DELETE FROM Coaches WHERE coach_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, coachId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }
}