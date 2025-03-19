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

public class DeleteArtifact extends JFrame {
    private JPanel dispanel = new JPanel();
    private JPanel cmdPanel = new JPanel();

    private JButton remove = new JButton("Remove");
    private JButton close = new JButton("Close");

    private JTextField id = new JTextField(15);

    private JTable table;

    private DefaultTableModel model;

    private ArrayList<Artifact> alist;

    private JScrollPane scrollPane;

    public DeleteArtifact() {
        setTitle("Remove an Artifact from the Catalogue");
        setLocationRelativeTo(null);
        dispanel.setSize(950, 300);

        dispanel.add(new JLabel("Artifact ID [ID #]"));
        dispanel.add(id);

        alist = loadArtifact("artifact_catalogue.txt");
        String[] columnNames = { "Name",
                "ID #",
                "Brief Description",
                "Status",
                "Weight",
                "Price",
                "Date of Arrival" };
        model = new DefaultTableModel(columnNames, 0);// default table
        table = new JTable(model);// takes in the default table
        showTable(alist);

        // sets the size
        table.setPreferredScrollableViewportSize(new Dimension(900, alist.size() * 15 + 50));

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
                    JOptionPane.showMessageDialog(null, "Selected record deleted successfully!");
                    remove(alist);
                    new EmployeeMenu();
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

    private ArrayList<Artifact> loadArtifact(String sfile) {
        Scanner pscan = null;
        ArrayList<Artifact> alist = new ArrayList<Artifact>();
        try {
            // takes a file name as paramter and reads information from it
            pscan = new Scanner(new File(sfile));
            while (pscan.hasNext()) {
                String[] nextLine = pscan.nextLine().split(" ");
                String name = nextLine[0];
                int Id = Integer.parseInt(nextLine[1]); // converts the id number to an integer
                String description = nextLine[2]; //
                String status = nextLine[3];
                double weight = Double.parseDouble(nextLine[4]);
                double price = Double.parseDouble(nextLine[5]);
                String date_arrival = nextLine[6];
                alist.add(new Artifact(name, Id, description, status, weight, price, date_arrival));
            }
            pscan.close();
        } catch (IOException e) {
        }
        return alist;
    }

    private void showTable(ArrayList<Artifact> alist) {
        if (alist.size() > 0) {
            for (Artifact list : alist)
                addToTable(list);
        }
    }

    private void addToTable(Artifact a) {
        String[] item = { a.getName(), " " + a.getID(), " " + a.getDescription(), " " + a.getStatus(),
                " " + a.getWeight(),
                " " + a.getPrice(), " " + a.getArrivalDate() };
        model.addRow(item);

    }

    private void remove(ArrayList<Artifact> alist) {
        String text_;
        for (int i = 0; i < alist.size(); i++) {
            if (alist.get(i).getID() == Integer.parseInt(id.getText())) {
                System.out.println("The Artifact has been removed");
                alist.remove(i);
                try {
                    File file = new File("artifact_catalogue.txt");
                    file.delete(); // deletes the existing file
                    File file_ = new File("artifact_catalogue.txt"); // recreates it to be filled with the updated
                                                                     // information

                    FileWriter fileWriter = new FileWriter(file, file_.exists());
                    BufferedWriter output = new BufferedWriter(fileWriter);

                    for (Artifact a : alist) {
                        text_ = a.getName() + " " + a.getID() + " " + a.getDescription() + " " + a.getStatus() + " "
                                + a.getWeight() +
                                " " + a.getPrice() + " " + a.getArrivalDate();
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