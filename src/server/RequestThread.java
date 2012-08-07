package server;

import gui.processors.LabelProcessor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author geoma48p
 */
public class RequestThread extends Thread {

   private final LabelProcessor processor;
   private final Socket socket;

   public RequestThread(LabelProcessor processor, Socket socket) {
      this.setDaemon(true);
      this.processor = processor;
      this.socket = socket;
   }

   @Override
   public void run() {
      try {
         BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         String message = reader.readLine();
         if(message !=null) {  // readLine returns null if buffer is empty
            processMessage(message);
         }
         socket.close();
      } catch (IOException ex) {
         Logger.getLogger(RequestThread.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   public void processMessage(String message) {
      String[] messageBits = message.split(" ");

      String performative = messageBits[0];
      int machineId = Integer.parseInt(messageBits[1]);

      if (performative.equals("request")) {
         processor.request(machineId);
      } else if (performative.equals("cancel")) {
         processor.cancel(machineId);
      } else if (performative.equals("queue")) {
         try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(processor.getQueue());
            oos.close();
         } catch (IOException ex) {
            Logger.getLogger(RequestThread.class.getName()).log(Level.SEVERE, null, ex);
         } 
      } else {
         Logger.getLogger(RequestThread.class.getName()).log(Level.SEVERE, "Unknown command: {0}", performative);
      }

   }
}
