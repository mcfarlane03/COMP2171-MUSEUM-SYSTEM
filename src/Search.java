import java.awt.*;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;

public class Search extends JFrame {
    public JPanel dispanel = new JPanel(new GridLayout(5, 1));
    public JPanel cmdpanel = new JPanel();

    private JTextField search = new JTextField(50);
    public JButton Close = new JButton("Close");

    // nathan added this:
    public JButton Search = new JButton("Search");

    private JTable table;
    private DefaultTableModel model;
    private ArrayList<Artifact> a_list;
    private JScrollPane scrollPane;

    public Search() {
        setTitle("Search Panel");
        setSize(400, 300);
        setLocationRelativeTo(null);

        dispanel.add(new JLabel("Search :"));
        dispanel.add(search);

        a_list = loadArtifact("artifact_catalogue.txt");
        String[] columnNames = { "Name",
                "ID #",
                "Brief Description",
                "Status",
                "Weight",
                "Price",
                "Date of Arrival" };
        model = new DefaultTableModel(columnNames, 0);// default table
        table = new JTable(model);// takes in the default table
        // showTable(a_list);

        // sets the size
        table.setPreferredScrollableViewportSize(new Dimension(900, a_list.size() * 15 + 50));

        table.setFillsViewportHeight(true);

        scrollPane = new JScrollPane(table);

        add(scrollPane);

        // adding functionality to the buttons
        Close.addActionListener(new CloseButtonListener());
        Search.addActionListener(new SearchButtonListener());

        Close.setOpaque(true);
        Close.setContentAreaFilled(true);
        Close.setBorderPainted(false);
        Close.setFocusPainted(false);
        Close.setBackground(Color.darkGray); // for the background
        Close.setForeground(Color.white); // for the text

        // nathan added this:
        cmdpanel.add(Search);

        cmdpanel.add(Close);

        add(dispanel, BorderLayout.NORTH);
        add(cmdpanel, BorderLayout.SOUTH);
        pack();
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
                if (search.getText().split(" ").length == 1) {
                    for (int x = 0; x < nextLine.length; x++) {
                        if (search.getText().equals(nextLine[x])) {
                            String name = nextLine[0];
                            int Id = Integer.parseInt(nextLine[1]); // converts the id number to an integer
                            String description = nextLine[2]; //
                            String status = nextLine[3];
                            double weight = Double.parseDouble(nextLine[4]);
                            double price = Double.parseDouble(nextLine[5]);
                            String date_arrival = nextLine[6];
                            alist.add(new Artifact(name, Id, description, status, weight, price, date_arrival));
                            System.out.println(alist);
                        }
                    }
                }
                if (search.getText().split(" ").length == 3) {
                    String[] message = search.getText().split(" ");

                    boolean part1 = Arrays.asList(nextLine).contains(message[0]);
                    boolean part2 = Arrays.asList(nextLine).contains(message[2]);

                    if ((message[1].equals("AND") && part1 && part2) ||
                            (message[1].equals("OR") && (part1 || part2)) ||
                            (message[1].equals("NOT") && part1 && !part2)) {
                        String name = nextLine[0];
                        int Id = Integer.parseInt(nextLine[1]); // converts the id number to an integer
                        String description = nextLine[2]; //
                        String status = nextLine[3];
                        double weight = Double.parseDouble(nextLine[4]);
                        double price = Double.parseDouble(nextLine[5]);
                        String date_arrival = nextLine[6];
                        alist.add(new Artifact(name, Id, description, status, weight, price, date_arrival));
                        System.out.println(alist);
                    }

                }
            }
            pscan.close();
        } catch (IOException e) {
        }
        return alist;
    }

    public void addArtifact(Artifact a) {
        a_list.add(a);
        addToTable(a);
    }

    private void showTable(ArrayList<Artifact> slist) {
        if (slist.size() > 0) {
            for (Artifact list : slist)
                addToTable(list);
        }
    }

    private void addToTable(Artifact a) {
        String[] item = { a.getName(), "" + a.getID(), "" + a.getDescription(), "" + a.getStatus(), "" + a.getWeight(),
                "" + a.getPrice(), "" + a.getArrivalDate() };
        model.addRow(item);
    }

    // nathan added this:
    private class SearchButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            a_list = loadArtifact("artifact_catalogue.txt");

            // System.out.println(a_list);

            // Clear existing data in the table
            model.setRowCount(0);

            // Add new data to the table
            showTable(a_list);

            // sets the size
            table.setPreferredScrollableViewportSize(new Dimension(900, a_list.size() * 15 + 50));

            table.setFillsViewportHeight(true);

            // Update the existing JScrollPane
            scrollPane.setViewportView(table);
        }
    }

    private class CloseButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            new EmployeeMenu();
        }
    }
}
