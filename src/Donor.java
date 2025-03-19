public class Donor {
    private String name;
    private int ID;
    private String contact;
    private String ArtifactsDonated;
    private int numArtifacts;

    public Donor() {
    }

    public Donor(String name, int ID, String contact, String ArtifactsDonated, int numArtifacts) {
        this.name = name;
        this.ID = ID;
        this.contact = contact;
        this.ArtifactsDonated = ArtifactsDonated;
        this.numArtifacts = numArtifacts;
    }

    public String getName() {
        return this.name;
    }

    public int getID() {
        return this.ID;
    }

    public String getContact() {
        return this.contact;
    }

    public String getArtifactsDonated() {
        return this.ArtifactsDonated;
    }

    public int getNumArtifacts() {
        return this.numArtifacts;
    }


}
