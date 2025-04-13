import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AddArtifactUI extends JFrame {
    // Color scheme matching CuratorMenu
    private static final Color PRIMARY_COLOR = new Color(25, 118, 210);    // Blue
    private static final Color SECONDARY_COLOR = new Color(245, 245, 245); // Light Gray
    private static final Color ACCENT_COLOR = new Color(211, 47, 47);      // Red
    private static final Color TEXT_COLOR = new Color(33, 33, 33);         // Dark Gray
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font INPUT_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    
    private JPanel formPanel = new JPanel();
    private JPanel headerPanel = new JPanel();
    private JPanel statusPanel = new JPanel();
    private JPanel buttonPanel = new JPanel();

    private JCheckBox soldCheckBox = new JCheckBox("Sold");
    private JCheckBox excavatedCheckBox = new JCheckBox("Excavated");
    private JCheckBox donatedCheckBox = new JCheckBox("Donated");

    private JTextField nameField = new JTextField(20);
    private JTextArea descriptionArea = new JTextArea(4, 20);
    private JTextField arrivalDateField = new JTextField(10);
    private JTextField idField = new JTextField(10);
    private JTextField priceField = new JTextField(10);
    private JTextField weightField = new JTextField(10);

    private JButton closeButton = new JButton("Cancel");
    private JButton saveButton = new JButton("Save Artifact");
    
    // Database access object
    private ArtifactDAO artifactDAO;

    public AddArtifactUI() {
        // Initialize DAO
        artifactDAO = new ArtifactDAO();
        
        setTitle("Add New Artifact");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Initialize panels with improved styling
        setupHeaderPanel();
        setupFormPanel();
        setupStatusPanel();
        setupButtonPanel();
        
        // Add panels to frame
        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Set window properties
        setSize(550, 750);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void setupHeaderPanel() {
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel("Add New Artifact");
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JButton backButton = new JButton("← Back to Menu");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(PRIMARY_COLOR);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backButton.setBackground(new Color(41, 128, 185));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                backButton.setBackground(PRIMARY_COLOR);
            }
        });
        
        backButton.addActionListener(e -> {
            setVisible(false);
            new CuratorMenu();
        });
        
        headerPanel.add(backButton, BorderLayout.EAST);
    }
    
    private void setupFormPanel() {
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(SECONDARY_COLOR);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create a scroll pane for the form
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);
        
        // Style the description text area
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        descScrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
        // Add form fields with improved layout
        formPanel.add(createFormField("Artifact Name:", nameField));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        formPanel.add(createFormField("Identification Number:", idField));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel descriptionPanel = new JPanel();
        descriptionPanel.setLayout(new BoxLayout(descriptionPanel, BoxLayout.Y_AXIS));
        descriptionPanel.setBackground(SECONDARY_COLOR);
        JLabel descLabel = new JLabel("Description:");
        descLabel.setFont(LABEL_FONT);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        descriptionPanel.add(descLabel);
        descriptionPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        descScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        descriptionPanel.add(descScrollPane);
        formPanel.add(descriptionPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        formPanel.add(createFormField("Arrival Date [dd/mm/yyyy]:", arrivalDateField));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        formPanel.add(createFormField("Price:", priceField));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        formPanel.add(createFormField("Weight:", weightField));
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Add status panel within the form
        setupStatusPanel();
        formPanel.add(statusPanel);
    }
    
    private JPanel createFormField(String labelText, JTextField field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(SECONDARY_COLOR);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel label = new JLabel(labelText);
        label.setFont(LABEL_FONT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        field.setFont(INPUT_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(field);
        
        return panel;
    }
    
    private void setupStatusPanel() {
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
        statusPanel.setBackground(SECONDARY_COLOR);
        statusPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Artifact Status",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                LABEL_FONT
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        statusPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Create checkbox panel with horizontal layout
        JPanel checkBoxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        checkBoxPanel.setBackground(SECONDARY_COLOR);
        
        // Style checkboxes
        styleCheckBox(soldCheckBox);
        styleCheckBox(excavatedCheckBox);
        styleCheckBox(donatedCheckBox);
        
        // Add checkboxes to panel
        checkBoxPanel.add(soldCheckBox);
        checkBoxPanel.add(excavatedCheckBox);
        checkBoxPanel.add(donatedCheckBox);
        
        statusPanel.add(checkBoxPanel);
    }
    
    private void styleCheckBox(JCheckBox checkBox) {
        checkBox.setFont(INPUT_FONT);
        checkBox.setBackground(SECONDARY_COLOR);
        checkBox.setFocusPainted(false);
    }
    
    private void setupButtonPanel() {
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        // Style close button
        closeButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        closeButton.setForeground(TEXT_COLOR);
        closeButton.setBackground(Color.WHITE);
        closeButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        closeButton.setFocusPainted(false);
        
        // Style save button
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveButton.setForeground(Color.WHITE);
        saveButton.setBackground(PRIMARY_COLOR);
        saveButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(13, 71, 161)),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        saveButton.setFocusPainted(false);
        
        // Add hover effects
        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                closeButton.setBackground(new Color(240, 240, 240));
                closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                closeButton.setBackground(Color.WHITE);
            }
        });
        
        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                saveButton.setBackground(new Color(33, 150, 243));
                saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                saveButton.setBackground(PRIMARY_COLOR);
            }
        });
        
        // Add action listeners
        closeButton.addActionListener(e -> {
            setVisible(false);
            new CuratorMenu();
        });
        
        saveButton.addActionListener(new SaveListener());
        
        // Add buttons to panel
        buttonPanel.add(closeButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(saveButton);
    }
    
    private class SaveListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String status = "";

            try {
                // determining which checkbox is selected for status:
                if (soldCheckBox.isSelected())
                    status = "Sold";
                else if (excavatedCheckBox.isSelected())
                    status = "Excavated";
                else if (donatedCheckBox.isSelected())
                    status = "Donated";

                // Get values from text fields
                String name = nameField.getText();
                int id = Integer.parseInt(idField.getText());
                String description = descriptionArea.getText();
                double weight = Double.parseDouble(weightField.getText());
                double price = Double.parseDouble(priceField.getText());
                String arrivalDate = arrivalDateField.getText();
                
                // Validate inputs
                if (name.trim().isEmpty() || description.trim().isEmpty() || arrivalDate.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(
                        AddArtifactUI.this,
                        "Please fill in all required fields.",
                        "Missing Information",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }
                
                if (status.isEmpty()) {
                    JOptionPane.showMessageDialog(
                        AddArtifactUI.this,
                        "Please select a status for the artifact.",
                        "Missing Status",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }
                
                // Create new Artifact object
                Artifact artifact = new Artifact(
                    id,             // ID
                    name,           // Name
                    description,    // Description
                    status,         // Status
                    weight,         // Weight
                    price,          // Price
                    arrivalDate     // Arrival Date
                );
                
                // Save to database using DAO
                boolean success = artifactDAO.addArtifact(artifact);
                
                if (success) {
                    JOptionPane.showMessageDialog(
                        AddArtifactUI.this,
                        "Artifact saved successfully to database.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    
                    // Close window and return to menu
                    setVisible(false);
                    new CuratorMenu();
                } else {
                    JOptionPane.showMessageDialog(
                        AddArtifactUI.this,
                        "Failed to save artifact to database.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
                
            } catch(NumberFormatException nfe) {
                JOptionPane.showMessageDialog(
                    AddArtifactUI.this,
                    "Please enter valid numbers for ID, Price, and Weight fields.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE
                );
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(
                    AddArtifactUI.this,
                    "Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}