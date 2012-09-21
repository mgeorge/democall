package clients;

import constants.Constants;
import discovery.computername.ComputerNameResolver;
import discovery.computername.InvalidComputerNameException;
import discovery.computername.OtagoComputerNameResolver;
import discovery.server.ServiceLocator;
import discovery.server.ServiceNotFoundException;
import gui.Lab;
import gui.LabRegistry;
import gui.QueuePanel;
import gui.processors.TutorLabelProcessor;
import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import network.RequestSender;

/**
 *
 * @author Mark
 */
public class TutorClient {

   private static final Logger LOG = Logger.getLogger(TutorClient.class.getName());
   
   private String serverIp;

   @SuppressWarnings("rawtypes")
   public TutorClient(final ComputerNameResolver nameResolver) {

      JFrame frame = null;
      
      try {
         nameResolver.resolve();
         
         String labName = nameResolver.getLabName();

         final LabRegistry registry = new LabRegistry();
         final Lab[] labs = registry.getLabs().toArray(new Lab[registry.getLabs().size()]);
         final Lab currentLab = registry.getLab(labName);
         
         final Lab lab = (Lab) JOptionPane.showInputDialog(null, "Which lab?", "Which lab?",  JOptionPane.QUESTION_MESSAGE, null, labs, currentLab);
         
         if (lab == null) {
            System.exit(0);
         } else {
            labName = lab.getLabName();
         }

         serverIp = new ServiceLocator().locateServer(labName);
         frame = new JFrame();
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setAlwaysOnTop(true);
         final JPanel mapPanel = lab.getPanel();
         final TutorLabelProcessor processor = new TutorLabelProcessor(serverIp);
         processor.processLabels(mapPanel);
         frame.setTitle(String.format("Democall %1s - Tutor Client (%1s)", Constants.VERSION, lab.getLabDescription()));
         frame.setLayout(new BorderLayout());
         frame.add(BorderLayout.NORTH, new QueuePanel());
         frame.add(BorderLayout.CENTER, mapPanel);
         frame.pack();
         frame.setVisible(true);

         final Timer timer = new Timer(true);
         timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
               final Collection<Integer> queue = new RequestSender(serverIp).requestQueue();
               processor.update(queue);
            }
         }, 0, Constants.TUTOR_CLIENT_POLL);
         
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
         

      } catch (InvalidComputerNameException ex) {
         JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error getting computer name.", JOptionPane.ERROR_MESSAGE);
      } catch (ServiceNotFoundException ex) {
         JOptionPane.showMessageDialog(frame, "Could not connect to server.", "Connection error", JOptionPane.ERROR_MESSAGE);
      } catch (Exception ex) {
         LOG.log(Level.SEVERE, null, ex);
      }
   }

   @SuppressWarnings("ResultOfObjectAllocationIgnored")
   public static void main(final String[] args) {

//      final String name = "SBEASTCAL1-31";      

      final String name = args.length > 0 ? args[0] : null;
      
      final ComputerNameResolver nameResolver = new OtagoComputerNameResolver(name, "COMPUTERNAME");

      new TutorClient(nameResolver);
   }

}
