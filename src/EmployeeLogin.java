import java.awt.*;
import java.io.File;
import javax.swing.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.awt.event.ActionEvent;
import java.security.MessageDigest;
import java.awt.event.ActionListener;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;



public class EmployeeLogin extends JFrame {
    private JPanel dpanel = new JPanel(new GridLayout(3, 1));
    private JPanel bpanel = new JPanel();
    private JButton login;
    private JButton create;
    private JButton back;

    private JTextField ID = new JTextField(15);
    private JPasswordField pwd = new JPasswordField(15);

    private static final String HASH_ALGORITHM = "SHA-256";
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    // "global manager_flag" variable:
    public static boolean manager_flag = false;

    public EmployeeLogin() {
        setLocationRelativeTo(null);
        setTitle("Museum Employee Login");
        dpanel.add(new JLabel("Employee ID: "));
        dpanel.add(ID);

        dpanel.add(new JLabel("Password: "));
        dpanel.add(pwd);

        // action buttons:
        back = new JButton("<--");
        login = new JButton("Login");
        create = new JButton("Create User");

        // adding buttons to panel:
        back.setOpaque(true);
        back.setContentAreaFilled(true);
        back.setBorderPainted(false);
        back.setFocusPainted(false);
        back.setBackground(Color.darkGray); // for the background
        back.setForeground(Color.white); // for the text

        login.setOpaque(true);
        login.setContentAreaFilled(true);
        login.setBorderPainted(false);
        login.setFocusPainted(false);
        login.setBackground(Color.darkGray); // for the background
        login.setForeground(Color.white); // for the text

        create.setOpaque(true);
        create.setContentAreaFilled(true);
        create.setBorderPainted(false);
        create.setFocusPainted(false);
        create.setBackground(Color.darkGray); // for the background
        create.setForeground(Color.white); // for the text

        bpanel.setLayout(new FlowLayout());
        bpanel.add(back);
        bpanel.add(login);
        bpanel.add(create);

        // button listeners:
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new InventoryManagement();
                setVisible(false);
            }
        });

        login.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // handles case where username and password field is empty. will give an error
                // message to user:
                if ((!ID.getText().isEmpty()) && pwd.getPassword().length > 0) {
                    int Id = Integer.parseInt(ID.getText());
                    String password = new String(pwd.getPassword());
                    byte[] userHash = hashPassword(password);
                    String userHashHex = bytesToHex(userHash);
                    int flag = -1;
                    String line;
                    try {
                        // Create a File object with the given file name:
                        File file = new File("employee_details.txt");

                        // Check if the file exists:
                        if (!file.exists())
                            JOptionPane.showMessageDialog(null,
                                    "No user account has been created yet. Please create a user");

                        // Create a FileReader object to read from the file:
                        FileReader fileReader = new FileReader(file);

                        // Create a BufferedReader object to read text from the FileReader:
                        BufferedReader bufferedReader = new BufferedReader(fileReader);

                        // loops through file
                        while ((line = bufferedReader.readLine()) != null) {
                            String[] loginStr = line.split(" ");
                            int check = Integer.parseInt(loginStr[3]);
                            // Compares fields from text to fields from JText & PasswordFields:
                            if (loginStr[1].equals("Manager") && check == Id && loginStr[4].equals(userHashHex)) {
                                new ManagerMenu();
                                JOptionPane.showMessageDialog(null, "User with ID#: [" + loginStr[3] + "] logged in");
                                // System.out.println("SUCCESSFUL MANAGER");
                                setVisible(false);
                                bufferedReader.close();
                                flag = 0;
                                manager_flag = true;
                            }
                            if (loginStr[1].equals("Entry_Level_Employee") && check == Id && loginStr[4].equals(userHashHex)) {
                                new EmployeeMenu();
                                // System.out.println("SUCCESSFUL EMPLOYEE");
                                JOptionPane.showMessageDialog(null, "User with ID#: [" + loginStr[3] + "] logged in");
                                setVisible(false);
                                bufferedReader.close();
                                flag = 0;
                            }
                        }
                        // handles case where uname or pwd is wrong:
                        if (flag == -1)
                            JOptionPane.showMessageDialog(null, "Incorrect username or password. Please try again!");
                        bufferedReader.close();
                        fileReader.close();
                    } catch (IOException IOE) {
                    }
                    // catch (InterruptedException IE) {IE.printStackTrace();}
                }
                // error message for null input:
                else
                    JOptionPane.showMessageDialog(null, "Null input detected. Please enter valid characters.");
            }
        });

        create.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new AddEmployee();
                setVisible(false);
            }
        });

        // adding panels to frame:
        add(dpanel, BorderLayout.NORTH);
        add(bpanel, BorderLayout.SOUTH);
        pack();
        setVisible(true);
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