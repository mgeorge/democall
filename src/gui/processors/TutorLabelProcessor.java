package gui.processors;

import clients.StudentClient;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import network.MessageGenerator;
import network.RequestSender;

/**
 *
 * @author Mark
 */
public class TutorLabelProcessor extends LabelProcessor {

   private static final Logger LOG = Logger.getLogger(StudentClient.class.getName());
   
   private final String ip;

   public TutorLabelProcessor(String ip) {
      this.ip = ip;
   }

   @Override
   public MouseAdapter getMouseAdapter() {
      return new MouseAdapter() {

         @Override
         public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            JLabel label = (JLabel) e.getSource();
            String machine = label.getText();

            // send cancel request to server
            cancelRequest(machine);

            // cancel locally so change happens immediately
            cancel(new Integer(machine));
         }
      };
   }

   private void cancelRequest(String machine) {
      try {
         new RequestSender(ip).sendRequest(new MessageGenerator().cancelRequest(machine));
      } catch (Exception ex) {
         LOG.log(Level.SEVERE, "Error cancelling request", ex);
      }
   }

   public void update(Collection<Integer> queue) {
      clear();

      for (Integer machine : new ArrayList<Integer>(queue)) {
         request(machine);
      }
   }

   private void clear() {
      for (Integer machine : new ArrayList<Integer>(queue)) {
         cancel(machine);
      }
   }   
   
}
