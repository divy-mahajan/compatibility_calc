package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResultDAO {

    // 🔹 Insert result safely
    public static void insertResult(int user1Id, int user2Id, int score) {

        String sql = "INSERT INTO results (user1_id, user2_id, score) VALUES (?, ?, ?)";

        Connection conn = DBConnection.getConnection();

        if (conn == null) {
            System.out.println("⚠ DB not connected. Skipping result save.");
            return;
        }

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, user1Id);
            ps.setInt(2, user2Id);
            ps.setInt(3, score);

            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error inserting result: " + e.getMessage());
        }
    }

    // 🔹 Get top 5 pairs safely
    public static List<String> getTopFivePairs() {

        List<String> pairs = new ArrayList<>();

        String sql =
                "SELECT u1.name AS name1, u2.name AS name2, r.score " +
                        "FROM results r " +
                        "JOIN users u1 ON r.user1_id = u1.id " +
                        "JOIN users u2 ON r.user2_id = u2.id " +
                        "ORDER BY r.score DESC " +
                        "LIMIT 5";

        Connection conn = DBConnection.getConnection();

        if (conn == null) {
            System.out.println("⚠ DB not connected. Returning empty list.");
            return pairs;
        }

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                String name1 = rs.getString("name1");
                String name2 = rs.getString("name2");
                int score = rs.getInt("score");

                pairs.add(name1 + " & " + name2 + " — " + score + "%");
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving top pairs: " + e.getMessage());
        }

        return pairs;
    }
}