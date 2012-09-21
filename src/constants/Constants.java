package constants;

/**
 *
 * @author Mark
 */
public final class Constants {

   private Constants() {
   }
   
   // version number for system
   public static final String VERSION = "3.3.0";

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
   
   // amount of time the service locator waits for a response from server
   public static final int SERVICE_LOCATOR_TIMEOUT = 5000; // milliseconds

   // default size of the label fonts on the maps
   public static final int DEFAULT_MAP_FONT_SIZE = 40;
   
}
