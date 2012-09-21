package gui.processors;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;

/**
 *
 * @author Mark
 */
public class ServerLabelProcessor extends AbstractLabelProcessor {

   @Override
   public final MouseAdapter getMouseAdapter() {
      return new MouseAdapter() {

         @Override
         public void mousePressed(final MouseEvent e) {
            super.mousePressed(e);
            final JLabel label = (JLabel) e.getSource();
            final int id = Integer.valueOf(label.getText());
            cancel(id);
         }

      };
   }
}
