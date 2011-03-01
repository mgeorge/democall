package server;

import gui.IMapPanel;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 *
 * @author mark
 */
public class ApplicationHandler extends Thread {

   private final IMapPanel panel;

   public ApplicationHandler(JPanel panel) {
      this.panel = (IMapPanel) panel;
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
