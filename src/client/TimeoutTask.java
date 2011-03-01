package client;

import java.awt.TrayIcon;
import java.util.TimerTask;

/**
 *
 * @author mark
 */
public class TimeoutTask extends TimerTask {

   private final TrayIcon trayIcon;

   public TimeoutTask(TrayIcon trayIcon) {
      this.trayIcon = trayIcon;
   }



   @Override
   public void run() {
      trayIcon.displayMessage("Connection Error", "Could not connect to server.  Please try again, and let supervisor know if it continues to happen.", TrayIcon.MessageType.ERROR);
      try {
         Thread.sleep(5000);
      } catch (InterruptedException ex) {
         //ignored
      }
      System.exit(-1);
   }

}
