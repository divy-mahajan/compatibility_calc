package db;

import model.Person;
import java.sql.*;

/**
 * UserDAO.java
 * Handles all database operations for the users table.
 */
public class UserDAO {

    // Inserts a new user and returns the generated ID
    public static int insertUser(String name, String personalityCode) {
        String sql = "INSERT INTO users (name, personality_code) VALUES (?, ?)";
        try {
            PreparedStatement ps = DBConnection.getConnection()
                    .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setString(2, personalityCode);
            ps.executeUpdate();

            // Retrieve the auto-generated ID
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error inserting user: " + e.getMessage());
        }
        return -1; // Returns -1 if insert failed
    }

    // Retrieves a user by ID — useful for testing and the top-5 query
    public static String getUserNameById(int id) {
        String sql = "SELECT name FROM users WHERE id = ?";
        try {
            PreparedStatement ps = DBConnection.getConnection()
                    .prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving user: " + e.getMessage());
        }
        return null;
    }
}
