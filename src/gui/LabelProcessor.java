/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author geoma48p
 */
public class LabelProcessor {

   private Color requestCol = Color.GREEN;
   private Color regularCol = new JLabel().getBackground();
   private Map<Integer, JLabel> labels = new HashMap<Integer, JLabel>();
   private Set<Integer> queue = new LinkedHashSet<Integer>();
   private IMapPanel mapPanel;

   public void processLabels(IMapPanel panel) {
      this.mapPanel = panel;

      processPanel(panel.getPanel());

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
      queue.add(id);

      String queueStr = queue.toString();

      mapPanel.getQueueLabel().setText(queueStr.substring(1, queueStr.length()-1));

      final JLabel label = labels.get(id);
      EventQueue.invokeLater(new Runnable() {

         public void run() {
            label.setBackground(requestCol);
         }
      });

   }

   public void cancel(int id) {
      queue.remove(id);

      String queueStr = queue.toString();

      mapPanel.getQueueLabel().setText(queueStr.substring(1, queueStr.length()-1));

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
}
