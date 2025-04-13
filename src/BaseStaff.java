public class BaseStaff {
    private int ID;
    private String name;
    private String role;
    private char gender;
    private String passwordHash;
    private boolean isManager; // New manager flag
    
    public BaseStaff(int id, String name, String role, char gender, String passwordHash) {
        this.ID = id;
        this.name = name;
        this.role = role;
        this.gender = gender;
        this.passwordHash = passwordHash;
        // Set isManager based on role
        this.isManager = "Manager".equalsIgnoreCase(role);
    }
    
    // Getters
    public int getId() {
        return ID;
    }
    
    public String getName() {
        return name;
    }
    
    public String getRole() {
        return role;
    }
    
    public char getGender() {
        return gender;
    }
    
    public String getPasswordHash() {
        return passwordHash;
    }
    
    public boolean isManager() {
        return isManager;
    }
    
    // Setters
    public void setId(int id) {
        this.ID = id;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setRole(String role) {
        this.role = role;
        // Update isManager when role changes
        this.isManager = "Manager".equalsIgnoreCase(role);
    }
    
    public void setGender(char gender) {
        this.gender = gender;
    }
    
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    
    public void setManager(boolean isManager) {
        this.isManager = isManager;
    }
}