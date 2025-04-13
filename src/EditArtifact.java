import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.util.List;

public class EditArtifact extends JFrame {
    // Main panels
    private JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
    private JPanel selectionPanel = new JPanel(new BorderLayout(10, 10));
    private JPanel editPanel = new JPanel();
    private JPanel buttonPanel = new JPanel();

    // Table components
    private JTable artifactTable;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;

    // Edit form components
    private JTextField nameField = new JTextField(15);
    private JTextField idField = new JTextField(15);
    private JTextArea descriptionField = new JTextArea(3, 15);
    private JScrollPane descScrollPane;
    private JComboBox<String> statusComboBox;
    private JTextField weightField = new JTextField(15);
    private JTextField priceField = new JTextField(15);
    private JTextField dateField = new JTextField(15);

    // Buttons
    private JButton saveButton = new JButton("Save Changes");
    private JButton cancelButton = new JButton("Cancel");
    private JButton searchButton = new JButton("Search");
    private JButton clearSearchButton = new JButton("Clear");
    private JTextField searchField = new JTextField(20);

    // Data
    private List<Artifact> artifactList;
    private Artifact selectedArtifact;
    private int selectedIndex = -1;
    
    // Data Access Object
    private ArtifactDAO artifactDAO = new ArtifactDAO();

    // Colors for UI
    private final Color PRIMARY_COLOR = new Color(44, 62, 80);      // Dark blue-gray
    private final Color ACCENT_COLOR = new Color(52, 152, 219);     // Bright blue
    private final Color TEXT_COLOR = new Color(236, 240, 241);      // Light gray
    private final Color BACKGROUND_COLOR = new Color(248, 249, 250); // Off-white
    private final Color BUTTON_HOVER = new Color(41, 128, 185);     // Darker blue

