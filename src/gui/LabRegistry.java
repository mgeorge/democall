package gui;

import gui.maps.MapPanel316;
import gui.maps.MapPanelRabel;
import gui.maps.MapPanel303;
import gui.maps.MapPanel317;
import gui.maps.MapPanel318;
import gui.maps.MapPanelMacGregor;
import java.util.Collection;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Mark
 */
public class LabRegistry {

   private final Map<String, Lab> labs = new ConcurrentHashMap<String, Lab>();

   public LabRegistry() {
      labs.put("SB303", new Lab("SB303", "Commerce 3.03", new MapPanel303()));
      labs.put("SB316", new Lab("SB316", "Commerce 3.16", new MapPanel316()));
      labs.put("SBEASTCAL1", new Lab("SBEASTCAL1", "MacGregor", new MapPanelMacGregor()));
      labs.put("SBEASTCAL2", new Lab("SBEASTCAL2", "Rabel", new MapPanelRabel()));
      labs.put("SB318", new Lab("SB318", "Commerce 3.18", new MapPanel318()));
      labs.put("SB317", new Lab("SB317", "Commerce 3.17", new MapPanel317()));      
   }

   public Collection<Lab> getLabs() {
      return new TreeSet<Lab>(labs.values());
   }

   public Lab getLab(final String lab) {
      return labs.get(lab);
   }

}
