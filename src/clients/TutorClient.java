package clients;

import constants.Constants;
import discovery.ServiceLocator;
import gui.Lab;
import gui.LabRegistry;
import gui.QueuePanel;
import gui.processors.TutorLabelProcessor;
import java.awt.BorderLayout;
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

   private String serverIp;

   @SuppressWarnings("rawtypes")
   public TutorClient(String compName) {

      try {
         String[] nameBits = compName.split("-");
         String labName = nameBits[0];

         LabRegistry registry = new LabRegistry();
         Object[] labs = registry.getLabs().toArray();
         Lab currentLab = registry.getLab(labName);

         Lab lab = (Lab) JOptionPane.showInputDialog(null, "Which lab?", "Which lab?",  JOptionPane.QUESTION_MESSAGE, null, labs, currentLab);
         if(lab==null) {
            JOptionPane.showMessageDialog(null, "There is no map for this lab yet.", "No map", JOptionPane.ERROR_MESSAGE);
            System.exit(Constants.EXIT_NO_MAP_FOUND);
         } else {
            labName = lab.getLabName();
         }

         serverIp = new ServiceLocator().locateServer(labName);
         JFrame frame = new JFrame();
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setAlwaysOnTop( true );
         JPanel mapPanel = lab.getPanel();
         final TutorLabelProcessor processor = new TutorLabelProcessor(serverIp);
         processor.processLabels(mapPanel);
         frame.setTitle(String.format("Democall %1s - Tutor Client (%1s)", Constants.VERSION, lab.getLabDescription()));
         frame.setLayout(new BorderLayout());
         frame.add(BorderLayout.NORTH, new QueuePanel());
         frame.add(BorderLayout.CENTER, mapPanel);
         frame.pack();
         frame.setVisible(true);

         Timer timer = new Timer(true);
         timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
               Collection<Integer> queue = new RequestSender(serverIp).requestQueue();
               processor.update(queue);
            }
         }, 0, Constants.TUTOR_CLIENT_POLL);

      } catch (Exception ex) {
         Logger.getLogger(TutorClient.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   @SuppressWarnings("ResultOfObjectAllocationIgnored")
   public static void main(String[] args) {
//      String compName = System.getenv("COMPUTERNAME");
      String compName = "SBEASTCAL1-30";      

      if(args.length > 0) {
         compName = args[0];
      }

      new TutorClient(compName);
   }
}
