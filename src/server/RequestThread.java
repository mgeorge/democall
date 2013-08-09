package server;

import constants.Constants;
import gui.processors.AbstractLabelProcessor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mark
 */
public class RequestThread extends Thread {

   private static final Logger LOG = Logger.getLogger(RequestThread.class.getName());   
   
   private final AbstractLabelProcessor processor;
   private final Socket socket;
   private boolean dontClose = false;

   public RequestThread(final AbstractLabelProcessor processor, final Socket socket) {
      super();
      super.setDaemon(true);
      this.processor = processor;
      this.socket = socket;
   }

   @Override
   public void run() {
      try {
         final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         final String message = reader.readLine();
         if (message != null) {  // readLine returns null if buffer is empty
            processMessage(message);
         }
         if(!dontClose) {
            socket.close();
         }
      } catch (IOException ex) {
         LOG.log(Level.SEVERE, null, ex);
      }
   }

   public void processMessage(final String message) {
      
//      System.out.println(message);
      
      final String[] messageBits = message.split(" ");

      final String performative = messageBits[0];
      final int machineId = Integer.parseInt(messageBits[1]);

      if ("request".equals(performative)) {
         processor.request(machineId);
      } else if ("cancel".equals(performative)) {
         processor.cancel(machineId);
      } else if ("queue".equals(performative)) {
         try {
            final ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(processor.getQueue());
            oos.close();
         } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
         } 
      } else if ("register".equals(performative)) {
         try {
            final ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(processor.getQueue());
            oos.flush();
            Constants.PERSISTENT_SOCKETS.add(oos);
            dontClose = true;
         } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
         } 
      } else {
         LOG.log(Level.SEVERE, "Unknown command: {0}", performative);
      }

   }

}
