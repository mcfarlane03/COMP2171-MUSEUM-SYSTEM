public class Artifact {
    private String name;
    private String Description;
    private String status;
    private double price;
    private double weight;
    private String arrivalDate;
    private int ID;

    public Artifact() {
    } // default constructor

    public Artifact(int ID, String name, String Description, String status, double weight, double price,
            String arrivalDate) {
        this.ID = ID;
        this.name = name;
        this.Description = Description;
        this.status = status;
        this.price = price;
        this.weight = weight;
        this.arrivalDate = arrivalDate;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.Description;
    }

    public int getID() {
        return this.ID;
    }

    public String getStatus() {
        return this.status;
    }

    public double getPrice() {
        return this.price;
    }

    public double getWeight() {
        return this.weight;
    }

    public String getArrivalDate() {
        return this.arrivalDate;
    }

    public double setPrice(double price) {
        return this.price = price;
    }

    public double setWeight(double weight) {
        return this.weight = weight;
    }

    public int getDay(String arrivalDate) {
        return Integer.parseInt(arrivalDate.split("/")[0]);

    }

    public int getMonth(String arrivalDate) {
        return Integer.parseInt(arrivalDate.split("/")[1]);

    }

    public int getYear(String arrivalDate) {
        return Integer.parseInt(arrivalDate.split("/")[2]);

    }

}