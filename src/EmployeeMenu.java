


import javax.swing.*;
import java.awt.event.*;

class EmployeeMenu extends JFrame {

   public EmployeeMenu() {
      // JFrame frame = new JFrame();

      JMenuBar menuBar = new JMenuBar(); // create menu bar
      JMenu menu = new JMenu("Welcome to the Employee Menu"); // create menu

      JMenuItem View_A = new JMenuItem("View Museum Artifact List");
      menu.add(View_A);

      JMenuItem View_D = new JMenuItem("View Donor Contact List");
      menu.add(View_D);

      JMenuItem Add_A = new JMenuItem("Add an Artifact");
      menu.add(Add_A);

      JMenuItem Search = new JMenuItem("Search Artifacts");
      menu.add(Search);

      JMenuItem Add_D = new JMenuItem("Add Donor");
      menu.add(Add_D);

      JMenuItem Delete_A = new JMenuItem("Delete Artifacts");
      menu.add(Delete_A);

      JMenuItem Delete_D = new JMenuItem("Delete Donors");
      menu.add(Delete_D);

      JMenuItem logout = new JMenuItem("Log Out");
      menu.add(logout);

      menuBar.add(menu);
      setJMenuBar(menuBar);

      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setSize(400, 400);
      setVisible(true);

      View_A.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            new ViewArtifact();
            setVisible(false);
         }
      });

      View_D.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            new ViewContactList();
            setVisible(false);
         }
      });

      Add_A.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            new AddArtifactUI();
            setVisible(false);
         }
      });

      Search.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            new Search();
            setVisible(false);
         }
      });

      Add_D.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            new AddDonorUI();
            setVisible(false);
         }
      });

      Delete_A.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            new DeleteArtifact();
            setVisible(false);
         }
      });

      Delete_D.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            new DeleteDonor();
            setVisible(false);

         }
      });

      logout.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            setVisible(false);
            new EmployeeLogin();
         }
      });
   }
}