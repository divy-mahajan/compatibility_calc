package db;

import model.Trait;
import java.sql.*;
import java.util.List;

/**
 * TraitDAO.java
 * Handles all database operations for the traits table.
 */
public class TraitDAO {

    // Inserts a single trait row for a user
    public static void insertTrait(int userId, String traitName, int value) {
        String sql = "INSERT INTO traits (user_id, trait_name, value) VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = DBConnection.getConnection()
                    .prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setString(2, traitName);
            ps.setInt(3, value);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error inserting trait: " + e.getMessage());
        }
    }

    // Inserts all 10 traits for a user in one batch — more efficient
    // than calling insertTrait() 10 times separately
    public static void insertAll(int userId, List<Trait> traits) {
        String sql = "INSERT INTO traits (user_id, trait_name, value) VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = DBConnection.getConnection()
                    .prepareStatement(sql);
            for (Trait t : traits) {
                ps.setInt(1, userId);
                ps.setString(2, t.getName());
                ps.setInt(3, t.getValue());
                ps.addBatch(); // Queue each row
            }
            ps.executeBatch(); // Send all rows in one round trip
        } catch (SQLException e) {
            System.out.println("Error inserting traits: " + e.getMessage());
        }
    }
}