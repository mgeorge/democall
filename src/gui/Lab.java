package gui;

import gui.maps.AbstractMapPanel;

/**
 *
 * @author mark
 */
public class Lab implements Comparable<Lab>{

   private String labName;
   private String labDescription;
   private AbstractMapPanel panel;

   public Lab(String labName, String labDescription, AbstractMapPanel panel) {
      this.labName = labName;
      this.labDescription = labDescription;
      this.panel = panel;
   }

   public String getLabName() {
      return labName;
   }

   public AbstractMapPanel getPanel() {
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