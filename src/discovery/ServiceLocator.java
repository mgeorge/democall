package discovery;

import constants.Constants;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mark
 */
public class ServiceLocator {

   /**
    * Send a multi-cast request for any democall servers.  Server should respond
    * with its IP address.
    *
    * @param lab The lab in which to search for a server.
    * @return IP address of server
    * @throws Exception
    */
   public String locateServer(String lab) {
      try {
         DatagramSocket socket = new DatagramSocket();
         InetAddress address = InetAddress.getByName(Constants.BROADCAST_ADDRESS);
         byte[] data = lab.getBytes();
         int port = Constants.PORT;
         socket.send(new DatagramPacket(data, data.length, address, port));
         byte[] response = new byte[256];
         DatagramPacket packet = new DatagramPacket(response, response.length);
         socket.receive(packet);
         String receivedIp = new String(packet.getData()).trim();
         socket.close();
         return receivedIp;
      } catch (Exception ex) {
         Logger.getLogger(ServiceLocator.class.getName()).log(Level.SEVERE, null, ex);
      }
      return null;
   }




}
