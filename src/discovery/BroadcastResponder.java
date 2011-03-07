package discovery;

import constants.Constants;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mark
 */
public class BroadcastResponder extends Thread {

   private String lab;

   public BroadcastResponder(String lab) {
      this.setDaemon(true);
      this.lab = lab;
   }

   @Override
   public void run() {
      try {
         MulticastSocket socket = new MulticastSocket(Constants.PORT);
         socket.joinGroup(InetAddress.getByName(Constants.BROADCAST_ADDRESS));

         while (true) {
            byte[] buffer = new byte[255];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            String requestedLab = new String(packet.getData()).trim();
            if (requestedLab.equalsIgnoreCase(lab)) {
               String serverIp = this.getServerIp();
               buffer = serverIp.getBytes();
               socket.send(new DatagramPacket(buffer, buffer.length, packet.getAddress(), packet.getPort()));
            }
         }
         
      } catch (Exception ex) {
         Logger.getLogger(BroadcastResponder.class.getName()).log(Level.SEVERE, null, ex);
      }

   }

   /**
    *  This code comes from Christopher Frantz and seems less likely to pick the loopback IP address than other ways.
    */
   private String getServerIp() {
      try {

         for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
            NetworkInterface intf = en.nextElement();
            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
               InetAddress inetAddress = enumIpAddr.nextElement();
               try {
                  // FIXME: quick hack to ensure we are only getting IPv4 addresses - need to test what happens with IPv6 addresses in the labs
                  Inet4Address testIfIpv4 = (Inet4Address) inetAddress;
                  if (!inetAddress.isLoopbackAddress()) {
                     return inetAddress.getHostAddress().toString();
                  }
               } catch (ClassCastException ignored) {
                  // ignored
               }
            }
         }
      } catch (Exception ex) {
         Logger.getLogger(BroadcastResponder.class.getName()).log(Level.SEVERE, null, ex);
      }
      return null;
   }
}
