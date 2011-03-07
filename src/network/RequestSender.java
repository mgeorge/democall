package network;

import constants.Constants;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Collections;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mark
 */
public class RequestSender {

   private final String destinationIp;

   public RequestSender(String destinationIp) {
      this.destinationIp = destinationIp;
   }

   public void sendRequest(String request) {
      try {
         Socket socket = new Socket(destinationIp, Constants.PORT);
         PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
         writer.println(request);
         writer.flush();
         writer.close();
         socket.close();
      } catch (Exception ex) {
         Logger.getLogger(RequestSender.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   @SuppressWarnings({"rawtypes", "unchecked"})
   public Set<Integer> requestQueue() {
      try {
         Socket socket = new Socket(destinationIp, Constants.PORT);
         PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
         writer.println(new MessageGenerator().requestQueue());
         writer.flush();
         ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
         Set<Integer> response = (Set<Integer>) ois.readObject();
         writer.close();
         ois.close();
         socket.close();
         return response;
      } catch (Exception ex) {
         Logger.getLogger(RequestSender.class.getName()).log(Level.SEVERE, null, ex);
      } 
      return Collections.EMPTY_SET;
   }

}
