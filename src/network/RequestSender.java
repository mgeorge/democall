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

   public RequestSender(final String destinationIp) {
      this.destinationIp = destinationIp;
   }

   public void sendRequest(final String request) {
      try {
         final Socket socket = new Socket(destinationIp, Constants.PORT);
         final PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
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
         final Socket socket = new Socket(destinationIp, Constants.PORT);
         final PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
         writer.println(messageGenerator.requestQueue());
         writer.flush();
         final ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
         final Collection<Integer> response = (Collection<Integer>) ois.readObject();
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
