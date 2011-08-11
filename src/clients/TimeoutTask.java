package clients;

import constants.Constants;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.util.TimerTask;

/**
 *
 * @author mark
 */
public class TimeoutTask extends TimerTask {

   private final TrayIcon systemTrayIcon;

   public TimeoutTask(TrayIcon trayIcon) {
      this.systemTrayIcon = trayIcon;
   }



   @Override
   public void run() {
      systemTrayIcon.displayMessage("Connection Error", "Could not connect to server.  Please try again, and let supervisor know if it continues to happen.", TrayIcon.MessageType.ERROR);
      try {
         Thread.sleep(5000);
      } catch (InterruptedException ex) {
         //ignored
      }

      // remove the tray icon from the system tray
      SystemTray.getSystemTray().remove(systemTrayIcon);

      System.exit(Constants.EXIT_NO_SERVER_FOUND);
   }

}
