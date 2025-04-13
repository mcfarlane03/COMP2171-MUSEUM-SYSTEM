import java.sql.*;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/museum_db";
    private static final String USER = "change-to-your-user ";
    private static final String PASSWORD = "change-to-your-password";
    
    private static Connection connection = null;
    
    /**
     * Get a connection to the database
     * @return Connection object
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Load the MySQL JDBC driver
                Class.forName("com.mysql.cj.jdbc.Driver");
                
                // Create the connection
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL JDBC Driver not found", e);
            }
        }
        return connection;
    }
    
    /**
     * Close the database connection
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
    
    /**
     * Initialize the database by creating the necessary tables if they don't exist
     */
    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Create the employees table if it doesn't exist
            String createEmployeesTableSQL = 
                "CREATE TABLE IF NOT EXISTS employees (" +
                "id INT PRIMARY KEY, " +
                "name VARCHAR(100) NOT NULL, " +
                "role VARCHAR(50) NOT NULL, " +
                "gender CHAR(1) NOT NULL, " +
                "password_hash VARCHAR(64) NOT NULL)";
            
            stmt.execute(createEmployeesTableSQL);
            
            // Create the artifacts table if it doesn't exist
            String createArtifactsTableSQL = 
                "CREATE TABLE IF NOT EXISTS artifacts (" +
                "id INT PRIMARY KEY, " +
                "name VARCHAR(100) NOT NULL, " +
                "description TEXT, " +
                "status VARCHAR(50) NOT NULL, " +
                "price DOUBLE NOT NULL, " +
                "weight DOUBLE NOT NULL, " +
                "arrival_date VARCHAR(20) NOT NULL)";
            
            stmt.execute(createArtifactsTableSQL);
            
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }
}