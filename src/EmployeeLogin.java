import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

public class EmployeeLogin extends JFrame {
    private JPanel mainPanel = new JPanel();
    private JPanel buttonPanel = new JPanel();
    
    private JTextField idField = new JTextField(15);
    private JPasswordField passwordField = new JPasswordField(15);
    
    private JButton loginButton;
    private JButton registerButton;
    private JButton backButton;
    
    private static final String HASH_ALGORITHM = "SHA-256";
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    
    public static boolean manager_flag = false;

    private EmployeeDAO employeeDAO = new EmployeeDAO();
    
    public EmployeeLogin() {   
        setTitle("Employee Login");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        mainPanel.setLayout(new GridLayout(3, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Add components to the main panel
        mainPanel.add(new JLabel("Employee ID:"));
        mainPanel.add(idField);
        
        mainPanel.add(new JLabel("Password:"));
        mainPanel.add(passwordField);
        
        // Create buttons
        backButton = new JButton("<--");
        loginButton = new JButton("Login");
        registerButton = new JButton("Register New Employee");
        
        // Style the buttons
        backButton.setOpaque(true);
        backButton.setContentAreaFilled(true);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setBackground(Color.darkGray); // for the background
        backButton.setForeground(Color.white); // for the text

        loginButton.setBackground(Color.darkGray);
        loginButton.setForeground(Color.white);
        loginButton.setOpaque(true);
        loginButton.setContentAreaFilled(true);
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
        
        registerButton.setBackground(Color.darkGray);
        registerButton.setForeground(Color.white);
        registerButton.setOpaque(true);
        registerButton.setContentAreaFilled(true);
        registerButton.setBorderPainted(false);
        registerButton.setFocusPainted(false);
        
        // Add buttons to button panel
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(backButton);
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        
        // Add action listeners
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new InventoryManagement();
                setVisible(false);
            }
        });
        
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attemptLogin();
            }
        });
        
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new AddEmployee();
            }
        });
        
        // Add panels to frame
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        setVisible(true);
    }
    
    private void attemptLogin() {
        try {
            String idText = idField.getText().trim();
            String password = new String(passwordField.getPassword());
            
            if (idText.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both ID and password");
                return;
            }
            
            int id = Integer.parseInt(idText);
            String passwordHash = bytesToHex(hashPassword(password));
            
            if (employeeDAO.verifyCredentials(id, passwordHash)) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                
                // Get the employee details
                BaseStaff employee = employeeDAO.getEmployeeById(id);
                
                // Navigate to the appropriate menu based on role
                setVisible(false);
                if (employee.isManager()) {
                    manager_flag = true;
                    new ManagerMenu();
                } else {
                    new CuratorMenu();
                }
                
            } else {
                JOptionPane.showMessageDialog(this, "Invalid ID or password");
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric ID");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            e.printStackTrace();
        }
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

    public static boolean managerFlag() {
        return manager_flag;
    }
}