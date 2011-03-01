/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import gui.MapPanel303;
import java.io.IOException;
import javax.swing.JFrame;

/**
 *
 * @author geoma48p
 */
public class Server {

   public static void main(String[] args) throws IOException {

//      String compName = System.getenv("COMPUTERNAME");
      String compName = "SB306-23";
      String[] nameBits = compName.split("-");
      final String lab = nameBits[0];

      JFrame frame = new JFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      MapPanel303 panel = new MapPanel303();
      frame.add(panel);
      frame.pack();
      frame.setVisible(true);

      new ApplicationHandler(panel).start();
      new BroadcastResponder(lab).start();
   }
}
