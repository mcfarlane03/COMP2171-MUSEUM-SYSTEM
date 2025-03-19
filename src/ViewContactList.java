import java.awt.*;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;

// imp/ort java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.IOException;
import javax.swing.JButton;
import java.util.Comparator;
import java.util.Collections;
import javax.swing.JScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;

public class ViewContactList extends JFrame {
    public JPanel dispanel = new JPanel(new GridLayout(5, 1));
    public JPanel cmdpanel = new JPanel();

    public JButton sort_name = new JButton("Sort by Donor Name");
    public JButton sort_artifacts = new JButton("Sort by Number of Artifacts Donated");
    public JButton Close = new JButton("Close");

    private JTable table;
    private DefaultTableModel model;
    private ArrayList<Donor> dlist;
    private JScrollPane scrollPane;

    public ViewContactList() {
        setTitle("View Contact List");
        setSize(400, 300);

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

        // adding functionality to the buttons
        sort_name.addActionListener(new SortNameListener());
        sort_artifacts.addActionListener(new SortNumArtifactsListener());
        Close.addActionListener(new CloseButtonListener());

        sort_name.setOpaque(true);
        sort_name.setContentAreaFilled(true);
        sort_name.setBorderPainted(false);
        sort_name.setFocusPainted(false);
        sort_name.setBackground(Color.darkGray); // for the background
        sort_name.setForeground(Color.white); // for the text

        sort_artifacts.setOpaque(true);
        sort_artifacts.setContentAreaFilled(true);
        sort_artifacts.setBorderPainted(false);
        sort_artifacts.setFocusPainted(false);
        sort_artifacts.setBackground(Color.darkGray); // for the background
        sort_artifacts.setForeground(Color.white); // for the text

        Close.setOpaque(true);
        Close.setContentAreaFilled(true);
        Close.setBorderPainted(false);
        Close.setFocusPainted(false);
        Close.setBackground(Color.darkGray); // for the background
        Close.setForeground(Color.white); // for the text

        // addition of buttons to command panel
        cmdpanel.add(sort_name);
        cmdpanel.add(sort_artifacts);
        cmdpanel.add(Close);

        add(dispanel, BorderLayout.NORTH);
        add(cmdpanel, BorderLayout.SOUTH);
        pack();
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

    // listener for balance sort button
    private class SortNumArtifactsListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Collections.sort(dlist, new sortNumArtifacts());
            model.setRowCount(0);
            showTable(dlist);
        }
    }

    // listener for name sort button
    private class SortNameListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Collections.sort(dlist, new nameCompare());
            model.setRowCount(0);
            showTable(dlist);
        }
    }

    // sorts the names in the table by comparing individual letters
    public class nameCompare implements Comparator<Donor> {
        public int compare(Donor d1, Donor d2) {
            return d1.getName().compareTo(d2.getName());
        }
    }

    // sorts the records by balance
    private class sortNumArtifacts implements Comparator<Donor> {
        public int compare(Donor d1, Donor d2) {
            // this function sorts in ascending order
            if (d1.getNumArtifacts() > d2.getNumArtifacts())
                return 1;

            if (d1.getNumArtifacts() < d2.getNumArtifacts())
                return -1;

            return 0;
        }
    }

    // listener for close button
    private class CloseButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            // new EmployeeMenu();
            if (EmployeeLogin.managerFlag())
                new ManagerMenu();
            else
                new EmployeeMenu();
        }
    }
}
