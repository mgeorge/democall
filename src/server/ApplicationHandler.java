package server;

import gui.processors.LabelProcessor;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mark
 */
public class ApplicationHandler extends Thread {

   private final LabelProcessor processor;

   public ApplicationHandler(LabelProcessor processor) {
      this.setDaemon(true);
      this.processor = processor;
   }

   @Override
   public void run() {
      try {
         ServerSocket ss = new ServerSocket(7321);
         while (true) {
            Socket socket = ss.accept();
            new RequestThread(processor, socket).start();
         }
      } catch (Exception ex) {
         Logger.getLogger(ApplicationHandler.class.getName()).log(Level.SEVERE, null, ex);
      }
   }
}
