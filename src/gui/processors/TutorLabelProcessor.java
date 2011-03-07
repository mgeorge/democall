package gui.processors;

import clients.StudentClient;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import network.MessageGenerator;
import network.RequestSender;

/**
 *
 * @author mark
 */
public class TutorLabelProcessor extends LabelProcessor {

   private final String ip;

   public TutorLabelProcessor(String ip) {
      this.ip = ip;
   }

   @Override
   public MouseAdapter getMouseAdapter() {
      return new MouseAdapter() {

         @Override
         public void mouseClicked(MouseEvent e) {
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
         Logger.getLogger(StudentClient.class.getName()).log(Level.SEVERE, "Error cancelling request", ex);
      }
   }

   public void update(Set<Integer> queue) {
      clear();

      // copy the queue so we don't get ConcurrentModificationExceptions in this iterator
      Set<Integer> copy = new LinkedHashSet<Integer>(queue);

      for (Integer machine : copy) {
         request(machine);
      }
   }

}
