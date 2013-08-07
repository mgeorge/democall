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
import java.awt.Font;
import java.awt.event.*;
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

//      final String name = "SBEASTCAL1-01";          
//            final String name = "SB318-1";          
//            final String name = "SB328-1";          

      
      final String name = args.length > 0 ? args[0] : null;
      
      final ComputerNameResolver nameResolver = new OtagoComputerNameResolver(name, "COMPUTERNAME");
      
      nameResolver.resolve();
      
      final String labName = nameResolver.getLabName();

      final JFrame frame = new JFrame();
      frame.setLayout(new BorderLayout());
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      final LabRegistry registry = new LabRegistry();
      final Lab lab = registry.getLab(labName);

      if (lab == null) {
          JOptionPane.showMessageDialog(frame, "There is no map for this lab yet.\nThe lab ID is : " + labName, "No map", JOptionPane.ERROR_MESSAGE);
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

      
      // dynamic font resize based on window size
      
      final int defaultSize = Constants.DEFAULT_MAP_FONT_SIZE;
      final int defaultWidth = mapPanel.getWidth();
      
      frame.addComponentListener(new ComponentAdapter() {
         
         public void componentResized(ComponentEvent e) {
            int newWidth = mapPanel.getWidth();
            float scaleFactor = (float)newWidth / (float)defaultWidth;
            int newFontSize = Math.round(defaultSize * scaleFactor);
            processor.resizeFonts(newFontSize);
         }

      });
      

      new ApplicationHandler(processor).start();
      new BroadcastResponder(labName).start();
   }

}
