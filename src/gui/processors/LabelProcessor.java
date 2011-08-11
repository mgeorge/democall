/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.processors;

import gui.QueuePanel;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author geoma48p
 */
public abstract class LabelProcessor {

   private Color requestCol = Color.GREEN;
   private Color regularCol = new JLabel().getBackground();
   private Map<Integer, JLabel> labels = new HashMap<Integer, JLabel>();
   private final Set<Integer> queue = new LinkedHashSet<Integer>();

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

   public synchronized void request(final int id) {

      queue.add(id);

      final String queueStr = queue.toString();

      final JLabel label = labels.get(id);

      EventQueue.invokeLater(new Runnable() {

         public void run() {
            label.setBackground(requestCol);
            QueuePanel.getQueueLabel().setText(queueStr.substring(1, queueStr.length() - 1));
         }
      });

   }

   public synchronized void cancel(int id) {
      queue.remove(id);

      String queueStr = queue.toString();

      QueuePanel.getQueueLabel().setText(queueStr.substring(1, queueStr.length() - 1));

      final JLabel label = labels.get(id);

      EventQueue.invokeLater(new Runnable() {

         public void run() {
            label.setBackground(regularCol);
         }
      });
   }

   public Set<Integer> getQueue() {
      return queue;
   }

   void clear() {
      // copy the queue so we don't get ConcurrentModificationExceptions in this iterator
      Set<Integer> copy = new LinkedHashSet<Integer>(queue);
      for (Integer machine : copy) {
         cancel(machine);
      }
   }
}
