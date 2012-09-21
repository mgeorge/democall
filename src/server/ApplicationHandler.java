package server;

import constants.Constants;
import gui.processors.AbstractLabelProcessor;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mark
 */
public class ApplicationHandler extends Thread {

   private final AbstractLabelProcessor processor;

   private static final Logger LOG = Logger.getLogger(ApplicationHandler.class.getName());

   public ApplicationHandler(final AbstractLabelProcessor processor) {
      super();
      super.setDaemon(true);
      this.processor = processor;
   }

   @Override
   public void run() {
      try {
         final ServerSocket ss = new ServerSocket(Constants.PORT);
         while (true) {
            final Socket socket = ss.accept();
            new RequestThread(processor, socket).start();
         }
      } catch (Exception ex) {
         LOG.log(Level.SEVERE, null, ex);
      }
   }
}
