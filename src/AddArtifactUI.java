import java.awt.*;
import javax.swing.*;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddArtifactUI extends JFrame {
    private JPanel dis_panel = new JPanel();
    private JPanel com_panel = new JPanel();
    private JPanel status_panel = new JPanel();

    private JCheckBox Sold = new JCheckBox();
    private JCheckBox Excavated = new JCheckBox();
    private JCheckBox Donated = new JCheckBox();

    private JTextField Name = new JTextField(50);
    private JTextField Description = new JTextField(350);
    private JTextField ArrivalDate = new JTextField(50);
    private JTextField ID = new JTextField(10);
    private JTextField Price = new JTextField(10);
    private JTextField Weight = new JTextField(10);

    private JButton close = new JButton("Close");
    private JButton save = new JButton("Save Artifact");

    public AddArtifactUI() {
        // setTitle("Add a New Artifact");
        dis_panel.setLayout(new GridLayout(7, 6));
        // dis_panel.setLayout(new BoxLayout(dis_panel, BoxLayout.Y_AXIS));
        setSize(350, 400);
        setLocationRelativeTo(null);

        // start of display panel:
        dis_panel.add(new JLabel("Enter the name of the Artifact:"));
        dis_panel.add(Name);

        dis_panel.add(new JLabel("Enter the Identification Number of the Artifact"));
        dis_panel.add(ID);

        dis_panel.add(new JLabel("Enter the Decription of the Artifact:"));
        dis_panel.add(Description);

        dis_panel.add(new JLabel("Enter the data of Arrival of the Artifact [dd/mm/yyy]:"));
        dis_panel.add(ArrivalDate);

        dis_panel.add(new JLabel("Enter the Price of the Artifact:"));
        dis_panel.add(Price);

        dis_panel.add(new JLabel("Enter the Weight of the Artifact"));
        dis_panel.add(Weight);

        // start of Status panel:
        status_panel.add(new JLabel("State the Status of the Artifact: |"));
        status_panel.add(new JLabel("Sold:"));
        status_panel.add(Sold);

        status_panel.add(new JLabel("Excavated:"));
        status_panel.add(Excavated);

        status_panel.add(new JLabel("Donated:"));
        status_panel.add(Donated);

        // close button listener:
        close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new CuratorMenu();
            }
        });

        // save button listener:
        save.addActionListener(new SaveListener());

        save.setOpaque(true);
        save.setContentAreaFilled(true);
        save.setBorderPainted(false);
        save.setFocusPainted(false);
        save.setBackground(Color.darkGray); // for the background
        save.setForeground(Color.white); // for the text

        close.setOpaque(true);
        close.setContentAreaFilled(true);
        close.setBorderPainted(false);
        close.setFocusPainted(false);
        close.setBackground(Color.darkGray); // for the background
        close.setForeground(Color.white); // for the text

        // command buttons panel:
        com_panel.add(save);
        com_panel.add(close);

        dis_panel.add(status_panel);

        add(dis_panel, BorderLayout.CENTER);
        add(com_panel, BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }
    private class SaveListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String status = "";

            try {
                // determining which button is selected for gender:
                if (Sold.isSelected())
                    status = "Sold";

                if (Excavated.isSelected())
                    status = "Excavated";

                if (Donated.isSelected())
                    status = "Donated";

               ArtifactController.storeArtifact(Name.getText(),Integer.parseInt(ID.getText()),Description.getText(),status,Double.parseDouble(Weight.getText()),Double.parseDouble(Price.getText()),ArrivalDate.getText());
                
        }
        catch(NumberFormatException | IOException nfe){
            System.out.println("Enter valid number");

        }
        setVisible(false);
        new CuratorMenu();

    }
    }
}