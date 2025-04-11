import java.awt.*;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
// import javax.swing.JTextField;

import java.util.Scanner;
import java.util.ArrayList;
// import java.util.Arrays;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JCheckBox;

import java.util.Comparator;
import java.util.Date;
import java.util.Collections;
import javax.swing.JScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;



public class ViewArtifact extends JFrame {
    public JPanel dispanel = new JPanel(new GridLayout(5, 1));
    public JPanel cmdpanel = new JPanel();
    public JPanel filterpanel = new JPanel(new GridLayout(0, 1));

    public JButton sort_name = new JButton("Sort by Name");
    public JButton sort_price = new JButton("Sort by Price");
    public JButton sort_weight = new JButton("Sort by Weight");
    public JButton sort_date = new JButton("Sort by the Date of Arrival");
    public JButton export_results = new JButton("Export Results");
    public JButton Close = new JButton("Close");
    public JButton Reset = new JButton("Reset");

    private JTable table;
    private DefaultTableModel model;
    private ArrayList<Artifact> alist;
    private ArrayList<Artifact> alist2;
    private JScrollPane scrollPane;

    // private ArrayList<Artifact> filteredList;
    public JCheckBox range0to20 = new JCheckBox("0 - 20 kg");
    public JCheckBox range20to100 = new JCheckBox("20 - 100 kg");
    public JCheckBox rangeabove100 = new JCheckBox(">100 kg");

    public JCheckBox range1 = new JCheckBox("0 - 100000 ");
    public JCheckBox range2 = new JCheckBox("100000 - 1000000 ");
    public JCheckBox range3 = new JCheckBox(">1000000");

    // Create class-level variables to track selected filters
    private boolean weight0to20Selected = false;
    private boolean weight20to100Selected = false;
    private boolean weightAbove100Selected = false;
    private boolean price0to100kSelected = false;
    private boolean price100kto1mSelected = false;
    private boolean priceAbove1mSelected = false;

