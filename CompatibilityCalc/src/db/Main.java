package db;

import java.sql.Connection;

public class Main {

    public static void main(String[] args) {

        Connection conn = DBConnection.getConnection();

        if (conn != null) {
            System.out.println("✓ Connection successful — DB is live.");
        } else {
            System.out.println("✗ Connection failed — check errors above.");
        }

        DBConnection.closeConnection();
    }
}
