package clients;

import discovery.ServiceLocator;
import gui.Lab;
import gui.maps.AbstractMapPanel;
import gui.LabRegistry;
import gui.processors.TutorLabelProcessor;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import network.RequestSender;

/**
 *
 * @author mark
 */
public class TutorClient {

   private String serverIp;

   @SuppressWarnings("rawtypes")
   public TutorClient() {



      try {
         String compName = System.getenv("COMPUTERNAME");
         String[] nameBits = compName.split("-");
         String labName = nameBits[0];

         LabRegistry registry = new LabRegistry();
         Object[] labs = registry.getLabs().toArray();
         Lab currentLab = registry.getLab(labName);

         Lab lab = (Lab) JOptionPane.showInputDialog(null, "Which lab?", "Which lab?",  JOptionPane.QUESTION_MESSAGE, null, labs, currentLab);
         if(lab==null) {
            System.exit(0);
         } else {
            labName = lab.getLabName();
         }

         serverIp = new ServiceLocator().locateServer(labName);
         JFrame frame = new JFrame();
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         AbstractMapPanel panel = lab.getPanel();
         final TutorLabelProcessor processor = new TutorLabelProcessor(serverIp);
         processor.processLabels(panel);
         frame.setTitle(String.format("Democall 3 - Tutor Client (%1s)", lab.getLabDescription()));
         frame.add(panel);
         frame.pack();
         frame.setVisible(true);

         Timer timer = new Timer(true);
         timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
               Set<Integer> queue = new RequestSender(serverIp).requestQueue();
               processor.update(queue);
            }
         }, 0, 5000);

      } catch (Exception ex) {
         Logger.getLogger(TutorClient.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   @SuppressWarnings("ResultOfObjectAllocationIgnored")
   public static void main(String[] args) {
      new TutorClient();
   }
}
