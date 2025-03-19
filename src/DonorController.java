import java.io.IOException;
public class DonorController {

    public DonorController(){

    }

    public static void storeDonor(String name, int ID, String contact, String ArtifactsDonated, int numArtifacts) throws IOException {
        DatabaseController.AddDonor(name, ID, contact, ArtifactsDonated, numArtifacts);
    }

}
