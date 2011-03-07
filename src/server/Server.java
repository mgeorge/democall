/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import discovery.BroadcastResponder;
import gui.Lab;
import gui.maps.AbstractMapPanel;
import gui.processors.LabelProcessor;
import gui.LabRegistry;
import gui.processors.ServerLabelProcessor;
import java.io.IOException;
import javax.swing.JFrame;

/**
 *
 * @author geoma48p
 */
public class Server {

   public static void main(String[] args) throws IOException {

      String compName = System.getenv("COMPUTERNAME");

      String[] nameBits = compName.split("-");
      final String labName = nameBits[0];

      JFrame frame = new JFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      LabRegistry registry = new LabRegistry();
      Lab lab = registry.getLab(labName);
      AbstractMapPanel panel = lab.getPanel();

      LabelProcessor processor = new ServerLabelProcessor();
      processor.processLabels(panel);

      frame.setTitle(String.format("Democall 3 - Server (%1s)",lab.getLabDescription()));
      frame.add(panel);
      frame.pack();
      frame.setVisible(true);

      new ApplicationHandler(processor).start();
      new BroadcastResponder(labName).start();
   }
}
