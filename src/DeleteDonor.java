
import java.awt.*;
import java.io.File;
import javax.swing.*;
import java.awt.Color;
import java.util.Scanner;
import java.awt.Dimension;
import java.util.ArrayList;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import java.io.FileWriter;
import java.io.BufferedWriter;

public class DeleteDonor extends JFrame {
    private JPanel dispanel = new JPanel();
    private JPanel cmdPanel = new JPanel();

    private JButton remove = new JButton("Remove");
    private JButton close = new JButton("Close");

    private JTextField id = new JTextField(15);

    private JTable table;

    private DefaultTableModel model;

    private ArrayList<Donor> dlist;

    private JScrollPane scrollPane;

    public DeleteDonor() {
        setTitle("Remove a Donor from the Contact List");
        dispanel.setSize(950, 300);

        dispanel.add(new JLabel("Donor ID [ID #]"));
        dispanel.add(id);

        dlist = loadDonors("donor_list.txt");
        String[] columnNames = { "Name",
                "Donor ID",
                "Contact Information",
                "Artifacts Donated",
                "Nummber of Artifacts Donated" };
        model = new DefaultTableModel(columnNames, 0);// default table
        table = new JTable(model);// takes in the default table
        showTable(dlist);

        // sets the size
        table.setPreferredScrollableViewportSize(new Dimension(900, dlist.size() * 15 + 50));

        table.setFillsViewportHeight(true);

        scrollPane = new JScrollPane(table);

        add(scrollPane);

        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                // check for selected row first
                if (table.getSelectedRow() != -1) {
                    // remove selected row from the model
                    model.removeRow(table.getSelectedRow());
                    JOptionPane.showMessageDialog(null, "Selected row deleted successfully");
                    remove(dlist);
                }
            }
        });

        close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new EmployeeMenu();
            }
        });

        remove.setOpaque(true);
        remove.setContentAreaFilled(true);
        remove.setBorderPainted(false);
        remove.setFocusPainted(false);
        remove.setBackground(Color.darkGray); // for the background
        remove.setForeground(Color.white); // for the text

        close.setOpaque(true);
        close.setContentAreaFilled(true);
        close.setBorderPainted(false);
        close.setFocusPainted(false);
        close.setBackground(Color.darkGray); // for the background
        close.setForeground(Color.white); // for the text

        // adding components to panels:
        dispanel.add(new JScrollPane(table));
        cmdPanel.add(remove);
        cmdPanel.add(close);

        add(dispanel, BorderLayout.CENTER);
        add(cmdPanel, BorderLayout.SOUTH);

        setSize(950, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private ArrayList<Donor> loadDonors(String dfile) {
        Scanner pscan = null;
        ArrayList<Donor> dlist = new ArrayList<Donor>();
        try {
            // takes a file name as paramter and reads information from it
            pscan = new Scanner(new File(dfile));
            while (pscan.hasNext()) {
                String[] nextLine = pscan.nextLine().split(" ");
                String name = nextLine[0];
                int ID = Integer.parseInt(nextLine[1]);
                String contact = nextLine[2];
                String donated = nextLine[3];
                int numArtifacts = Integer.parseInt(nextLine[4]);
                dlist.add(new Donor(name, ID, contact, donated, numArtifacts));
            }
            pscan.close();
        } catch (IOException e) {
        }
        return dlist;
    }

    public void addDonor(Donor d) {
        dlist.add(d);
        addToTable(d);

    }

    private void showTable(ArrayList<Donor> dlist) {
        if (dlist.size() > 0) {
            for (Donor list : dlist)
                addToTable(list);
        }
    }

    private void addToTable(Donor d) {
        String[] item = { d.getName(), " " + d.getID(), " " + d.getContact(), " " + d.getArtifactsDonated(),
                " " + d.getNumArtifacts() };
        model.addRow(item);
    }

    private void remove(ArrayList<Donor> dlist) {
        String text_;
        for (int i = 0; i < dlist.size(); i++) {
            if (dlist.get(i).getID() == Integer.parseInt(id.getText())) {
                System.out.println("The Student has been removed");
                dlist.remove(i);
                try {
                    File file = new File("stud_records.txt");
                    file.delete();// deletes the existing file
                    File file_ = new File("stud_records.txt"); // re creates it to be filled with the updates
                                                               // information
                    FileWriter fileWriter = new FileWriter(file, file_.exists());
                    BufferedWriter output = new BufferedWriter(fileWriter);

                    for (Donor d_ : dlist) {
                        text_ = d_.getName() + " " + d_.getID() + " " + d_.getContact() + " " + d_.getArtifactsDonated()
                                + " " + d_.getNumArtifacts();
                        output.write(text_); // each record is written to the file
                        if (file_.exists())
                            output.newLine();
                    }
                    output.close();
                    fileWriter.close();
                } catch (NumberFormatException nfe) {
                    System.out.println("Incorrect input format. Please to enter a number.");
                } catch (IOException ioe) {
                }
            }

            else {
                setVisible(false);
            }
        }

    }
}
