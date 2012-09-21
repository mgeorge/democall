package constants;

/**
 *
 * @author Mark
 */
public class Constants {

   private Constants() {
   }
   
   // version number for system
   public static final String VERSION = "3.2.0";

   // randomly chosen multi-cast group
   public static final String BROADCAST_ADDRESS = "233.7.2.9";

   // randomly chosen port
   public static final int PORT = 7321;

   // time between polls for tutor client (milliseconds)
   public static final int TUTOR_CLIENT_POLL = 5000;

   // exit code for clients if no server found
   public static final int EXIT_NO_SERVER_FOUND = 100;

   // exit code if no map found that matches current COMPUTERNAME env var
   public static final int EXIT_NO_MAP_FOUND = 200;
  
}
