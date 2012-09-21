package discovery.server;

import constants.Constants;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mark
 */
public class ServiceLocator {

   private static final Logger LOG = Logger.getLogger(ServiceLocator.class.getName());   
   
   /**
    * Send a multi-cast request for any democall servers.  Server should respond
    * with its IP address.
    *
    * @param lab The lab in which to search for a server.
    * @return IP address of server
    * @throws Exception
    */
   public String locateServer(final String lab) {
      try {
         final DatagramSocket socket = new DatagramSocket();
         final InetAddress address = InetAddress.getByName(Constants.BROADCAST_ADDRESS);
         final byte[] data = lab.getBytes();
         final int port = Constants.PORT;
         socket.send(new DatagramPacket(data, data.length, address, port));
         final byte[] response = new byte[256];
         final DatagramPacket packet = new DatagramPacket(response, response.length);
         socket.receive(packet);
         final String receivedIp = new String(packet.getData()).trim();
         socket.close();
         return receivedIp;
      } catch (Exception ex) {
         LOG.log(Level.SEVERE, null, ex);
      }
      return null;
   }

}
