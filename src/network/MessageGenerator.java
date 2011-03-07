package network;

/**
 *
 * @author mark
 */
public class MessageGenerator {

   public String requestHelp(String machine) {
      return "request " + machine;
   }

   public String cancelRequest(String machine) {
      return "cancel " + machine;
   }

   public String requestQueue() {
      return "queue 0";
   }

}
