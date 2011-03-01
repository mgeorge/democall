package server;

import gui.IMapPanel;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mark
 */
public class ApplicationHandler extends Thread {

   private final IMapPanel panel;

   public ApplicationHandler(IMapPanel panel) {
      this.panel = panel;
      setDaemon(true);
   }

   @Override
   public void run() {
      try {
         ServerSocket ss = new ServerSocket(7321);
         while (true) {
            Socket socket = ss.accept();
            Thread thread = new Thread(new RequestThread(panel, socket));
            thread.setDaemon(true);
            thread.start();
         }
      } catch (Exception ex) {
         Logger.getLogger(ApplicationHandler.class.getName()).log(Level.SEVERE, null, ex);
      }
   }
}
