package server;

import constants.Constants;
import discovery.BroadcastResponder;
import discovery.ComputerNameResolver;
import gui.Lab;
import gui.LabRegistry;
import gui.QueuePanel;
import gui.processors.LabelProcessor;
import gui.processors.ServerLabelProcessor;
import java.awt.BorderLayout;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Mark
 */
public class Server {

   private Server() {
   }   
   
   public static void main(String[] args) throws IOException {

      String compName = ComputerNameResolver.getName();

      if(args.length > 0) {
         compName = args[0];
      }

      String[] nameBits = compName.split("-");
      final String labName = nameBits[0];

      JFrame frame = new JFrame();
      frame.setLayout(new BorderLayout());
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      LabRegistry registry = new LabRegistry();
      Lab lab = registry.getLab(labName);

      if(lab == null) {
          JOptionPane.showMessageDialog(frame, "There is no map for this lab yet.", "No map", JOptionPane.ERROR_MESSAGE);
          System.exit(Constants.EXIT_NO_MAP_FOUND);
      }

      JPanel mapPanel = lab.getPanel();

      LabelProcessor processor = new ServerLabelProcessor();
      processor.processLabels(mapPanel);

      frame.setTitle(String.format("Democall %1s - Server (%1s)",Constants.VERSION, lab.getLabDescription()));
      frame.add(BorderLayout.NORTH, new QueuePanel());
      frame.add(BorderLayout.CENTER, mapPanel);
      frame.pack();
      frame.setVisible(true);

      new ApplicationHandler(processor).start();
      new BroadcastResponder(labName).start();
   }

}
