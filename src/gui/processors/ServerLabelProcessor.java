package gui.processors;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;

/**
 *
 * @author Mark
 */
public class ServerLabelProcessor extends LabelProcessor {

   @Override
   public MouseAdapter getMouseAdapter() {
      return new MouseAdapter() {

         @Override
         public void mouseClicked(MouseEvent e) {
            JLabel label = (JLabel) e.getSource();
            int id = new Integer(label.getText());
            cancel(id);
         }
      };
   }
}
