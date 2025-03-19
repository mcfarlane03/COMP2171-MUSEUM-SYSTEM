public class BaseStaff {
    private String name;
    private String role;
    private String gender;
    private int ID;

    public BaseStaff() {
    } // default constructor

    public BaseStaff(String name, String role, String gender, int ID) {
        this.name = name;
        this.role = role;
        this.gender = gender;
        this.ID = ID;
    }

    public String getName() {
        return this.name;
    }

    public String getRole() {
        return this.role;
    }

    public String getGender() {
        return this.gender;
    }

    public int getID() {
        return this.ID;
    }

    public int setID(int ID) {
        return this.ID = ID;
    }
}