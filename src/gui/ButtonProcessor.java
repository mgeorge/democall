/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author geoma48p
 */
public class ButtonProcessor {

   private Color requestCol = Color.GREEN;
   private Color regularCol = new JLabel().getBackground();
   private Map<Integer, JLabel> labels = new HashMap<Integer, JLabel>();

   public void processButtons(JPanel panel) {

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
            processButtons((JPanel) component);
         }
      }
   }

   public void request(final int id) {
      System.out.println("Request " + id);

      final JLabel label = labels.get(id);
      System.out.println("label id " + label.getText());

      EventQueue.invokeLater(new Runnable() {

         public void run() {
            label.setBackground(requestCol);
         }
      });

   }

   public void cancel(int id) {
      System.out.println("Cancel " + id);

      final JLabel label = labels.get(id);
      System.out.println("label id " + label.getText());

      EventQueue.invokeLater(new Runnable() {

         public void run() {
            label.setBackground(regularCol);
         }
      });
   }
}
