package discovery.server;

import constants.Constants;
import java.net.*;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mark, and Christopher Frantz
 */
public class BroadcastResponder extends Thread {

   private static final Logger LOG = Logger.getLogger(BroadcastResponder.class.getName());
   private final String lab;

   public BroadcastResponder(final String lab) {
      super();
      super.setDaemon(true);
      this.lab = lab;
   }

   @Override
   public void run() {
      try {
         final MulticastSocket socket = new MulticastSocket(Constants.PORT);
         socket.joinGroup(InetAddress.getByName(Constants.BROADCAST_ADDRESS));

         final int bufferSize = 255;

         while (true) {
            byte[] buffer = new byte[bufferSize]; // NOPMD: need to reset buffer each time
            final DatagramPacket packet = new DatagramPacket(buffer, buffer.length);  // NOPMD: safer to create new packets than try to reuse
            socket.receive(packet);
            final String requestedLab = new String(packet.getData()).trim();  // NOPMD: String.valueOf does not handle byte[] properly...
            if (requestedLab.equalsIgnoreCase(lab)) {
               final String serverIp = this.getServerIp();
               buffer = serverIp.getBytes();
               packet.setData(buffer);
               socket.send(packet);
            }
         }

      } catch (Exception ex) {
         LOG.log(Level.SEVERE, null, ex);
      }

   }

   /**
    * This code comes from Christopher Frantz and seems less likely to pick the
    * loopback IP address than other ways.
    */
   private String getServerIp() {
      try {

         for (final Enumeration<NetworkInterface> en =
            NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {

            final NetworkInterface intf = en.nextElement();

            for (final Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {

               final InetAddress inetAddress = enumIpAddr.nextElement();

               try {

                  // FIXME: Quick hack to ensure we are only getting IPv4 
                  //        addresses - need to test what happens with IPv6
                  //        addresses.
                  //        
                  //        This variable is never used - the cast is to check
                  //        if IPv4 or 6.
                  final Inet4Address testIfIpv4 = (Inet4Address) inetAddress;  // NOPMD: see above comment

                  if (!inetAddress.isLoopbackAddress()) {
                     return inetAddress.getHostAddress().toString();
                  }

               } catch (ClassCastException ignored) {
                  continue;
               }

            }
         }

      } catch (Exception ex) {
         LOG.log(Level.SEVERE, null, ex);
      }
      return null;
   }
}
