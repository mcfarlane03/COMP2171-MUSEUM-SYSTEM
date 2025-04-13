import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.util.ArrayList;
import java.util.List;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewArtifact extends JFrame {
    // Color scheme
    private final Color PRIMARY_COLOR = new Color(44, 62, 80);    // Dark blue
    private final Color ACCENT_COLOR = new Color(52, 152, 219);   // Bright blue
    private final Color BUTTON_COLOR = new Color(41, 128, 185);   // Slightly darker blue
    private final Color TEXT_COLOR = new Color(236, 240, 241);    // Off-white
    private final Color PANEL_BG = new Color(236, 240, 241);      // Light gray
    private final Color TABLE_HEADER_BG = new Color(52, 73, 94);  // Darker blue
    private final Color TABLE_ALT_ROW = new Color(245, 246, 250); // Very light blue

    // Panels
    private JPanel mainPanel;
    private JPanel tablePanel;
    private JPanel filterPanel;
    private JPanel buttonPanel;
    
    // Table components
    private JTable table;
    private DefaultTableModel model;
    private JScrollPane scrollPane;
    
    // Buttons
    private JButton sortNameBtn;
    private JButton sortPriceBtn;
    private JButton sortWeightBtn;
    private JButton sortDateBtn;
    private JButton exportBtn;
    private JButton closeBtn;
    private JButton resetBtn;
    
    // Filter checkboxes - Weight
    private JPanel weightFilterPanel;
    private JCheckBox range0to20;
    private JCheckBox range20to100;
    private JCheckBox rangeAbove100;
    
    // Filter checkboxes - Price
    private JPanel priceFilterPanel;
    private JCheckBox priceRange1;
    private JCheckBox priceRange2;
    private JCheckBox priceRange3;
    
    // Data
    private List<Artifact> artifactList;
    private List<Artifact> originalList;
    
    // Data Access Object
    private ArtifactDAO artifactDAO;
    
    // Filter state tracking
    private boolean weight0to20Selected = false;
    private boolean weight20to100Selected = false;
    private boolean weightAbove100Selected = false;
    private boolean price0to100kSelected = false;
    private boolean price100kto1mSelected = false;
    private boolean priceAbove1mSelected = false;
    
    private EmployeeDAO employeeDAO = new EmployeeDAO();

    public ViewArtifact() {
        // Basic frame setup
        setTitle("Artifact Catalogue Viewer");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 600));
        
        // Initialize DAO
        artifactDAO = new ArtifactDAO();
        
        // Load data from database
        loadArtifactsFromDatabase();
        
        // Initialize UI components
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
    
    private void loadArtifactsFromDatabase() {
        try {
            // Get all artifacts from the database
            artifactList = artifactDAO.getAllArtifacts();
            
            // Create a copy for filtering operations
            originalList = new ArrayList<>(artifactList);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading artifact data from database: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void initializeUI() {
        // Create main panel with border layout
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(PANEL_BG);
        
        // Create table
        String[] columnNames = {
            "Name", "ID #", "Brief Description", "Status", 
            "Weight", "Price", "Date of Arrival"
        };
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model) {
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
        table.setRowHeight(25);
        table.setIntercellSpacing(new Dimension(10, 5));
        table.setShowGrid(false);
        table.setFillsViewportHeight(true);
        
        // Style table header
        JTableHeader header = table.getTableHeader();
        header.setBackground(TABLE_HEADER_BG);
        header.setForeground(TEXT_COLOR);
        header.setFont(new Font("SansSerif", Font.BOLD, 12));
        
        // Create scroll pane for table
        scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 1));
        
        // Create table panel
        tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Create filter panels
        initializeFilterPanels();
        
        // Create buttons
        initializeButtons();
    }
    
    private void initializeFilterPanels() {
        // Create main filter panel
        filterPanel = new JPanel(new BorderLayout(0, 10));
        filterPanel.setBackground(PANEL_BG);
        filterPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
        
        // Weight filter panel
        weightFilterPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        weightFilterPanel.setBackground(PANEL_BG);
        weightFilterPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR), 
            "Filter by Weight", 
            TitledBorder.LEFT, 
            TitledBorder.TOP,
            new Font("SansSerif", Font.BOLD, 12),
            PRIMARY_COLOR
        ));
        
        range0to20 = createStyledCheckBox("0 - 20 kg");
        range20to100 = createStyledCheckBox("20 - 100 kg");
        rangeAbove100 = createStyledCheckBox(">100 kg");
        
        weightFilterPanel.add(range0to20);
        weightFilterPanel.add(range20to100);
        weightFilterPanel.add(rangeAbove100);
        
        // Price filter panel
        priceFilterPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        priceFilterPanel.setBackground(PANEL_BG);
        priceFilterPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR), 
            "Filter by Price", 
            TitledBorder.LEFT, 
            TitledBorder.TOP,
            new Font("SansSerif", Font.BOLD, 12),
            PRIMARY_COLOR
        ));
        
        priceRange1 = createStyledCheckBox("$0 - $100,000");
        priceRange2 = createStyledCheckBox("$100,000 - $1,000,000");
        priceRange3 = createStyledCheckBox(">$1,000,000");
        
        priceFilterPanel.add(priceRange1);
        priceFilterPanel.add(priceRange2);
        priceFilterPanel.add(priceRange3);
        
        // Add filter panels to main filter panel
        JPanel filtersContainer = new JPanel(new GridLayout(2, 1, 0, 15));
        filtersContainer.setBackground(PANEL_BG);
        filtersContainer.add(weightFilterPanel);
        filtersContainer.add(priceFilterPanel);
        
        filterPanel.add(filtersContainer, BorderLayout.NORTH);
    }
    
    private JCheckBox createStyledCheckBox(String text) {
        JCheckBox checkBox = new JCheckBox(text);
        checkBox.setBackground(PANEL_BG);
        checkBox.setForeground(PRIMARY_COLOR);
        checkBox.setFont(new Font("SansSerif", Font.PLAIN, 12));
        checkBox.setFocusPainted(false);
        return checkBox;
    }
    
    private void initializeButtons() {
        // Create button panel
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(PANEL_BG);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        // Create buttons
        sortNameBtn = createStyledButton("Sort by Name", "sort_name.png");
        sortPriceBtn = createStyledButton("Sort by Price", "sort_price.png");
        sortWeightBtn = createStyledButton("Sort by Weight", "sort_weight.png");
        sortDateBtn = createStyledButton("Sort by Date", "sort_date.png");
        exportBtn = createStyledButton("Export Results", "export.png");
        resetBtn = createStyledButton("Reset Filters", "reset.png");
        closeBtn = createStyledButton("Close", "close.png");
        
        // Customize close button
        closeBtn.setBackground(new Color(231, 76, 60)); // Red color for close button
        
        // Add buttons to panel
        buttonPanel.add(sortNameBtn);
        buttonPanel.add(sortPriceBtn);
        buttonPanel.add(sortWeightBtn);
        buttonPanel.add(sortDateBtn);
        buttonPanel.add(exportBtn);
        buttonPanel.add(resetBtn);
        buttonPanel.add(closeBtn);
    }
    
    private JButton createStyledButton(String text, String iconName) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setBackground(BUTTON_COLOR);
        button.setForeground(TEXT_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(150, 35));
        
        // Add rollover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(ACCENT_COLOR);
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_COLOR);
                if (button == closeBtn) {
                    button.setBackground(new Color(231, 76, 60));
                }
            }
        });
        
        return button;
    }
    
    private void layoutComponents() {
        // Populate table
        showTable(artifactList);
        
        // Add components to main panel
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(filterPanel, BorderLayout.EAST);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add main panel to frame
        setContentPane(mainPanel);
    }
    
    private void setupEventListeners() {
        // Sort buttons
        sortNameBtn.addActionListener(new SortNameListener());
        sortPriceBtn.addActionListener(new SortByPriceListener());
        sortWeightBtn.addActionListener(new SortByWeightListener());
        sortDateBtn.addActionListener(new SortDateListener());
        
        // Filter checkboxes
        range0to20.addActionListener(new FilterListener(0));
        range20to100.addActionListener(new FilterListener(1));
        rangeAbove100.addActionListener(new FilterListener(2));
        priceRange1.addActionListener(new FilterListener(3));
        priceRange2.addActionListener(new FilterListener(4));
        priceRange3.addActionListener(new FilterListener(5));
        
        // Other buttons
        exportBtn.addActionListener(new ExportResultsListener());
        resetBtn.addActionListener(new ResetListener());
        closeBtn.addActionListener(new CloseButtonListener());
    }
    
    private void showTable(List<Artifact> alist) {
        // Clear the table
        model.setRowCount(0);
        
        // Add artifacts to table
        if (alist != null && !alist.isEmpty()) {
            for (Artifact artifact : alist) {
                addToTable(artifact);
            }
        }
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
        model.addRow(item);
    }
    
    private void applyAllFilters() {
        // Get filtered results from database based on selected filters
        List<Artifact> filteredList = new ArrayList<>();
        
        // If no price filters are selected, get all artifacts
        if (!price0to100kSelected && !price100kto1mSelected && !priceAbove1mSelected &&
            !weight0to20Selected && !weight20to100Selected && !weightAbove100Selected) {
            filteredList = new ArrayList<>(originalList);
        } else {
            // Start with original list and apply filters
            filteredList = new ArrayList<>(originalList);
            List<Artifact> tempList = new ArrayList<>();
            
            // Apply weight filters if any are selected
            if (weight0to20Selected || weight20to100Selected || weightAbove100Selected) {
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
                        tempList.add(artifact);
                    }
                }
                
                filteredList = new ArrayList<>(tempList);
                tempList.clear();
            }
            
            // Apply price filters if any are selected
            if (price0to100kSelected || price100kto1mSelected || priceAbove1mSelected) {
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
                        tempList.add(artifact);
                    }
                }
                
                filteredList = tempList;
            }
        }
        
        // Update the display with filtered results
        artifactList = filteredList;
        showTable(artifactList);
    }
    
    // ====== Event Listeners ======
    
    private class SortNameListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            artifactList.sort(new NameComparator());
            showTable(artifactList);
        }
    }
    
    private class SortByPriceListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            artifactList.sort(new PriceComparator());
            showTable(artifactList);
        }
    }
    
    private class SortByWeightListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            artifactList.sort(new WeightComparator());
            showTable(artifactList);
        }
    }
    
    private class SortDateListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            artifactList.sort(new DateComparator());
            showTable(artifactList);
        }
    }
    
    private class FilterListener implements ActionListener {
        private int filterType;
        
        public FilterListener(int filterType) {
            this.filterType = filterType;
        }
        
        public void actionPerformed(ActionEvent e) {
            switch (filterType) {
                case 0: weight0to20Selected = range0to20.isSelected(); break;
                case 1: weight20to100Selected = range20to100.isSelected(); break;
                case 2: weightAbove100Selected = rangeAbove100.isSelected(); break;
                case 3: price0to100kSelected = priceRange1.isSelected(); break;
                case 4: price100kto1mSelected = priceRange2.isSelected(); break;
                case 5: priceAbove1mSelected = priceRange3.isSelected(); break;
            }
            applyAllFilters();
        }
    }
    
    private class ExportResultsListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(ViewArtifact.this, 
                "Export functionality not yet implemented.", 
                "Information", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private class ResetListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Reset all filter checkboxes
            range0to20.setSelected(false);
            range20to100.setSelected(false);
            rangeAbove100.setSelected(false);
            priceRange1.setSelected(false);
            priceRange2.setSelected(false);
            priceRange3.setSelected(false);
            
            // Reset filter state variables
            weight0to20Selected = false;
            weight20to100Selected = false;
            weightAbove100Selected = false;
            price0to100kSelected = false;
            price100kto1mSelected = false;
            priceAbove1mSelected = false;
            
            // Reset the list and update the display
            artifactList = new ArrayList<>(originalList);
            showTable(artifactList);
        }
    }
    
    private class CloseButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            dispose();
            // new ManagerMenu();

            // Navigate to the appropriate menu based on role
            if (EmployeeLogin.managerFlag())
                new ManagerMenu();
            else
                new CuratorMenu();
        }
    }
    
    // ====== Comparators ======
    
    private static class NameComparator implements Comparator<Artifact> {
        public int compare(Artifact a1, Artifact a2) {
            return a1.getName().compareTo(a2.getName());
        }
    }
    
    private static class DateComparator implements Comparator<Artifact> {
        public int compare(Artifact a1, Artifact a2) {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date d1 = df.parse(a1.getArrivalDate());
                Date d2 = df.parse(a2.getArrivalDate());
                return d1.compareTo(d2);
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
        }
    }
    
    private static class PriceComparator implements Comparator<Artifact> {
        public int compare(Artifact a1, Artifact a2) {
            return Double.compare(a1.getPrice(), a2.getPrice());
        }
    }
    
    private static class WeightComparator implements Comparator<Artifact> {
        public int compare(Artifact a1, Artifact a2) {
            return Double.compare(a1.getWeight(), a2.getWeight());
        }
    }
}