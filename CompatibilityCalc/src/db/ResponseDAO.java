package db;

import java.sql.*;
import java.util.Map;

/**
 * ResponseDAO.java
 * Logs each user's raw question answer values for auditing.
 */
public class ResponseDAO {

    // Inserts a single question response
    public static void insertResponse(int userId, String questionId, int optionValue) {
        String sql = "INSERT INTO question_responses (user_id, question_id, option_value) " +
                "VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = DBConnection.getConnection()
                    .prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setString(2, questionId);
            ps.setInt(3, optionValue);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error inserting response: " + e.getMessage());
        }
    }

    // Inserts all 10 question responses for a user in one batch
    public static void insertAll(int userId, Map<String, Integer> answers) {
        String sql = "INSERT INTO question_responses (user_id, question_id, option_value) " +
                "VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = DBConnection.getConnection()
                    .prepareStatement(sql);
            for (Map.Entry<String, Integer> entry : answers.entrySet()) {
                ps.setInt(1, userId);
                ps.setString(2, entry.getKey());
                ps.setInt(3, entry.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            System.out.println("Error inserting responses: " + e.getMessage());
        }
    }
}