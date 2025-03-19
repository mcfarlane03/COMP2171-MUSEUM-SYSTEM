import java.awt.*;
import javax.swing.*;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddDonorUI extends JFrame {
    private JPanel dis_panel = new JPanel();
    private JPanel com_panel = new JPanel();

    private JTextField Name = new JTextField(50);
    private JTextField ID = new JTextField(50);
    private JTextField Contact = new JTextField(350);
    private JTextField ArtifactsDonated = new JTextField(50);
    private JTextField NumArtifacts = new JTextField(10);

    private JButton close = new JButton("Close");
    private JButton save = new JButton("Save Donor");
    // private JButton update = new JButton("Update Donor");

    public AddDonorUI() {
        setTitle("Add Donor to Contact List");
        dis_panel.setLayout(new GridLayout(5, 4));
        setSize(400, 400);

        // start of display panel:
        dis_panel.add(new JLabel("Enter the donor's name: "));
        dis_panel.add(Name);

        dis_panel.add(new JLabel("Enter the donor ID#: "));
        dis_panel.add(ID);

        dis_panel.add(new JLabel("Enter the contact# for the donor: "));
        dis_panel.add(Contact);

        dis_panel.add(new JLabel("Enter the artifacts donated: "));
        dis_panel.add(ArtifactsDonated);

        dis_panel.add (new JLabel ("Enter the number of artifacts donated: "));
        dis_panel.add (NumArtifacts);

        // close button listener:
        close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new EmployeeMenu();
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

        // update.setOpaque(true);
        // update.setContentAreaFilled(true);
        // update.setBorderPainted(false);
        // update.setFocusPainted(false);
        // update.setBackground(Color.darkGray); // for the background
        // update.setForeground(Color.white); // for the text

        // command buttons panel:
        com_panel.add(save);
        com_panel.add(close);
        // com_panel.add(update);

        add(dis_panel, BorderLayout.CENTER);
        add(com_panel, BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }

    private class SaveListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                DonorController.storeDonor(Name.getText(),Integer.parseInt(ID.getText()), Contact.getText(),ArtifactsDonated.getText(),Integer.parseInt(NumArtifacts.getText()));
               
            } catch (NumberFormatException nfe) {
                System.out.println("Incorrect input format. Please to enter a number.");
            } catch (IOException ioe) {
            }
            setVisible(false);
            new EmployeeMenu();
        }
    }
}