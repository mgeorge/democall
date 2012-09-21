package network;

import constants.Constants;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mark
 */
public class RequestSender {

   private static final Logger LOG = Logger.getLogger(RequestSender.class.getName());   
   
   private final String destinationIp;
   private final MessageGenerator messageGenerator = new MessageGenerator();

   public RequestSender(String destinationIp) {
      this.destinationIp = destinationIp;
   }

   public void sendRequest(String request) {
      try {
         Socket socket = new Socket(destinationIp, Constants.PORT);
         PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
         writer.println(request);
         writer.close();
         socket.close();
      } catch (Exception ex) {
         LOG.log(Level.SEVERE, null, ex);
      }
   }

   @SuppressWarnings({"rawtypes", "unchecked"})
   public Collection<Integer> requestQueue() {
      try {
         Socket socket = new Socket(destinationIp, Constants.PORT);
         PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
         writer.println(messageGenerator.requestQueue());
         writer.flush();
         ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
         Collection<Integer> response = (Collection<Integer>) ois.readObject();
         writer.close();
         ois.close();
         socket.close();
         return response;
      } catch (Exception ex) {
         LOG.log(Level.SEVERE, null, ex);
      }
      return Collections.EMPTY_SET;
   }

}
