package gui;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;

/**
 *
 * @author mark
 */
public class PanelRegistry {

   private Map<String, JPanel> panels = new HashMap<String, JPanel>();

   public PanelRegistry() {
      panels.put("SB303", new MapPanel303());
      panels.put("SB316", new MapPanel316());
   }

   public JPanel getPanel(String lab) {
      return panels.get(lab);
   }

}
