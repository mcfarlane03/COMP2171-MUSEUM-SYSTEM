import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.awt.event.ActionListener;



public class InventoryManagement extends JFrame {
    private JPanel dispanel = new JPanel();
    private JPanel cmdpanel = new JPanel();
    private static final String FIRST_STARTUP_KEY = "firstStartup";

    public InventoryManagement() {
        // welcome/spalsh screen message:
        setTitle("Inventory Management System");

        // shows this message on first startup only:
        Preferences prefs = Preferences.userRoot();
        boolean firstStartup = prefs.getBoolean(FIRST_STARTUP_KEY, true);
        if (firstStartup) {
            JOptionPane.showMessageDialog(null,
                    "Welcome to the National Museum of Jamaica's Inventory Management System");
            prefs.putBoolean(FIRST_STARTUP_KEY, false);
            try {
                prefs.flush();
            } catch (BackingStoreException e) {
                e.printStackTrace();
            }
        }

        // frame settings:
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        // JLabel that provides prompt to user:
        dispanel.add(new JLabel("Select the login button below to use the system.\n"));
        dispanel.add(new JLabel("Enter your ID and password"));

        // create control buttons:
        JButton LoginButton = new JButton("LOGIN");
        JButton close = new JButton("X");
        close.setForeground(Color.RED);

        // button listeners:
        LoginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new EmployeeLogin();
            }
        });

        close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        LoginButton.setOpaque(true);
        LoginButton.setContentAreaFilled(true);
        LoginButton.setBorderPainted(false);
        LoginButton.setFocusPainted(false);
        LoginButton.setBackground(Color.darkGray); // for the background
        LoginButton.setForeground(Color.white); // for the text

        close.setOpaque(true);
        close.setContentAreaFilled(true);
        close.setBorderPainted(false);
        close.setFocusPainted(false);
        close.setBackground(Color.darkGray); // for the background
        close.setForeground(Color.RED); // for the text

        // add control components to cmdpanel:
        cmdpanel.add(LoginButton);
        // cmdpanel.add(studentButton);
        cmdpanel.add(close); // , FlowLayout.RIGHT

        // adding cmdpanel and dispanel to the frame:
        add(dispanel, BorderLayout.CENTER);
        add(cmdpanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    public static void main(String[] args) {
        new InventoryManagement();
    }
}