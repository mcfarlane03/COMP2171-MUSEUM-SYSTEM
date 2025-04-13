import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.util.List;

public class DeleteArtifact extends JFrame {
    // Color scheme matching other UI components
    private static final Color PRIMARY_COLOR = new Color(25, 118, 210);    // Blue
    private static final Color SECONDARY_COLOR = new Color(245, 245, 245); // Light Gray
    private static final Color ACCENT_COLOR = new Color(211, 47, 47);      // Red
    private static final Color TEXT_COLOR = new Color(33, 33, 33);         // Dark Gray
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font TABLE_HEADER_FONT = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font CONTENT_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    
    private JPanel headerPanel = new JPanel();
    private JPanel contentPanel = new JPanel();
    private JPanel buttonPanel = new JPanel();
    private JPanel searchPanel = new JPanel();
    
    private JTable artifactsTable;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;
    
    private JTextField searchField = new JTextField(15);
    private JButton searchButton = new JButton("Search");
    private JButton resetButton = new JButton("Show All");
    private JButton removeButton = new JButton("Remove Selected");
    private JButton closeButton = new JButton("Back to Menu");
    
    private ArtifactDAO artifactDAO;
    
    public DeleteArtifact() {
        artifactDAO = new ArtifactDAO();
        
        setTitle("Remove Artifacts");
        setLayout(new BorderLayout());
        
        setupHeaderPanel();
        setupContentPanel();
        setupButtonPanel();
        
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
    
    private void setupHeaderPanel() {
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel("Delete Artifacts");
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Create search panel
        searchPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);
        
        JLabel searchLabel = new JLabel("Search by ID:");
        searchLabel.setForeground(Color.WHITE);
        searchLabel.setFont(CONTENT_FONT);
        
        searchField.setFont(CONTENT_FONT);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        styleButton(searchButton, PRIMARY_COLOR, Color.WHITE);
        styleButton(resetButton, PRIMARY_COLOR, Color.WHITE);
        
        searchButton.addActionListener(e -> searchArtifacts());
        resetButton.addActionListener(e -> loadAllArtifacts());
        
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(resetButton);
        
        headerPanel.add(searchPanel, BorderLayout.EAST);
    }
    
    private void setupContentPanel() {
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        contentPanel.setBackground(SECONDARY_COLOR);
        
        // Create table model with non-editable cells
        String[] columnNames = {
            "ID", "Name", "Description", "Status", "Weight", "Price", "Date of Arrival"
        };
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };
        
        artifactsTable = new JTable(tableModel);
        configureTable();
        
        // Load all artifacts initially
        loadAllArtifacts();
        
        scrollPane = new JScrollPane(artifactsTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        contentPanel.add(scrollPane, BorderLayout.CENTER);
    }
    
    private void configureTable() {
        artifactsTable.setFont(CONTENT_FONT);
        artifactsTable.setRowHeight(25);
        artifactsTable.setGridColor(new Color(230, 230, 230));
        artifactsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Style the table header
        JTableHeader header = artifactsTable.getTableHeader();
        header.setFont(TABLE_HEADER_FONT);
        header.setBackground(new Color(240, 240, 240));
        header.setForeground(TEXT_COLOR);
        
        // Set column widths
        TableColumnModel columnModel = artifactsTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);    // ID
        columnModel.getColumn(1).setPreferredWidth(150);   // Name
        columnModel.getColumn(2).setPreferredWidth(250);   // Description
        columnModel.getColumn(3).setPreferredWidth(80);    // Status
        columnModel.getColumn(4).setPreferredWidth(70);    // Weight
        columnModel.getColumn(5).setPreferredWidth(70);    // Price
        columnModel.getColumn(6).setPreferredWidth(100);   // Date
        
        // Enable sorting
        artifactsTable.setAutoCreateRowSorter(true);
        
        // Add selection listener to enable/disable remove button based on selection
        artifactsTable.getSelectionModel().addListSelectionListener(e -> {
            boolean rowSelected = artifactsTable.getSelectedRow() != -1;
            removeButton.setEnabled(rowSelected);
        });
    }
    
    private void setupButtonPanel() {
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Style buttons
        styleButton(closeButton, Color.WHITE, TEXT_COLOR);
        styleButton(removeButton, ACCENT_COLOR, Color.WHITE);
        
        // Disable remove button initially (until selection is made)
        removeButton.setEnabled(false);
        
        // Add action listeners
        removeButton.addActionListener(e -> removeSelectedArtifact());
        closeButton.addActionListener(e -> {
            setVisible(false);
            new CuratorMenu();
        });
        
        buttonPanel.add(closeButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(removeButton);
    }
    
    private void styleButton(JButton button, Color bgColor, Color fgColor) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(fgColor);
        button.setBackground(bgColor);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker()),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        button.setFocusPainted(false);
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
    }
    
    private void loadAllArtifacts() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Fetch all artifacts from the database
        List<Artifact> artifacts = artifactDAO.getAllArtifacts();
        
        // Add artifacts to table
        for (Artifact artifact : artifacts) {
            addArtifactToTable(artifact);
        }
        
        // Display message if no artifacts found
        if (artifacts.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "No artifacts found in the database.",
                "Information",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
    
    private void searchArtifacts() {
        try {
            // Parse ID from the search field
            String searchText = searchField.getText().trim();
            
            if (searchText.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this,
                    "Please enter an artifact ID to search.",
                    "Search Error",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            
            int id = Integer.parseInt(searchText);
            
            // Clear existing data
            tableModel.setRowCount(0);
            
            // Search for the artifact by ID
            Artifact artifact = artifactDAO.getArtifactById(id);
            
            if (artifact != null) {
                addArtifactToTable(artifact);
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "No artifact found with ID: " + id,
                    "Search Result",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                this,
                "Please enter a valid numeric ID.",
                "Input Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    private void addArtifactToTable(Artifact artifact) {
        Object[] row = {
            artifact.getID(),
            artifact.getName(),
            artifact.getDescription(),
            artifact.getStatus(),
            artifact.getWeight(),
            artifact.getPrice(),
            artifact.getArrivalDate()
        };
        tableModel.addRow(row);
    }
    
    private void removeSelectedArtifact() {
        int viewRow = artifactsTable.getSelectedRow();
        
        if (viewRow < 0) {
            JOptionPane.showMessageDialog(
                this,
                "Please select an artifact to delete.",
                "Selection Required",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        // Convert view row index to model row index in case the table is sorted
        int modelRow = artifactsTable.convertRowIndexToModel(viewRow);
        
        // Get the artifact ID from the first column
        int artifactId = (Integer) tableModel.getValueAt(modelRow, 0);
        
        // Confirm deletion
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete the artifact with ID: " + artifactId + "?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Delete from database
            boolean success = artifactDAO.deleteArtifact(artifactId);
            
            if (success) {
                // Remove from table model
                tableModel.removeRow(modelRow);
                
                JOptionPane.showMessageDialog(
                    this,
                    "Artifact successfully deleted.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Failed to delete artifact. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}