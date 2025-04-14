import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArtifactDAO {
    
    /**
     * Add a new artifact to the database
     * @param artifact The artifact to add
     * @return true if the operation was successful, false otherwise
     */
    public boolean addArtifact(Artifact artifact) {
        String sql = "INSERT INTO artifacts (id, name, description, status, price, weight, arrival_date) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, artifact.getID());
            stmt.setString(2, artifact.getName());
            stmt.setString(3, artifact.getDescription());
            stmt.setString(4, artifact.getStatus());
            stmt.setDouble(5, artifact.getPrice());
            stmt.setDouble(6, artifact.getWeight());
            stmt.setString(7, artifact.getArrivalDate());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding artifact: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Update an existing artifact in the database
     * @param artifact The artifact with updated information
     * @return true if the operation was successful, false otherwise
     */
    public boolean updateArtifact(Artifact artifact) {
        String sql = "UPDATE artifacts SET name = ?, description = ?, status = ?, " +
                     "price = ?, weight = ?, arrival_date = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, artifact.getName());
            stmt.setString(2, artifact.getDescription());
            stmt.setString(3, artifact.getStatus());
            stmt.setDouble(4, artifact.getPrice());
            stmt.setDouble(5, artifact.getWeight());
            stmt.setString(6, artifact.getArrivalDate());
            stmt.setInt(7, artifact.getID());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating artifact: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete an artifact from the database
     * @param id The ID of the artifact to delete
     * @return true if the operation was successful, false otherwise
     */
    public boolean deleteArtifact(int id) {
        String sql = "DELETE FROM artifacts WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting artifact: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get an artifact by its ID
     * @param id The ID of the artifact to retrieve
     * @return The Artifact object, or null if not found
     */
    public Artifact getArtifactById(int id) {
        String sql = "SELECT * FROM artifacts WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return createArtifactFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving artifact: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Get all artifacts in the database
     * @return A List of Artifact objects
     */
    public List<Artifact> getAllArtifacts() {
        List<Artifact> artifacts = new ArrayList<>();
        String sql = "SELECT * FROM artifacts";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                artifacts.add(createArtifactFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving all artifacts: " + e.getMessage());
        }
        
        return artifacts;
    }
    
    /**
     * Search for artifacts by name (partial match)
     * @param searchTerm The search term to look for in artifact names
     * @return A List of matching Artifact objects
     */
    public List<Artifact> searchArtifactsByName(String searchTerm) {
        List<Artifact> artifacts = new ArrayList<>();
        String sql = "SELECT * FROM artifacts WHERE name LIKE ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + searchTerm + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    artifacts.add(createArtifactFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching for artifacts: " + e.getMessage());
        }
        
        return artifacts;
    }
    
    /**
     * Search for artifacts by status
     * @param status The status to search for
     * @return A List of matching Artifact objects
     */
    public List<Artifact> getArtifactsByStatus(String status) {
        List<Artifact> artifacts = new ArrayList<>();
        String sql = "SELECT * FROM artifacts WHERE status = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    artifacts.add(createArtifactFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving artifacts by status: " + e.getMessage());
        }
        
        return artifacts;
    }
    
    /**
     * Get artifacts within a price range
     * @param minPrice The minimum price
     * @param maxPrice The maximum price
     * @return A List of matching Artifact objects
     */
    public List<Artifact> getArtifactsByPriceRange(double minPrice, double maxPrice) {
        List<Artifact> artifacts = new ArrayList<>();
        String sql = "SELECT * FROM artifacts WHERE price BETWEEN ? AND ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, minPrice);
            stmt.setDouble(2, maxPrice);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    artifacts.add(createArtifactFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving artifacts by price range: " + e.getMessage());
        }
        
        return artifacts;
    }
    
    /**
     * Check if an artifact with the given ID already exists
     * @param id The artifact ID to check
     * @return true if the artifact exists, false otherwise
     */
    public boolean artifactExists(int id) {
        String sql = "SELECT COUNT(*) FROM artifacts WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking if artifact exists: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Helper method to create an Artifact object from a ResultSet
     */
    private Artifact createArtifactFromResultSet(ResultSet rs) throws SQLException {
        return new Artifact(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getString("status"),
            rs.getDouble("weight"),
            rs.getDouble("price"),
            rs.getString("arrival_date")
        );
    }
}