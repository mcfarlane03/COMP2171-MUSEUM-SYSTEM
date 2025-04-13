import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;
import java.net.URL;

class ManagerMenu extends JFrame {
    // Panels for layout organization
    private JPanel mainPanel;
    private JPanel headerPanel;
    private JPanel actionsPanel;
    private JPanel footerPanel;
    
    // UI Components
    private JLabel welcomeLabel;
    private JLabel titleLabel;
    private JButton viewArtifactsButton;
    private JButton addCuratorButton;
    private JButton logoutButton;
    
    // Constructor
    public ManagerMenu() {
        // Set up the frame
        setTitle("Museum Management System");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        
        // Initialize and configure the main panel
        mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 245, 250)); // Light blue-gray background
        
        // Set up the header panel
        setupHeaderPanel();
        
        // Set up the actions panel
        setupActionsPanel();
        
        // Set up the footer panel
        setupFooterPanel();
        
        // Add all panels to the main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(actionsPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        // Add the main panel to the frame
        add(mainPanel);
        
        // Make the frame visible
        setVisible(true);
    }
    
    private void setupHeaderPanel() {
        headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setBackground(new Color(41, 128, 185)); // Blue header
        headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Create welcome label
        welcomeLabel = new JLabel("Welcome, Museum Manager");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeLabel.setForeground(Color.WHITE);
        
        // Create title/subtitle label
        titleLabel = new JLabel("Access your management functions below");
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        titleLabel.setForeground(Color.WHITE);
        
        // Add labels to header panel
        JPanel labelPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        labelPanel.setOpaque(false);
        labelPanel.add(welcomeLabel);
        labelPanel.add(titleLabel);
        
        headerPanel.add(labelPanel, BorderLayout.CENTER);
        
        // Optional: Add a logo or icon if available
        try {
            // This is a placeholder - you'd replace with an actual image path
            ImageIcon icon = new ImageIcon(new ImageIcon("museum_icon.png").getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
            JLabel logoLabel = new JLabel(icon);
            headerPanel.add(logoLabel, BorderLayout.EAST);
        } catch (Exception e) {
            // If image loading fails, use a text placeholder instead
            JLabel logoText = new JLabel("🏛️");
            logoText.setFont(new Font("Arial", Font.PLAIN, 40));
            logoText.setForeground(Color.WHITE);
            headerPanel.add(logoText, BorderLayout.EAST);
        }
    }
    
    private void setupActionsPanel() {
        actionsPanel = new JPanel(new GridBagLayout());
        actionsPanel.setBackground(new Color(245, 245, 250));
        actionsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1.0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        
        // Create action buttons with consistent styling
        viewArtifactsButton = createActionButton("View Museum Artifact List", "View all artifacts in the museum collection");
        addCuratorButton = createActionButton("Add a Curator", "Register a new curator to manage artifacts");
        
        // Add buttons to the panel
        gbc.gridy = 0;
        actionsPanel.add(viewArtifactsButton, gbc);
        
        gbc.gridy = 1;
        actionsPanel.add(addCuratorButton, gbc);
        
        // Adding action listeners to buttons
        viewArtifactsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ViewArtifact();
                setVisible(false);
                dispose();
            }
        });
        
        addCuratorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new AddEmployee();
                setVisible(false);
                dispose();
            }
        });
    }
    
    private void setupFooterPanel() {
        footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(new Color(245, 245, 250));
        footerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Create logout button
        logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(new Color(231, 76, 60)); // Red color
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setPreferredSize(new Dimension(120, 40));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new EmployeeLogin();
                dispose();
            }
        });
        
        // Add system time/date or other footer information if needed
        JLabel copyrightLabel = new JLabel("© " + java.time.Year.now().getValue() + " Museum Management System");
        copyrightLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        copyrightLabel.setForeground(new Color(100, 100, 100));
        
        // Add components to footer
        footerPanel.add(copyrightLabel);
        footerPanel.add(Box.createHorizontalStrut(20)); // Add some spacing
        footerPanel.add(logoutButton);
    }
    
    private JButton createActionButton(String title, String description) {
        // Create a panel for the button with title and description
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Create title label
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(44, 62, 80)); // Dark blue text
        
        // Create description label
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descLabel.setForeground(new Color(100, 100, 100));
        
        // Add labels to panel
        JPanel textPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel);
        textPanel.add(descLabel);
        buttonPanel.add(textPanel, BorderLayout.CENTER);
        
        // Add arrow icon to indicate it's clickable
        JLabel arrowLabel = new JLabel("→");
        arrowLabel.setFont(new Font("Arial", Font.BOLD, 24));
        arrowLabel.setForeground(new Color(52, 152, 219)); // Blue arrow
        buttonPanel.add(arrowLabel, BorderLayout.EAST);
        
        // Create the actual button that contains this panel
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.add(buttonPanel);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(500, 100));
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                buttonPanel.setBackground(new Color(240, 248, 255)); // Light blue when hovering
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                buttonPanel.setBackground(Color.WHITE);
            }
        });
        
        return button;
    }
}