import java.io.IOException;

public class ArtifactController {


    public ArtifactController(){

    }

    public static void storeArtifact(String name, int ID, String Description, String status, double weight, double price,
    String arrivalDate) throws IOException{
        DatabaseController.store_to_artifact_database( name, ID,  Description, status, weight, price, arrivalDate);

    }

    

}