    public ViewArtifact() {
        setTitle("Artifact View");
        setSize(400, 300);
        setLocationRelativeTo(null);

        alist = loadArtifact("artifact_catalogue.txt");
        String[] columnNames = { "Name",
                "ID #",
                "Brief Description",
                "Status",
                "Weight",
                "Price",
                "Date of Arrival" };
        alist2 = alist;
        model = new DefaultTableModel(columnNames, 0);// default table
        table = new JTable(model);// takes in the default table
        showTable(alist);

        // sets the size
        table.setPreferredScrollableViewportSize(new Dimension(900, alist.size() * 15 + 50));

        table.setFillsViewportHeight(true);

        scrollPane = new JScrollPane(table);

        add(scrollPane);
        // FILTERS TO GO HERE:
        filterpanel.add(new JLabel("FILTER BY WEIGHT|"));
        filterpanel.add(range0to20);
        filterpanel.add(range20to100);
        filterpanel.add(rangeabove100);

        range0to20.addActionListener(new FilterListener1());
        range20to100.addActionListener(new FilterListener2());
        rangeabove100.addActionListener(new FilterListener3());

        filterpanel.add(new JLabel("FILTER BY PRICE|"));
        filterpanel.add(range1);
        filterpanel.add(range2);
        filterpanel.add(range3);

        range1.addActionListener(new PriceListener1());
        range2.addActionListener(new PriceListener2());
        range3.addActionListener(new PriceListener3());

        add(filterpanel, BorderLayout.EAST);

        // adding functionality to the buttons
        sort_name.addActionListener(new SortNameListener());
        sort_price.addActionListener(new Sortbyprice());
        sort_weight.addActionListener(new Sortbyweight());
        sort_date.addActionListener(new SortDateListener());
        export_results.addActionListener(new exportResultsListener());
        Close.addActionListener(new CloseButtonListener());
        Reset.addActionListener(new ResetListener());


        sort_name.setOpaque(true);
        sort_name.setContentAreaFilled(true);
        sort_name.setBorderPainted(false);
        sort_name.setFocusPainted(false);
        sort_name.setBackground(Color.darkGray); // for the background
        sort_name.setForeground(Color.white); // for the text

        sort_price.setOpaque(true);
        sort_price.setContentAreaFilled(true);
        sort_price.setBorderPainted(false);
        sort_price.setFocusPainted(false);
        sort_price.setBackground(Color.darkGray); // for the background
        sort_price.setForeground(Color.white); // for the text

        Close.setOpaque(true);
        Close.setContentAreaFilled(true);
        Close.setBorderPainted(false);
        Close.setFocusPainted(false);
        Close.setBackground(Color.darkGray); // for the background
        Close.setForeground(Color.white); // for the text

        Reset.setOpaque(true);
        Reset.setContentAreaFilled(true);
        Reset.setBorderPainted(false);
        Reset.setFocusPainted(false);
        Reset.setBackground(Color.darkGray); // for the background
        Reset.setForeground(Color.white); // for the text

        // addition of buttons to command panel
        cmdpanel.add(sort_name);
        cmdpanel.add(sort_price);
        cmdpanel.add(sort_weight);
        cmdpanel.add(sort_date);
        cmdpanel.add(Close);
        cmdpanel.add(Reset);

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

    // public void addArtifact(Artifact a) {
    // alist.add(a);
    // addToTable(a);

    // }

    private void showTable(ArrayList<Artifact> alist) {
        if (!alist.isEmpty()) {
            for (Artifact list : alist)
                addToTable(list);
        }
    }

    private void addToTable(Artifact a) {
        String[] item = { a.getName(), "" + a.getID(), "" + a.getDescription(), "" + a.getStatus(), "" + a.getWeight(),
                "" + a.getPrice(), "" + a.getArrivalDate() };
        model.addRow(item);
    }

    // listener for balance sort button
    private class Sortbyprice implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            alist.sort(new sortPrice());
            model.setRowCount(0);
            showTable(alist);
        }
    }

    // listener for name sort button
    private class SortNameListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            alist.sort(new nameCompare());
            model.setRowCount(0);
            showTable(alist);
        }
    }

    private class SortDateListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            alist.sort(new sortDate());
            model.setRowCount(0);
            showTable(alist);
        }
    }

    private class Sortbyweight implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            alist.sort(new sortWeight());
            model.setRowCount(0);
            showTable(alist);
        }
    }

    private static class exportResultsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    // sorts the names in the table by comparing individual letters
    public class nameCompare implements Comparator<Artifact> {
        public int compare(Artifact a1, Artifact a2) {
            return a1.getName().compareTo(a2.getName());
        }
    }

    public class sortDate implements Comparator<Artifact> {
        public int compare(Artifact a1, Artifact a2) {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Date d_1 = new Date();
            try {
                d_1 = df.parse(a1.getArrivalDate());
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Date d_2 = new Date();
            try {
                d_2 = df.parse(a2.getArrivalDate());
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return d_1.compareTo(d_2);
        }
    }

    // sorts the records by balance
    private static class sortPrice implements Comparator<Artifact> {
        public int compare(Artifact a1, Artifact a2) {
            // this function sorts in ascending order
            if (a1.getPrice() > a2.getPrice())
                return 1;

            if (a1.getPrice() < a2.getPrice())
                return -1;

            return 0;
        }
    }

    // sorts the records by balance
    private static class sortWeight implements Comparator<Artifact> {
        public int compare(Artifact a1, Artifact a2) {
            // this function sorts in ascending order
            if (a1.getWeight() > a2.getWeight())
                return 1;

            if (a1.getWeight() < a2.getWeight())
                return -1;

            return 0;
        }
    }

    private class CloseButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            if (EmployeeLogin.managerFlag())
                new ManagerMenu();
            else
                new CuratorMenu();
        }
    }

    // New implementation of filter listeners that track states
    private class FilterListener1 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            weight0to20Selected = range0to20.isSelected();
            applyAllFilters();
        }
    }

    private class FilterListener2 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            weight20to100Selected = range20to100.isSelected();
            applyAllFilters();
        }
    }

    private class FilterListener3 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            weightAbove100Selected = rangeabove100.isSelected();
            applyAllFilters();
        }
    }

    private class PriceListener1 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            price0to100kSelected = range1.isSelected();
            applyAllFilters();
        }
    }

    private class PriceListener2 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            price100kto1mSelected = range2.isSelected();
            applyAllFilters();
        }
    }

    private class PriceListener3 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            priceAbove1mSelected = range3.isSelected();
            applyAllFilters();
        }
    }

    // New common method to apply all selected filters at once
    private void applyAllFilters() {
        // Start with the original full list
        ArrayList<Artifact> filteredList = new ArrayList<>(alist2);

        // If any weight filters are selected, apply weight filtering
        if (weight0to20Selected || weight20to100Selected || weightAbove100Selected) {
            ArrayList<Artifact> weightFiltered = new ArrayList<>();

            for (Artifact artifact : filteredList) {
                boolean includeArtifact = false;

                if (weight0to20Selected && artifact.getWeight() >= 0.00 && artifact.getWeight() <= 20.00) {
                    includeArtifact = true;
                }

                if (weight20to100Selected && artifact.getWeight() > 20.00 && artifact.getWeight() <= 100.00) {
                    includeArtifact = true;
                }

                if (weightAbove100Selected && artifact.getWeight() > 100.00) {
                    includeArtifact = true;
                }

                if (includeArtifact) {
                    weightFiltered.add(artifact);
                }
            }

            filteredList = weightFiltered;
        }

        // If any price filters are selected, apply price filtering to the already filtered list
        if (price0to100kSelected || price100kto1mSelected || priceAbove1mSelected) {
            ArrayList<Artifact> priceFiltered = new ArrayList<>();

            for (Artifact artifact : filteredList) {
                boolean includeArtifact = false;

                if (price0to100kSelected && artifact.getPrice() >= 0.00 && artifact.getPrice() <= 100000.00) {
                    includeArtifact = true;
                }

                if (price100kto1mSelected && artifact.getPrice() > 100000.00 && artifact.getPrice() <= 1000000.00) {
                    includeArtifact = true;
                }

                if (priceAbove1mSelected && artifact.getPrice() > 1000000.00) {
                    includeArtifact = true;
                }

                if (includeArtifact) {
                    priceFiltered.add(artifact);
                }
            }

            filteredList = priceFiltered;
        }

        // If no filters are selected, show all artifacts
        if (!weight0to20Selected && !weight20to100Selected && !weightAbove100Selected &&
                !price0to100kSelected && !price100kto1mSelected && !priceAbove1mSelected) {
            filteredList = alist2;
        }

        // Update the display with filtered results
        model.setRowCount(0);
        showTable(filteredList);
        alist = filteredList;
    }

    // Updated Reset listener to reset all filter states
    private class ResetListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Reset all filter checkboxes
            range0to20.setSelected(false);
            range20to100.setSelected(false);
            rangeabove100.setSelected(false);
            range1.setSelected(false);
            range2.setSelected(false);
            range3.setSelected(false);

            // Reset filter state variables
            weight0to20Selected = false;
            weight20to100Selected = false;
            weightAbove100Selected = false;
            price0to100kSelected = false;
            price100kto1mSelected = false;
            priceAbove1mSelected = false;

            // Reset the list and update the display
            alist = alist2;
            model.setRowCount(0);
            showTable(alist);
        }
    }
}