package network;

/**
 *
 * @author Mark
 */
public class MessageGenerator {

   public String requestHelp(final String machine) {
      return "request " + machine;
   }

   public String cancelRequest(final String machine) {
      return "cancel " + machine;
   }

   public String requestQueue() {
      return "queue 0";
   }

}
