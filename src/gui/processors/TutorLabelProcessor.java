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
public class TutorLabelProcessor extends AbstractLabelProcessor {

   private static final Logger LOG = Logger.getLogger(StudentClient.class.getName());
   
   private final String ip;

   public TutorLabelProcessor(final String ip) {
      super();
      this.ip = ip;
   }

   @Override
   public final MouseAdapter getMouseAdapter() {
      return new MouseAdapter() {

         @Override
         public void mousePressed(final MouseEvent e) {
            super.mousePressed(e);
            final JLabel label = (JLabel) e.getSource();
            final String machine = label.getText();

            // send cancel request to server
            cancelRequest(machine);

            // cancel locally so change happens immediately
            cancel(Integer.valueOf(machine));
         }
      };
   }

   private final void cancelRequest(final String machine) {
      try {
         new RequestSender(ip).sendRequest(new MessageGenerator().cancelRequest(machine));
      } catch (Exception ex) {
         LOG.log(Level.SEVERE, "Error cancelling request", ex);
      }
   }

   public final void update(final Collection<Integer> queue) {
      clear();

      final Collection<Integer> q = new ArrayList<Integer>(queue);
      for (Integer machine : q) {
         request(machine);
      }
   }

   private final void clear() {
      final Collection<Integer> q = new ArrayList<Integer>(queue);
      for (Integer machine : q) {
         cancel(machine);
      }
   }   
   
}
