package gui;

import javax.swing.JPanel;

/**
 *
 * @author Mark
 */
public class Lab implements Comparable<Lab> {

   private final String labName;
   private final String labDescription;
   private final JPanel panel;

   public Lab(final String labName, final String labDescription, final JPanel panel) {
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

   public int compareTo(final Lab otherLab) {
      return this.getLabDescription().compareTo(otherLab.getLabDescription());
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == null) {
         return false;
      }
      if (getClass() != obj.getClass()) {
         return false;
      }
      final Lab other = (Lab) obj;
      if ((this.labName == null) ? (other.labName != null) : !this.labName.equals(other.labName)) {
         return false;
      }
      if ((this.labDescription == null) ? (other.labDescription != null) : !this.labDescription.equals(other.labDescription)) {
         return false;
      }
      return true;
   }

   @Override
   public int hashCode() {
      int hash = 3;
      hash = 59 * hash + (this.labName != null ? this.labName.hashCode() : 0);
      hash = 59 * hash + (this.labDescription != null ? this.labDescription.hashCode() : 0);
      return hash;
   }
   
}
