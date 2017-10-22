package tanks;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import tanks.Main;

public class ControlWindow extends JFrame {

   public Container c = new Container();
   public JButton start;
   public JButton stop;
   public JList players;
   public DefaultListModel listModel;


   public ControlWindow() {
      super("TanksServer v0.1 Mishin870");
      this.setBounds(300, 300, 300, 325);
      this.setResizable(false);
      this.setDefaultCloseOperation(0);
      this.start = new JButton("Старт");
      this.start.addActionListener(new ControlWindow.ButtonListener(this, "start"));
      this.start.setBounds(10, 10, 270, 30);
      this.c.add(this.start);
      this.stop = new JButton("Стоп");
      this.stop.addActionListener(new ControlWindow.ButtonListener(this, "stop"));
      this.stop.setBounds(10, 40, 270, 30);
      this.c.add(this.stop);
      this.listModel = new DefaultListModel();
      this.players = new JList(this.listModel);
      this.players.setBounds(10, 80, 270, 200);
      this.c.add(this.players);
      this.add(this.c);
      this.addWindowListener(new WindowListener() {
         public void windowClosing(WindowEvent var1) {
            Main.stopServer();
            System.exit(0);
         }
         public void windowClosed(WindowEvent var1) {}
         public void windowOpened(WindowEvent var1) {}
         public void windowDeactivated(WindowEvent var1) {}
         public void windowActivated(WindowEvent var1) {}
         public void windowDeiconified(WindowEvent var1) {}
         public void windowIconified(WindowEvent var1) {}
      });
   }

   public void showMessage(String var1, String var2, int var3) {
      JOptionPane.showMessageDialog((Component)null, var1, var2, var3);
   }

   public void showMessage(String var1, String var2) {
      JOptionPane.showMessageDialog((Component)null, var1, var2, 1);
   }

   class ButtonListener implements ActionListener {

      private String type;
      private ControlWindow cw;


      public ButtonListener(ControlWindow var2, String var3) {
         this.type = var3;
         this.cw = this.cw;
      }

      public void actionPerformed(ActionEvent var1) {
         if(this.type.equals("start")) {
            Main.startServer();
         }

         if(this.type.equals("stop")) {
            Main.stopServer();
         }

      }
   }
}
