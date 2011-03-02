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
      this.setDaemon(true);
      this.panel = (IMapPanel) panel;
   }

   @Override
   public void run() {
      try {
         ServerSocket ss = new ServerSocket(7321);
         while (true) {
            Socket socket = ss.accept();
            new RequestThread(panel, socket).start();
         }
      } catch (Exception ex) {
         Logger.getLogger(ApplicationHandler.class.getName()).log(Level.SEVERE, null, ex);
      }
   }
}
