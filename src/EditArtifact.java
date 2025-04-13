import java.awt.*;
import java.io.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class EditArtifact extends JFrame {
    // Main panels
    private JPanel mainPanel = new JPanel(new BorderLayout());
    private JPanel selectionPanel = new JPanel(new BorderLayout());
    private JPanel editPanel = new JPanel();
    private JPanel buttonPanel = new JPanel();

    // Table components
    private JTable artifactTable;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;

    // Edit form components
    private JTextField nameField = new JTextField(15);
    private JTextField idField = new JTextField(15);
    private JTextField descriptionField = new JTextField(15);
    private JTextField statusField = new JTextField(15);
    private JTextField weightField = new JTextField(15);
    private JTextField priceField = new JTextField(15);
    private JTextField dateField = new JTextField(15);

    // Buttons
    private JButton saveButton = new JButton("Save Changes");
    private JButton cancelButton = new JButton("Cancel");

    // Data
    private ArrayList<Artifact> artifactList;
    private Artifact selectedArtifact;
    private int selectedIndex = -1;

    public EditArtifact() {
        setTitle("Edit Artifact");
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Load artifacts
        artifactList = loadArtifacts("artifact_catalogue.txt");

        // Initialize table
        String[] columnNames = {"Name", "ID", "Description", "Status", "Weight", "Price", "Date of Arrival"};
        tableModel = new DefaultTableModel(columnNames, 0);
        artifactTable = new JTable(tableModel);
        populateTable();

        // Configure table
        artifactTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        artifactTable.getSelectionModel().addListSelectionListener(new ArtifactSelectionListener());

        // Add table to scroll pane
        scrollPane = new JScrollPane(artifactTable);
        scrollPane.setPreferredSize(new Dimension(750, 200));

        JLabel instructionLabel = new JLabel("Select an artifact to edit:");
        selectionPanel.add(instructionLabel, BorderLayout.NORTH);
        selectionPanel.add(scrollPane, BorderLayout.CENTER);

        // Set up edit form
        editPanel.setLayout(new GridLayout(7, 2, 10, 10));
        editPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        editPanel.add(new JLabel("Name:"));
        editPanel.add(nameField);

        editPanel.add(new JLabel("ID:"));
        editPanel.add(idField);
        idField.setEditable(false); // ID shouldn't be changed

        editPanel.add(new JLabel("Description:"));
        editPanel.add(descriptionField);

        editPanel.add(new JLabel("Status:"));
        editPanel.add(statusField);

        editPanel.add(new JLabel("Weight (kg):"));
        editPanel.add(weightField);

        editPanel.add(new JLabel("Price:"));
        editPanel.add(priceField);

        editPanel.add(new JLabel("Date of Arrival (dd/MM/yyyy):"));
        editPanel.add(dateField);

        // Style buttons - do this before using them in setEditFieldsEnabled
        saveButton.setOpaque(true);
        saveButton.setContentAreaFilled(true);
        saveButton.setBorderPainted(false);
        saveButton.setFocusPainted(false);
        saveButton.setBackground(Color.darkGray);
        saveButton.setForeground(Color.white);

        cancelButton.setOpaque(true);
        cancelButton.setContentAreaFilled(true);
        cancelButton.setBorderPainted(false);
        cancelButton.setFocusPainted(false);
        cancelButton.setBackground(Color.darkGray);
        cancelButton.setForeground(Color.white);

        // Add buttons to panel
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Now it's safe to call this as buttons are initialized
        setEditFieldsEnabled(false);

        // Add action listeners
        saveButton.addActionListener(new SaveButtonListener());
        cancelButton.addActionListener(new CancelButtonListener());

        // Assemble main panel
        mainPanel.add(selectionPanel, BorderLayout.NORTH);
        mainPanel.add(editPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);

        setVisible(true);
    }

    private void populateTable() {
        tableModel.setRowCount(0); // Clear existing rows

        for (Artifact artifact : artifactList) {
            Object[] row = {
                    artifact.getName(),
                    artifact.getID(),
                    artifact.getDescription(),
                    artifact.getStatus(),
                    artifact.getWeight(),
                    artifact.getPrice(),
                    artifact.getArrivalDate()
            };
            tableModel.addRow(row);
        }
    }

    private void setEditFieldsEnabled(boolean enabled) {
        nameField.setEnabled(enabled);
        descriptionField.setEnabled(enabled);
        statusField.setEnabled(enabled);
        weightField.setEnabled(enabled);
        priceField.setEnabled(enabled);
        dateField.setEnabled(enabled);
        saveButton.setEnabled(enabled);
    }

    private void displayArtifactDetails(Artifact artifact) {
        nameField.setText(artifact.getName());
        idField.setText(String.valueOf(artifact.getID()));
        descriptionField.setText(artifact.getDescription());
        statusField.setText(artifact.getStatus());
        weightField.setText(String.valueOf(artifact.getWeight()));
        priceField.setText(String.valueOf(artifact.getPrice()));
        dateField.setText(artifact.getArrivalDate());

        setEditFieldsEnabled(true);
    }

    private ArrayList<Artifact> loadArtifacts(String filename) {
        Scanner scanner = null;
        ArrayList<Artifact> artifacts = new ArrayList<>();

        try {
            scanner = new Scanner(new File(filename));
            while (scanner.hasNext()) {
                String[] nextLine = scanner.nextLine().split(" ");
                String name = nextLine[0];
                int id = Integer.parseInt(nextLine[1]);
                String description = nextLine[2];
                String status = nextLine[3];
                double weight = Double.parseDouble(nextLine[4]);
                double price = Double.parseDouble(nextLine[5]);
                String dateArrival = nextLine[6];

                artifacts.add(new Artifact(name, id, description, status, weight, price, dateArrival));
            }
            scanner.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading artifacts: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        return artifacts;
    }

    private void saveArtifacts(ArrayList<Artifact> artifacts, String filename) {
        try {
            FileWriter writer = new FileWriter(filename);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            for (Artifact artifact : artifacts) {
                String line = artifact.getName() + " " +
                        artifact.getID() + " " +
                        artifact.getDescription() + " " +
                        artifact.getStatus() + " " +
                        artifact.getWeight() + " " +
                        artifact.getPrice() + " " +
                        artifact.getArrivalDate();

                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }

            bufferedWriter.close();
            writer.close();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving artifacts: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class ArtifactSelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                selectedIndex = artifactTable.getSelectedRow();
                if (selectedIndex >= 0) {
                    selectedArtifact = artifactList.get(selectedIndex);
                    displayArtifactDetails(selectedArtifact);
                }
            }
        }
    }

    private class SaveButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                if (selectedIndex < 0) {
                    JOptionPane.showMessageDialog(null, "Please select an artifact to edit.");
                    return;
                }

                // Validate inputs
                String name = nameField.getText().trim();
                int id = Integer.parseInt(idField.getText().trim());
                String description = descriptionField.getText().trim();
                String status = statusField.getText().trim();
                double weight = Double.parseDouble(weightField.getText().trim());
                double price = Double.parseDouble(priceField.getText().trim());
                String date = dateField.getText().trim();

                if (name.isEmpty() || description.isEmpty() || status.isEmpty() || date.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "All fields must be filled in.",
                            "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Create updated artifact
                Artifact updatedArtifact = new Artifact(name, id, description, status, weight, price, date);

                // Update the artifact in the list
                artifactList.set(selectedIndex, updatedArtifact);

                // Update the table
                populateTable();

                // Save the updated list to file
                saveArtifacts(artifactList, "artifact_catalogue.txt");

                JOptionPane.showMessageDialog(null, "Artifact updated successfully!");

                // Clear selection
                artifactTable.clearSelection();
                selectedIndex = -1;
                setEditFieldsEnabled(false);
                clearFields();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter valid numeric values for ID, weight, and price.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error updating artifact: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearFields() {
        nameField.setText("");
        idField.setText("");
        descriptionField.setText("");
        statusField.setText("");
        weightField.setText("");
        priceField.setText("");
        dateField.setText("");
    }

    private class CancelButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            // Return to previous menu
            if (EmployeeLogin.managerFlag()) {
                new ManagerMenu();
            } else {
                new CuratorMenu();
            }
        }
    }
}