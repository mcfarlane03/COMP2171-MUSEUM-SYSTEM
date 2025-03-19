import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class DatabaseController {
    public DatabaseController() {

    }

    public static void store_to_artifact_database(String name, int ID, String Description, String status, double weight,
            double price,
            String arrivalDate) throws IOException {
        String text;

        // creates a file named stud_records.txt
        File file = new File("artifact_catalogue.txt");

        // needed in order to handle appending data to file if file already exists:
        FileWriter fileWriter = new FileWriter(file, file.exists());
        BufferedWriter output = new BufferedWriter(fileWriter);

        // determining which button is selected for gender:
        text = name.trim() + " " + ID + " " + Description.trim() + " " + status + " "
                + weight + " " + price + " " + arrivalDate.trim();

        output.write(text);

        // adding new line for next record to be appended:
        if (file.exists())
            output.newLine();

        try {
            output.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            fileWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (file.exists())
            System.out.println("Text has been appended to the file: artifact_catalogue.txt");
        else
            System.out.println("Text has been written to a new file: artifact_catalogue.txt");
        // note this does not display on the GUI but the terminal:

    }



}
