package db;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static Connection connection = null;

    private DBConnection() {}

    public static Connection getConnection() {

        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            }
            System.out.println("Looking for config at: " + new java.io.File("config.properties").getAbsolutePath());

            Properties props = new Properties();
            FileInputStream fis = new FileInputStream("config.properties");
            props.load(fis);
            fis.close();

            String url  = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String pass = props.getProperty("db.password");


            if (url == null || user == null || pass == null) {
                System.out.println("Error: missing properties in config.properties");
                return null;
            }

            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, pass);
            System.out.println("Database connected successfully.");
            return connection;

        } catch (IOException e) {
            System.out.println("Could not read config.properties: " + e.getMessage());
            return null;
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL driver not found: " + e.getMessage());
            return null;
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
            return null;
        }
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connection closed.");
            } catch (SQLException e) {
                System.out.println("Error closing: " + e.getMessage());
            }
        }
    }
}