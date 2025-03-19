import java.awt.*;
import java.io.File;
import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.awt.event.ActionEvent;
import java.security.MessageDigest;
import java.awt.event.ActionListener;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

public class AddEmployee extends JFrame {
    private JPanel dispanel = new JPanel();
    private JPanel gendPanel = new JPanel();
    private JPanel companel = new JPanel();
    private JButton cancel;
    private JButton save;

    private JTextField name = new JTextField(15);
    private JTextField ID = new JTextField(15);
    private JTextField role = new JTextField(15);
    private JPasswordField pwd = new JPasswordField(15);
    private JCheckBox male = new JCheckBox();
    private JCheckBox female = new JCheckBox();
    private JCheckBox other = new JCheckBox();

    private static final String HASH_ALGORITHM = "SHA-256";
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public AddEmployee() {
        setTitle("Create an employee");
        // dispanel.setLayout(new BoxLayout(dispanel, BoxLayout.Y_AXIS));
        dispanel.setLayout(new GridLayout(6, 5));
        setSize(400, 400);
        setLocationRelativeTo(null);

        // start of display panel:
        dispanel.add(new JLabel("Enter your name: "));
        dispanel.add(name);

        dispanel.add(new JLabel("Enter your ID#: "));
        dispanel.add(ID);

        dispanel.add(new JLabel("Enter your role [Manager/Entry Level Employee]: "));
        dispanel.add(role);

        // start of gender panel
        gendPanel.add(new JLabel("Select your gender: |"));
        gendPanel.add(new JLabel("Male: "));
        gendPanel.add(male);
        gendPanel.add(new JLabel("Female: "));
        gendPanel.add(female);
        gendPanel.add(new JLabel("Other: "));
        gendPanel.add(other);

        dispanel.add(gendPanel);

        dispanel.add(new JLabel("Create a secure password: "));
        dispanel.add(pwd);

        // action buttons:
        save = new JButton("save");
        cancel = new JButton("cancel");

        save.setOpaque(true);
        save.setContentAreaFilled(true);
        save.setBorderPainted(false);
        save.setFocusPainted(false);
        save.setBackground(Color.darkGray); // for the background
        save.setForeground(Color.white); // for the text

        cancel.setOpaque(true);
        cancel.setContentAreaFilled(true);
        cancel.setBorderPainted(false);
        cancel.setFocusPainted(false);
        cancel.setBackground(Color.darkGray); // for the background
        cancel.setForeground(Color.white); // for the text

        // adding buttons to panel:
        companel.setLayout(new FlowLayout());
        // dispanel.setLayout(BorderLayout);
        companel.add(save);
        companel.add(cancel);

        // button listeners:
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // handles case where username and password field is empty. will give an error
                // message to user:
                if (!name.getText().isEmpty() && pwd.getPassword().length > 0 && (!ID.getText().isEmpty())) {
                    if (male.isSelected() || female.isSelected() || other.isSelected()) {
                        String name_ = name.getText().trim();
                        String role_ = role.getText().trim();
                        int Id = Integer.parseInt(ID.getText());
                        String password = new String(pwd.getPassword());
                        String gender = "";
                        byte[] userHash = hashPassword(password);
                        String userHashHex = bytesToHex(userHash);
                        try {
                            // Creates a file named admin.txt
                            File file = new File("employee_details.txt");

                            // Create a FileWriter object to write to the file:
                            FileWriter fileWriter = new FileWriter(file, file.exists());

                            // Create a BufferedWriter object to write text to the FileWriter
                            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                            if (male.isSelected())
                                gender = "M";

                            if (female.isSelected())
                                gender = "F";

                            if (other.isSelected())
                                gender = "O";

                            // Handles writing name in format: first_last
                            String[] names = name_.split(" ");

                            // Gets username and computed hashed password and stores in String text
                            String text = names[0] + "_" + names[1] + " " + role_ + " " + gender + " " + Id + " "
                                    + userHashHex;

                            // Write the text to the file
                            bufferedWriter.write(text);

                            if (file.exists())
                                bufferedWriter.newLine();

                            // Close the BufferedWriter and FileWriter
                            bufferedWriter.close();
                            fileWriter.close();

                            setVisible(false);
                            JOptionPane.showMessageDialog(null, "Addition successful!");
                            new EmployeeLogin();
                        } catch (IOException IOE) {
                        }
                    }
                }
                // error message for null input:
                else
                    JOptionPane.showMessageDialog(null, "Null input detected. Please enter valid characters.");
            }
        });

        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new EmployeeLogin();
            }
        });

        // adding panels to frame:
        add(dispanel, BorderLayout.NORTH);
        add(companel, BorderLayout.SOUTH);
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
}