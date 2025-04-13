import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.io.IOException;

class CuratorMenu extends JFrame {
    // Color scheme
    private static final Color PRIMARY_COLOR = new Color(25, 118, 210);    // Blue
    private static final Color SECONDARY_COLOR = new Color(245, 245, 245); // Light Gray
    private static final Color ACCENT_COLOR = new Color(211, 47, 47);      // Red
    private static final Color TEXT_COLOR = new Color(33, 33, 33);         // Dark Gray
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font MENU_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    
    // Icons for menu items (using Unicode characters as placeholders)
    private static final String VIEW_ICON = "🔍";
    private static final String ADD_ICON = "➕";
    private static final String SEARCH_ICON = "🔎";
    private static final String EDIT_ICON = "✏️";
    private static final String DELETE_ICON = "🗑️";
    private static final String LOGOUT_ICON = "<-";

    public CuratorMenu() {
        setTitle("Nationoal Museum of Jamaica Inventory Management System");
        setLayout(new BorderLayout());
        
        // Create header panel with title and logout button
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Create main content panel with stacked options
        JPanel contentPanel = createContentPanel();
        add(contentPanel, BorderLayout.CENTER);
        
        // Create footer panel
        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);
        
        // Set up window properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null); // Center on screen
        setVisible(true);
        
        // Use custom icon if available
        try {
            // Replace with your actual icon path if you have one
            // setIconImage(ImageIO.read(getClass().getResource("/icons/museum_icon.png")));
        } catch (Exception e) {
            // Silently fail if icon isn't available
        }
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        
        JLabel titleLabel = new JLabel("Inventory Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Create a panel for the date and logout button
        JPanel rightPanel = new JPanel(new BorderLayout(10, 0));
        rightPanel.setOpaque(false);
        
        // Add current date
        JLabel dateLabel = new JLabel(new java.util.Date().toString());
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateLabel.setForeground(Color.WHITE);
        rightPanel.add(dateLabel, BorderLayout.WEST);
        
        // Add logout button to the top right
        JButton logoutButton = createLogoutButton();
        rightPanel.add(logoutButton, BorderLayout.EAST);
        
        headerPanel.add(rightPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JButton createLogoutButton() {
        JButton logoutButton = new JButton(LOGOUT_ICON + " Log Out");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(new Color(231, 76, 60)); // Red color
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setPreferredSize(new Dimension(105, 40));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setToolTipText("Exit the curator system");
        
        // Add hover effect
        logoutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                logoutButton.setBackground(new Color(231, 76, 60)); // Brighter red on hover
                logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                logoutButton.setBackground(ACCENT_COLOR);
            }
        });
        
        logoutButton.addActionListener(e -> {
            setVisible(false);
            new EmployeeLogin();
        });
        
        return logoutButton;
    }
    
    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(SECONDARY_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Use BoxLayout for stacked options instead of GridLayout
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        
        // Create action buttons
        JButton viewButton = createMenuButton(VIEW_ICON + " View Artifacts", "Browse the complete collection of museum artifacts");
        JButton addButton = createMenuButton(ADD_ICON + " Add Artifact", "Register a new artifact in the system");
        JButton searchButton = createMenuButton(SEARCH_ICON + " Search Artifacts", "Find artifacts by name, era, or other criteria");
        JButton editButton = createMenuButton(EDIT_ICON + " Edit Artifact", "Modify details of existing artifacts");
        JButton deleteButton = createMenuButton(DELETE_ICON + " Delete Artifact", "Remove artifacts from the system");
        
        // Add action listeners
        viewButton.addActionListener(e -> {
            new ViewArtifact();
            setVisible(false);
        });
        
        addButton.addActionListener(e -> {
            new AddArtifactUI();
            setVisible(false);
        });
        
        searchButton.addActionListener(e -> {
            new SearchArtifact();
            setVisible(false);
        });
        
        editButton.addActionListener(e -> {
            new EditArtifact();
            setVisible(false);
        });
        
        deleteButton.addActionListener(e -> {
            new DeleteArtifact();
            setVisible(false);
        });
        
        // Add all buttons to the panel with spacing
        contentPanel.add(viewButton);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(addButton);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(searchButton);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(editButton);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(deleteButton);
        
        // Make sure the buttons stretch horizontally
        viewButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        addButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        searchButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        editButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        deleteButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Wrap in another panel to center the stack
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(SECONDARY_COLOR);
        wrapperPanel.add(contentPanel, BorderLayout.CENTER);
        
        return wrapperPanel;
    }
    
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(PRIMARY_COLOR);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel footerLabel = new JLabel("© Museum Management System");
        footerLabel.setForeground(Color.WHITE);
        footerPanel.add(footerLabel);
        
        return footerPanel;
    }
    
    private JButton createMenuButton(String title, String tooltip) {
        JButton button = new JButton(title);
        button.setFont(MENU_FONT);
        button.setForeground(TEXT_COLOR);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200)), 
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        button.setToolTipText(tooltip);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(235, 235, 235));
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.WHITE);
            }
        });
        
        return button;
    }
    
    // // Main method for testing
    // public static void main(String[] args) {
    //     // Set the look and feel to system default
    //     try {
    //         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
        
    //     // Create the curator menu
    //     SwingUtilities.invokeLater(() -> new CuratorMenu());
    // }
}