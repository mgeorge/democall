package gui;

import javax.swing.JPanel;

/**
 *
 * @author mark
 */
public class Lab implements Comparable<Lab>{

   private String labName;
   private String labDescription;
   private JPanel panel;

   public Lab(String labName, String labDescription, JPanel panel) {
      this.labName = labName;
      this.labDescription = labDescription;
      this.panel = panel;
   }

   public String getLabName() {
      return labName;
   }

   public JPanel getPanel() {
      return panel;
   }

   public String getLabDescription() {
      return labDescription;
   }

   @Override
   public String toString() {
      return labDescription;
   }

   public int compareTo(Lab otherLab) {
      return this.getLabDescription().compareTo(otherLab.getLabDescription());
   }

}