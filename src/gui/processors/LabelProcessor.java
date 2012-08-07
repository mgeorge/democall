package gui.processors;

import gui.QueuePanel;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.util.*;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Mark
 */
public abstract class LabelProcessor {

   private final Color requestCol = Color.GREEN;
   private final Color regularCol = new JLabel().getBackground();
   private final Map<Integer, JLabel> labels = new HashMap<Integer, JLabel>();
//   protected final Collection<Integer> queue = new ConcurrentLinkedQueue<Integer> ();
   protected final Collection<Integer> queue = new LinkedHashSet<Integer>();

   public abstract MouseAdapter getMouseAdapter();

   public void processLabels(JPanel panel) {
      processPanel(panel);
   }

   private void processPanel(JPanel panel) {
      for (Component component : panel.getComponents()) {

         if (component instanceof JLabel) {
            JLabel label = (JLabel) component;
            try {
               // component must be opaque so the background color can be set
               label.setOpaque(true);
               Integer id = new Integer(label.getText());
               labels.put(id, label);

               label.addMouseListener(this.getMouseAdapter());

            } catch (NumberFormatException ignored) {
               // ignored
            }
         } else if (component instanceof JPanel) {
            // found a panel that might contain buttons so recurse
            processPanel((JPanel) component);
         }
      }
   }

   public void request(final int id) {
      synchronized (queue) {
         queue.add(id);

         final String queueStr = queue.toString();

         final JLabel label = labels.get(id);

         if (label != null) {
            label.setBackground(requestCol);
         }
         QueuePanel.getQueueLabel().setText(queueStr.substring(1, queueStr.length() - 1));
      }
   }

   public synchronized void cancel(int id) {
      synchronized (queue) {
         queue.remove(id);

         String queueStr = queue.toString();

         QueuePanel.getQueueLabel().setText(queueStr.substring(1, queueStr.length() - 1));

         final JLabel label = labels.get(id);

         if (label != null) {
            label.setBackground(regularCol);
         }
      }
   }

   public Collection<Integer> getQueue() {
      synchronized (queue) {
         // creating a copy of queue to prevent ConcurrentModification errors
         return new ArrayList(queue);
      }
   }
}
