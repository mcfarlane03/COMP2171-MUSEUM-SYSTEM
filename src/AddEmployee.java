import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

public class AddEmployee extends JFrame {
    private JPanel mainPanel = new JPanel();
    private JPanel formPanel = new JPanel();
    private JPanel genderPanel = new JPanel();
    private JPanel buttonPanel = new JPanel();
    
    private JTextField nameField = new JTextField(20);
    private JTextField idField = new JTextField(20);
    private JComboBox<String> roleComboBox;
    private JPasswordField passwordField = new JPasswordField(20);
    private JPasswordField confirmPasswordField = new JPasswordField(20);
    
    private ButtonGroup genderGroup = new ButtonGroup();
    private JRadioButton maleRadio = new JRadioButton("Male");
    private JRadioButton femaleRadio = new JRadioButton("Female");
    private JRadioButton otherRadio = new JRadioButton("Other");

    private JButton saveButton;
    private JButton cancelButton;

    private static final String HASH_ALGORITHM = "SHA-256";
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    
    // Add the EmployeeDAO
    private EmployeeDAO employeeDAO = new EmployeeDAO();

    public AddEmployee() {
        // Initialize the database when the application starts
        DatabaseConnection.initializeDatabase();
        
        setTitle("Create New Employee");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Set up the main panel with a border layout
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Set up the form panel with a grid layout
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), 
                "Employee Information",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14)
            ),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        // Create role dropdown instead of text field
        String[] roles = {"Manager", "Curator"};
        roleComboBox = new JComboBox<>(roles);
        roleComboBox.setPreferredSize(new Dimension(200, 25));
        
        // Set up the gender panel
        genderPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        genderPanel.setBorder(BorderFactory.createTitledBorder("Gender"));
        
        // Configure radio buttons and add to button group
        genderGroup.add(maleRadio);
        genderGroup.add(femaleRadio);
        genderGroup.add(otherRadio);
        
        // Style radio buttons
        maleRadio.setFont(new Font("Arial", Font.PLAIN, 12));
        femaleRadio.setFont(new Font("Arial", Font.PLAIN, 12));
        otherRadio.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // Add radio buttons to gender panel
        genderPanel.add(maleRadio);
        genderPanel.add(femaleRadio);
        genderPanel.add(otherRadio);
        
        // Set up GridBagConstraints for form layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Add form components using GridBagLayout
        
        // Name field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(nameLabel, gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(nameField, gbc);
        
        // ID field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel idLabel = new JLabel("Employee ID:");
        idLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(idLabel, gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(idField, gbc);
        
        // Role dropdown
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(roleLabel, gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(roleComboBox, gbc);
        
        // Gender panel
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(genderPanel, gbc);
        
        // Password field
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(passwordLabel, gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(passwordField, gbc);
        
        // Confirm Password field
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel confirmLabel = new JLabel("Confirm Password:");
        confirmLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(confirmLabel, gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(confirmPasswordField, gbc);
        
        // Set up button panel
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        // Create and style buttons
        saveButton = new JButton("Save");
        saveButton.setPreferredSize(new Dimension(100, 30));
        saveButton.setFont(new Font("Arial", Font.BOLD, 12));
        saveButton.setBackground(new Color(34, 139, 34)); // Forest green
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        
        cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(100, 30));
        cancelButton.setFont(new Font("Arial", Font.BOLD, 12));
        cancelButton.setBackground(new Color(178, 34, 34)); // Firebrick red
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        
        // Add buttons to panel
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        // Add panels to main panel
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add main panel to frame
        add(mainPanel);
        
        // Button listeners
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (validateInput()) {
                    saveEmployeeToDatabase();
                } else {
                    JOptionPane.showMessageDialog(
                        null, 
                        "Please fill all fields and select a gender.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new EmployeeLogin();
                dispose();
            }
        });
        
        pack();
        setVisible(true);
    }
    
    private boolean validateInput() {
        if (nameField.getText().trim().isEmpty() || 
            idField.getText().trim().isEmpty() || 
            passwordField.getPassword().length == 0 ||
            confirmPasswordField.getPassword().length == 0) {
            return false;
        }
        
        // Check if passwords match
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(
                this,
                "Passwords do not match!",
                "Password Error",
                JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
        
        // Check if a gender is selected
        if (!maleRadio.isSelected() && !femaleRadio.isSelected() && !otherRadio.isSelected()) {
            return false;
        }
        
        return true;
    }
    
    private void saveEmployeeToDatabase() {
        try {
            String fullName = nameField.getText().trim();
            String roleValue = (String) roleComboBox.getSelectedItem();
            int id = Integer.parseInt(idField.getText().trim());
            String password = new String(passwordField.getPassword());
            char gender = getSelectedGender();
            
            // Check if employee with this ID already exists
            if (employeeDAO.employeeExists(id)) {
                JOptionPane.showMessageDialog(
                    null, 
                    "An employee with this ID already exists!",
                    "Duplicate ID",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            
            // Hash the password
            String passwordHash = bytesToHex(hashPassword(password));
            
            // Create an Employee object
            BaseStaff employee = new BaseStaff(id, fullName, roleValue, gender, passwordHash);
            
            // Save to database
            boolean success = employeeDAO.addEmployee(employee);
            
            if (success) {
                JOptionPane.showMessageDialog(
                    null,
                    "Employee added successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
                setVisible(false);
                new EmployeeLogin();
                dispose();
            } else {
                JOptionPane.showMessageDialog(
                    null,
                    "Failed to add employee. Please try again.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                null,
                "Please enter a valid numeric ID.",
                "Input Error",
                JOptionPane.ERROR_MESSAGE
            );
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                null,
                "Error: " + ex.getMessage(),
                "System Error",
                JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
    }
    
    private char getSelectedGender() {
        if (maleRadio.isSelected()) return 'M';
        if (femaleRadio.isSelected()) return 'F';
        if (otherRadio.isSelected()) return 'O';
        return ' '; // Should never happen due to validation
    }

    private static byte[] hashPassword(String password) {
        Charset charset = StandardCharsets.UTF_8;
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(HASH_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] passwordBytes = password.getBytes(charset);
        return md.digest(passwordBytes);
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            hexChars[i * 2] = HEX_ARRAY[v >>> 4];
            hexChars[i * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}