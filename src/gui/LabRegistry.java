package gui;

import gui.maps.MapPanel316;
import gui.maps.MapPanelRabel;
import gui.maps.MapPanel303;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 *
 * @author mark
 */
public class LabRegistry {

   private Map<String, Lab> labs = new HashMap<String, Lab>();

   public LabRegistry() {
      labs.put("SB303", new Lab("SB303", "Commerce 3.03", new MapPanel303()));
      labs.put("SB316", new Lab("SB316", "Commerce 3.16", new MapPanel316()));
      labs.put("SBEASTCAL2", new Lab("SBEASTCAL2", "Rabel", new MapPanelRabel()));
   }

   public Collection<Lab> getLabs() {
      return new TreeSet<Lab>(labs.values());
   }

   public Lab getLab(String lab) {
      return labs.get(lab);
   }

}