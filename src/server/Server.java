/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import gui.PanelRegistry;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author geoma48p
 */
public class Server {

   public static void main(String[] args) throws IOException {

//      String compName = System.getenv("COMPUTERNAME");
      String compName = "SB303-23";
      String[] nameBits = compName.split("-");
      final String lab = nameBits[0];

      JFrame frame = new JFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      JPanel panel = new PanelRegistry().getPanel(lab);

      frame.add(panel);
      frame.pack();
      frame.setVisible(true);

      new ApplicationHandler(panel).start();
      new BroadcastResponder(lab).start();
   }
}
