package gui.processors;

import gui.QueuePanel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Mark
 */
public abstract class AbstractLabelProcessor {

   private final Color requestCol = Color.GREEN;
   private final Color regularCol = new JLabel().getBackground();
   private final Map<Integer, JLabel> labels = new ConcurrentHashMap<Integer, JLabel>();
//   protected final Collection<Integer> queue = new ConcurrentLinkedQueue<Integer> ();
   protected final Collection<Integer> queue = new LinkedHashSet<Integer>();

   public abstract MouseAdapter getMouseAdapter();

   public final void processLabels(final JPanel panel) {
      processPanel(panel);
   }

   private void processPanel(final JPanel panel) {
      for (Component component : panel.getComponents()) {

         if (component instanceof JLabel) {
            final JLabel label = (JLabel) component;
            try {
               // component must be opaque so the background color can be set
               label.setOpaque(true);
               final Integer id = Integer.valueOf(label.getText());
               labels.put(id, label);

               label.addMouseListener(this.getMouseAdapter());

            } catch (NumberFormatException ignored) {
               continue;
            }
         } else if (component instanceof JPanel) {
            // found a panel that might contain labels so recurse
            processPanel((JPanel) component);
         }
      }
   }

   public void resizeFonts(float newFontSize) {
      // there should always labels in this map so hopefully this next line doesn't cause issues
      Font newFont = labels.values().iterator().next().getFont().deriveFont(newFontSize);
      
      for (JLabel label : labels.values()) {
         label.setFont(newFont);
      }
   }   
   
   public final void request(final int id) {
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

   public final synchronized void cancel(final int id) {
      synchronized (queue) {
         queue.remove(id);

         final String queueStr = queue.toString();

         QueuePanel.getQueueLabel().setText(queueStr.substring(1, queueStr.length() - 1));

         final JLabel label = labels.get(id);

         if (label != null) {
            label.setBackground(regularCol);
         }
      }
   }

   public final Collection<Integer> getQueue() {
      synchronized (queue) {
         // Creating a copy of queue to prevent ConcurrentModification errors.
         // This only works because queue is locked by the sync block
         return new ArrayList<Integer>(queue);
      }
   }
}