    public EditArtifact() {
        setTitle("Edit Artifact");
        setSize(900, 1024);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Set the look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Setup components that need additional configuration
        setupComponents();

        // Load artifacts from database
        artifactList = artifactDAO.getAllArtifacts();

        // Initialize table
        String[] columnNames = {"Name", "ID", "Description", "Status", "Weight (kg)", "Price ($)", "Date of Arrival"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        artifactTable = new JTable(tableModel);
        populateTable();

        // Configure table appearance
        artifactTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        artifactTable.getSelectionModel().addListSelectionListener(new ArtifactSelectionListener());
        artifactTable.setRowHeight(25);
        artifactTable.setGridColor(new Color(220, 220, 220));
        artifactTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        // Table header styling
        JTableHeader header = artifactTable.getTableHeader();
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(TEXT_COLOR);
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setBorder(BorderFactory.createEmptyBorder());

        // Add table to scroll pane
        scrollPane = new JScrollPane(artifactTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Create search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        searchPanel.add(new JLabel("Search by name: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(clearSearchButton);
        
        // Style search components
        styleButton(searchButton, ACCENT_COLOR);
        styleButton(clearSearchButton, new Color(149, 165, 166)); // Light gray for clear button
        
        // Add action listeners for search buttons
        searchButton.addActionListener(e -> {
            String searchTerm = searchField.getText().trim();
            if (!searchTerm.isEmpty()) {
                artifactList = artifactDAO.searchArtifactsByName(searchTerm);
                populateTable();
            }
        });
        
        clearSearchButton.addActionListener(e -> {
            searchField.setText("");
            artifactList = artifactDAO.getAllArtifacts();
            populateTable();
        });

        JLabel instructionLabel = new JLabel("Select an artifact to edit:");
        instructionLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        instructionLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        
        selectionPanel.add(instructionLabel, BorderLayout.NORTH);
        selectionPanel.add(searchPanel, BorderLayout.CENTER);
        selectionPanel.add(scrollPane, BorderLayout.SOUTH);
        selectionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Set up edit form
        buildEditPanel();

        // Style buttons
        styleButton(saveButton, ACCENT_COLOR);
        styleButton(cancelButton, new Color(231, 76, 60));  // Red color for cancel

        // Add action listeners
        saveButton.addActionListener(new SaveButtonListener());
        cancelButton.addActionListener(new CancelButtonListener());

        // Add buttons to panel
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        // Now it's safe to call this as buttons are initialized
        setEditFieldsEnabled(false);

        // Assemble main panel
        mainPanel.add(selectionPanel, BorderLayout.NORTH);
        mainPanel.add(editPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Add main panel to frame
        setContentPane(mainPanel);
        
        setVisible(true);
    }
    
    private void setupComponents() {
        // Initialize the description text area with scroll pane
        descriptionField.setLineWrap(true);
        descriptionField.setWrapStyleWord(true);
        descScrollPane = new JScrollPane(descriptionField);
        descScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        // Initialize status combo box
        String[] statuses = {"On Display", "In Storage", "On Loan", "Under Restoration", "New Acquisition"};
        statusComboBox = new JComboBox<>(statuses);
        statusComboBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
    }
    
    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(TEXT_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        Color hoverColor = bgColor.darker();
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
    }
    
    private void buildEditPanel() {
        editPanel = new JPanel(new GridBagLayout());
        editPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                "Artifact Details",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("SansSerif", Font.BOLD, 16),
                PRIMARY_COLOR
            ),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Use GridBagLayout for more control
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Style labels and fields
        Font labelFont = new Font("SansSerif", Font.BOLD, 14);
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 14);
        
        // Row 0
        JLabel idLabel = new JLabel("ID:");
        idLabel.setFont(labelFont);
        idField.setFont(fieldFont);
        idField.setEditable(false);
        idField.setBackground(new Color(220, 220, 220));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        editPanel.add(idLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        editPanel.add(idField, gbc);
        
        // Row 1
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(labelFont);
        nameField.setFont(fieldFont);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        editPanel.add(nameLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        editPanel.add(nameField, gbc);
        
        // Row 2
        JLabel descLabel = new JLabel("Description:");
        descLabel.setFont(labelFont);
        descriptionField.setFont(fieldFont);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        editPanel.add(descLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.gridheight = 2;  // Make it span 2 rows
        gbc.fill = GridBagConstraints.BOTH;
        editPanel.add(descScrollPane, gbc);
        
        // Reset height
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Row 4
        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setFont(labelFont);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        editPanel.add(statusLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        editPanel.add(statusComboBox, gbc);
        
        // Row 5
        JLabel weightLabel = new JLabel("Weight (kg):");
        weightLabel.setFont(labelFont);
        weightField.setFont(fieldFont);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        editPanel.add(weightLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        editPanel.add(weightField, gbc);
        
        JLabel priceLabel = new JLabel("Price ($):");
        priceLabel.setFont(labelFont);
        priceField.setFont(fieldFont);
        
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        editPanel.add(priceLabel, gbc);
        
        gbc.gridx = 3;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        editPanel.add(priceField, gbc);
        
        // Row 6
        JLabel dateLabel = new JLabel("Date of Arrival (dd/MM/yyyy):");
        dateLabel.setFont(labelFont);
        dateField.setFont(fieldFont);
        
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        editPanel.add(dateLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 3;
        editPanel.add(dateField, gbc);
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
        
        // Update status if no artifacts found
        if (artifactList.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No artifacts found matching your search criteria.", 
                "Search Results", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void setEditFieldsEnabled(boolean enabled) {
        nameField.setEnabled(enabled);
        descriptionField.setEnabled(enabled);
        statusComboBox.setEnabled(enabled);
        weightField.setEnabled(enabled);
        priceField.setEnabled(enabled);
        dateField.setEnabled(enabled);
        saveButton.setEnabled(enabled);
    }

    private void displayArtifactDetails(Artifact artifact) {
        nameField.setText(artifact.getName());
        idField.setText(String.valueOf(artifact.getID()));
        descriptionField.setText(artifact.getDescription());
        
        // Set the combo box selection based on the artifact status
        for (int i = 0; i < statusComboBox.getItemCount(); i++) {
            if (statusComboBox.getItemAt(i).equalsIgnoreCase(artifact.getStatus())) {
                statusComboBox.setSelectedIndex(i);
                break;
            }
        }
        
        weightField.setText(String.valueOf(artifact.getWeight()));
        priceField.setText(String.valueOf(artifact.getPrice()));
        dateField.setText(artifact.getArrivalDate());

        setEditFieldsEnabled(true);
    }

    private void clearFields() {
        nameField.setText("");
        idField.setText("");
        descriptionField.setText("");
        statusComboBox.setSelectedIndex(0);
        weightField.setText("");
        priceField.setText("");
        dateField.setText("");
    }

    private class ArtifactSelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                selectedIndex = artifactTable.getSelectedRow();
                if (selectedIndex >= 0) {
                    int id = (int) artifactTable.getValueAt(selectedIndex, 1); // Get ID from the table
                    selectedArtifact = artifactDAO.getArtifactById(id);
                    if (selectedArtifact != null) {
                        displayArtifactDetails(selectedArtifact);
                    }
                }
            }
        }
    }

    private class SaveButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                if (selectedIndex < 0) {
                    JOptionPane.showMessageDialog(EditArtifact.this, 
                            "Please select an artifact to edit.", 
                            "Selection Required", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Validate inputs
                String name = nameField.getText().trim();
                int id = Integer.parseInt(idField.getText().trim());
                String description = descriptionField.getText().trim();
                String status = statusComboBox.getSelectedItem().toString();
                
                // Validate weight and price as numbers
                double weight;
                double price;
                try {
                    weight = Double.parseDouble(weightField.getText().trim());
                    price = Double.parseDouble(priceField.getText().trim());
                    
                    if (weight <= 0 || price < 0) {
                        JOptionPane.showMessageDialog(EditArtifact.this, 
                                "Weight must be positive and price cannot be negative.",
                                "Invalid Input", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(EditArtifact.this, 
                            "Weight and price must be valid numbers.",
                            "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                String date = dateField.getText().trim();
                
                // Validate date format
                if (!date.matches("\\d{2}/\\d{2}/\\d{4}")) {
                    JOptionPane.showMessageDialog(EditArtifact.this, 
                            "Date must be in the format dd/MM/yyyy",
                            "Invalid Date Format", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (name.isEmpty() || description.isEmpty() || status.isEmpty() || date.isEmpty()) {
                    JOptionPane.showMessageDialog(EditArtifact.this, "All fields must be filled in.",
                            "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Create updated artifact
                Artifact updatedArtifact = new Artifact(id, name, description, status, weight, price, date);

                // Update in the database
                boolean success = artifactDAO.updateArtifact(updatedArtifact);
                
                if (success) {
                    // Refresh the artifact list from the database
                    artifactList = artifactDAO.getAllArtifacts();
                    
                    // Update the table
                    populateTable();
                    
                    JOptionPane.showMessageDialog(EditArtifact.this, 
                            "Artifact updated successfully!", 
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Clear selection
                    artifactTable.clearSelection();
                    selectedIndex = -1;
                    selectedArtifact = null;
                    setEditFieldsEnabled(false);
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(EditArtifact.this, 
                            "Failed to update artifact in the database.",
                            "Database Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(EditArtifact.this, 
                        "Error updating artifact: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private class CancelButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int response = JOptionPane.showConfirmDialog(
                EditArtifact.this,
                "Are you sure you want to exit? Any unsaved changes will be lost.",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (response == JOptionPane.YES_OPTION) {
                setVisible(false);
                dispose(); // Properly dispose the window
                
                // Return to previous menu
                if (EmployeeLogin.managerFlag()) {
                    new ManagerMenu();
                } else {
                    new CuratorMenu();
                }
            }
        }
    }
    
    // // For testing the UI independently
    // public static void main(String[] args) {
    //     SwingUtilities.invokeLater(() -> {
    //         new EditArtifact();
    //     });
    // }
}