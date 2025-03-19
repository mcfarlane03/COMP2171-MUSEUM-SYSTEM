


import javax.swing.*;
import java.awt.event.*;

class ManagerMenu extends JFrame {

   public ManagerMenu() {
      // JFrame frame = new JFrame();

      JMenuBar menuBar = new JMenuBar(); // create menu bar
      JMenu menu = new JMenu("Welcome to the Managers' Menu"); // create menu

      JMenuItem view = new JMenuItem("View Museum Artifact List");
      menu.add(view);


      JMenuItem add = new JMenuItem("Add a Curator");
      menu.add(add);

      JMenuItem logout = new JMenuItem("LOGOUT");
      menu.add(logout);

      menuBar.add(menu);
      setJMenuBar(menuBar);

      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setSize(400, 400);
      setVisible(true);

      view.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            new ViewArtifact();
            setVisible(false);
         }
      });


      add.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            new AddEmployee();
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