import java.sql.*;

public class EmployeeDAO {
    
    /**
     * Add a new employee to the database
     * @param employee The employee to add
     * @return true if the operation was successful, false otherwise
     */
    public boolean addEmployee(BaseStaff employee) {
        String sql = "INSERT INTO employees (id, name, role, gender, password_hash) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, employee.getId());
            stmt.setString(2, employee.getName());
            stmt.setString(3, employee.getRole());
            stmt.setString(4, String.valueOf(employee.getGender()));
            stmt.setString(5, employee.getPasswordHash());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding employee: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check if an employee with the given ID already exists
     * @param id The employee ID to check
     * @return true if the employee exists, false otherwise
     */
    public boolean employeeExists(int id) {
        String sql = "SELECT COUNT(*) FROM employees WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking if employee exists: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Get an employee by their ID
     * @param id The employee ID
     * @return The Employee object, or null if not found
     */
    public BaseStaff getEmployeeById(int id) {
        String sql = "SELECT * FROM employees WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new BaseStaff(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("role"),
                        rs.getString("gender").charAt(0),
                        rs.getString("password_hash")
                    );
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting employee: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Verify employee credentials
     * @param id The employee ID
     * @param passwordHash The hashed password to verify
     * @return true if credentials are valid, false otherwise
     */
    public boolean verifyCredentials(int id, String passwordHash) {
        String sql = "SELECT password_hash FROM employees WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password_hash");
                    return storedHash.equals(passwordHash);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error verifying credentials: " + e.getMessage());
        }
        
        return false;
    }
}