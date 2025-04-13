import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.util.List;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SearchArtifact extends JFrame {
    // Color scheme - matching ViewArtifact for consistency
    private final Color PRIMARY_COLOR = new Color(44, 62, 80);    // Dark blue
    private final Color ACCENT_COLOR = new Color(52, 152, 219);   // Bright blue
    private final Color BUTTON_COLOR = new Color(41, 128, 185);   // Slightly darker blue
    private final Color TEXT_COLOR = new Color(236, 240, 241);    // Off-white
    private final Color PANEL_BG = new Color(236, 240, 241);      // Light gray
    private final Color TABLE_HEADER_BG = new Color(52, 73, 94);  // Darker blue
    private final Color TABLE_ALT_ROW = new Color(245, 246, 250); // Very light blue

    // Panels
    private JPanel mainPanel;
    private JPanel searchPanel;
    private JPanel tablePanel;
    private JPanel buttonPanel;
    
    // Search components
    private JTextField searchField;
    private JComboBox<String> searchTypeComboBox;
    private JButton searchButton;
    private JLabel searchResultsLabel;
    
    // Table components
    private JTable resultsTable;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;
    
    // Buttons
    private JButton viewDetailsButton;
    private JButton clearButton;
    private JButton closeButton;
    
    // Data Access Object
    private ArtifactDAO artifactDAO;
    
    // Search results
    private List<Artifact> searchResults;
    
    public SearchArtifact() {
        // Basic frame setup
        setTitle("Search Artifacts");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(900, 600));
        
        // Initialize DAO
        artifactDAO = new ArtifactDAO();
        
        // Initialize components
        initializeUI();
        
        // Layout components
        layoutComponents();
        
        // Add event listeners
        setupEventListeners();
        
        // Display
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void initializeUI() {
        // Create main panel with border layout
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(PANEL_BG);
        
        // Create search panel
        initializeSearchPanel();
        
        // Create table panel
        initializeTablePanel();
        
        // Create button panel
        initializeButtonPanel();
    }
    
    private void initializeSearchPanel() {
        searchPanel = new JPanel(new BorderLayout(10, 10));
        searchPanel.setBackground(PANEL_BG);
        searchPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR), 
            "Search Criteria", 
            TitledBorder.LEFT, 
            TitledBorder.TOP,
            new Font("SansSerif", Font.BOLD, 12),
            PRIMARY_COLOR
        ));
        
        // Create inner panel for search components
        JPanel searchControlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchControlsPanel.setBackground(PANEL_BG);
        
        // Search type combo box
        String[] searchTypes = {"Name", "ID", "Status", "Description"};
        searchTypeComboBox = new JComboBox<>(searchTypes);
        searchTypeComboBox.setPreferredSize(new Dimension(120, 30));
        searchTypeComboBox.setBackground(Color.WHITE);
        
        // Search field
        searchField = new JTextField(25);
        searchField.setPreferredSize(new Dimension(200, 30));
        
        // Search button
        searchButton = createStyledButton("Search", "search.png");
        searchButton.setPreferredSize(new Dimension(100, 30));
        
        // Add components to search controls panel
        searchControlsPanel.add(new JLabel("Search by:"));
        searchControlsPanel.add(searchTypeComboBox);
        searchControlsPanel.add(searchField);
        searchControlsPanel.add(searchButton);
        
        // Search results label
        searchResultsLabel = new JLabel("Enter search criteria and click Search");
        searchResultsLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        searchResultsLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        // Add components to search panel
        searchPanel.add(searchControlsPanel, BorderLayout.CENTER);
        searchPanel.add(searchResultsLabel, BorderLayout.SOUTH);
    }
    
    private void initializeTablePanel() {
        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(PANEL_BG);
        
        // Create table
        String[] columnNames = {
            "Name", "ID #", "Brief Description", "Status", 
            "Weight", "Price", "Date of Arrival"
        };
        tableModel = new DefaultTableModel(columnNames, 0);
        resultsTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
            
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer, row, column);
                // Alternate row colors for better readability
                if (!comp.getBackground().equals(getSelectionBackground())) {
                    comp.setBackground(row % 2 == 0 ? Color.WHITE : TABLE_ALT_ROW);
                }
                return comp;
            }
        };
        
        // Style table
        resultsTable.setRowHeight(25);
        resultsTable.setIntercellSpacing(new Dimension(10, 5));
        resultsTable.setShowGrid(false);
        resultsTable.setFillsViewportHeight(true);
        
        // Style table header
        JTableHeader header = resultsTable.getTableHeader();
        header.setBackground(TABLE_HEADER_BG);
        header.setForeground(TEXT_COLOR);
        header.setFont(new Font("SansSerif", Font.BOLD, 12));
        
        // Create scroll pane for table
        scrollPane = new JScrollPane(resultsTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 1));
        
        // Add scroll pane to table panel
        tablePanel.add(scrollPane, BorderLayout.CENTER);
    }
    
    private void initializeButtonPanel() {
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(PANEL_BG);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        // Create buttons
        viewDetailsButton = createStyledButton("View Details", "view.png");
        clearButton = createStyledButton("Clear Results", "clear.png");
        closeButton = createStyledButton("Close", "close.png");
        
        // Customize close button
        closeButton.setBackground(new Color(231, 76, 60)); // Red color for close button
        
        // Add buttons to panel
        buttonPanel.add(viewDetailsButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(closeButton);
    }
    
    private JButton createStyledButton(String text, String iconName) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setBackground(BUTTON_COLOR);
        button.setForeground(TEXT_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(130, 35));
        
        // Add rollover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(ACCENT_COLOR);
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_COLOR);
                if (button == closeButton) {
                    button.setBackground(new Color(231, 76, 60));
                }
            }
        });
        
        return button;
    }
    
    private void layoutComponents() {
        // Add panels to main panel
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add main panel to frame
        setContentPane(mainPanel);
    }
    
    private void setupEventListeners() {
        // Search button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });
        
        // Search field - enable search on Enter key
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performSearch();
                }
            }
        });
        
        // View details button
        viewDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewSelectedArtifactDetails();
            }
        });
        
        // Clear button
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearSearch();
            }
        });
        
        // Close button
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new CuratorMenu();
            }
        });
    }
    
    private void performSearch() {
        String searchText = searchField.getText().trim();
        
        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter search criteria",
                "Search Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Clear previous results
        tableModel.setRowCount(0);
        
        // Get search type
        String searchType = (String) searchTypeComboBox.getSelectedItem();
        
        try {
            // Perform search based on selected type
            switch (searchType) {
                case "Name":
                    searchResults = artifactDAO.searchArtifactsByName(searchText);
                    break;
                case "ID":
                    try {
                        int id = Integer.parseInt(searchText);
                        Artifact artifact = artifactDAO.getArtifactById(id);
                        searchResults = new ArrayList<>();
                        if (artifact != null) {
                            searchResults.add(artifact);
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this,
                            "Please enter a valid numeric ID",
                            "Search Error",
                            JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    break;
                case "Status":
                    searchResults = artifactDAO.getArtifactsByStatus(searchText);
                    break;
                case "Description":
                    // This would require adding a method to the DAO
                    JOptionPane.showMessageDialog(this,
                        "Search by description not implemented in the DAO",
                        "Feature Not Available",
                        JOptionPane.INFORMATION_MESSAGE);
                    return;
                default:
                    return;
            }
            
            // Display results
            displaySearchResults();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error performing search: " + e.getMessage(),
                "Search Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void displaySearchResults() {
        // Clear the table
        tableModel.setRowCount(0);
        
        // Check if we have results
        if (searchResults == null || searchResults.isEmpty()) {
            searchResultsLabel.setText("No artifacts found matching your criteria");
            return;
        }
        
        // Add results to table
        for (Artifact artifact : searchResults) {
            addToTable(artifact);
        }
        
        // Update results label
        searchResultsLabel.setText(searchResults.size() + " artifact(s) found");
    }
    
    private void addToTable(Artifact a) {
        // Format values for better display
        String price = String.format("$%,.2f", a.getPrice());
        String weight = String.format("%.2f kg", a.getWeight());
        
        String[] item = { 
            a.getName(), 
            String.valueOf(a.getID()), 
            a.getDescription(), 
            a.getStatus(), 
            weight,
            price, 
            a.getArrivalDate() 
        };
        tableModel.addRow(item);
    }
    
    private void viewSelectedArtifactDetails() {
        int selectedRow = resultsTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select an artifact from the results table",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Get the artifact ID from the selected row
        String idStr = (String) tableModel.getValueAt(selectedRow, 1);
        int artifactId = Integer.parseInt(idStr);
        
        // Here you would typically open a detailed view of the artifact
        // For now, we'll just show a dialog with some details
        Artifact artifact = artifactDAO.getArtifactById(artifactId);
        
        if (artifact != null) {
            String message = "Artifact Details:\n\n" +
                            "Name: " + artifact.getName() + "\n" +
                            "ID: " + artifact.getID() + "\n" +
                            "Description: " + artifact.getDescription() + "\n" +
                            "Status: " + artifact.getStatus() + "\n" +
                            "Weight: " + String.format("%.2f kg", artifact.getWeight()) + "\n" +
                            "Price: " + String.format("$%,.2f", artifact.getPrice()) + "\n" +
                            "Arrival Date: " + artifact.getArrivalDate();
            
            JOptionPane.showMessageDialog(this,
                message,
                "Artifact Details",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void clearSearch() {
        // Clear search field
        searchField.setText("");
        
        // Clear results
        tableModel.setRowCount(0);
        searchResults = null;
        
        // Reset label
        searchResultsLabel.setText("Enter search criteria and click Search");
    }
}