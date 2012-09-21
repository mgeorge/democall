package gui;

import java.util.logging.Logger;
import javax.swing.JLabel;

/**
 *
 * @author Mark
 */
public class QueuePanel extends javax.swing.JPanel {

   /** Creates new form QueuePanel */
   public QueuePanel() {
      initComponents();
   }

   public static JLabel getQueueLabel() {
      return lblQueue;
   }

   /** This method is called from within the constructor to
    * initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is
    * always regenerated by the Form Editor.
    */
   @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      scrollPane = new javax.swing.JScrollPane();

      scrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
      scrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
      scrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
      scrollPane.setName("scrollPane"); // NOI18N

      lblQueue.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
      lblQueue.setName("lblQueue"); // NOI18N
      scrollPane.setViewportView(lblQueue);

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
      this.setLayout(layout);
      layout.setHorizontalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
            .addContainerGap())
      );
      layout.setVerticalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(layout.createSequentialGroup()
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
      );
   }// </editor-fold>//GEN-END:initComponents
   // Variables declaration - do not modify//GEN-BEGIN:variables
   private static final javax.swing.JLabel lblQueue = new javax.swing.JLabel();
   private javax.swing.JScrollPane scrollPane;
   // End of variables declaration//GEN-END:variables
}
