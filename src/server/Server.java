package server;

import constants.Constants;
import discovery.computername.ComputerNameResolver;
import discovery.computername.OtagoComputerNameResolver;
import discovery.server.BroadcastResponder;
import gui.Lab;
import gui.LabRegistry;
import gui.QueuePanel;
import gui.processors.AbstractLabelProcessor;
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
public final class Server {

   private Server() {
   }   
   
   public static void main(final String[] args) throws IOException {

      final String name = "SBEASTCAL1-01";          
      
//      final String name = args.length > 0 ? args[0] : null;
      
      final ComputerNameResolver nameResolver = new OtagoComputerNameResolver(name, "COMPUTERNAME");
      
      nameResolver.resolve();
      
      final String labName = nameResolver.getLabName();

      final JFrame frame = new JFrame();
      frame.setLayout(new BorderLayout());
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      final LabRegistry registry = new LabRegistry();
      final Lab lab = registry.getLab(labName);

      if (lab == null) {
          JOptionPane.showMessageDialog(frame, "There is no map for this lab yet.", "No map", JOptionPane.ERROR_MESSAGE);
          System.exit(Constants.EXIT_NO_MAP_FOUND);
      }

      final JPanel mapPanel = lab.getPanel();

      final AbstractLabelProcessor processor = new ServerLabelProcessor();
      processor.processLabels(mapPanel);

      frame.setTitle(String.format("Democall %1s - Server (%1s)", Constants.VERSION, lab.getLabDescription()));
      frame.add(BorderLayout.NORTH, new QueuePanel());
      frame.add(BorderLayout.CENTER, mapPanel);
      frame.pack();
      frame.setVisible(true);

      new ApplicationHandler(processor).start();
      new BroadcastResponder(labName).start();
   }

}
