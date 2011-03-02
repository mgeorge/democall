/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import constants.Constants;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author geoma48p
 */
public class Client {

   private String ip;
   private PopupMenu popup = new PopupMenu();
   private Image image = Toolkit.getDefaultToolkit().getImage("icon.png");
   private final TrayIcon trayIcon = new TrayIcon(image, "Request Help", popup);
   private String machine;
   private Thread shutDownHook = new Thread() {

      @Override
      public void run() {
         exit();
      }
   };

   public Client() {
      try {
         createTrayMenu();

         shutDownHook.setDaemon(true);
         Runtime.getRuntime().addShutdownHook(shutDownHook);

//         String compName = System.getenv("COMPUTERNAME");
         String compName = "SB303-13";


         String[] nameBits = compName.split("-");
         final String lab = nameBits[0];
         machine = nameBits[1];

         if (compName == null || nameBits.length < 2) {
            trayIcon.displayMessage("Whoops", "COMPUTERNAME environment variable is not set or not in the correct format.", TrayIcon.MessageType.ERROR);
         }


         // create a timer to cause a timeout if server IP not found within 5 seconds
         Timer timer = new Timer();
         timer.schedule(new TimeoutTask(trayIcon), 5000);
         ip = findServerIP(lab);

         // we received a response so cancel timer
         timer.cancel();

      } catch (Exception ex) {
         Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
      }


   }

   private void createTrayMenu() {
      try {
         trayIcon.setImageAutoSize(true);
         MenuItem request = new MenuItem("Request Help");
         request.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
               makeRequest();
            }
         });
         final MenuItem cancel = new MenuItem("Cancel Request");
         cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
               cancelRequest();
            }
         });
         MenuItem exit = new MenuItem("Exit");
         exit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
               exit();
            }
         });
         popup.add(request);
         popup.add(cancel);
         popup.addSeparator();
         popup.add(exit);
         SystemTray.getSystemTray().add(trayIcon);
      } catch (Exception ex) {
         Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   private void sendRequest(String request) throws Exception {
      Socket socket = new Socket(ip, Constants.PORT);
      PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
      writer.println(request);
      writer.flush();
      writer.close();
      socket.close();
   }

   private String findServerIP(String lab) throws Exception {
      DatagramSocket socket = new DatagramSocket();
      InetAddress address = InetAddress.getByName(Constants.BROADCAST_ADDRESS);
      byte[] data = lab.getBytes();
      int port = Constants.PORT;
      socket.send(new DatagramPacket(data, data.length, address, port));

      byte[] response = new byte[256];
      DatagramPacket packet = new DatagramPacket(response, response.length);

      socket.receive(packet);
      String serverIp = new String(packet.getData()).trim();
      socket.close();

      return serverIp;
   }


   private void makeRequest() {
      try {
         sendRequest("request " + machine);
         trayIcon.displayMessage("Help is on the way", "Your request is now in the queue.", TrayIcon.MessageType.INFO);
      } catch (Exception ex) {
         trayIcon.displayMessage("Whoops", "Failed to make request", TrayIcon.MessageType.ERROR);
         Logger.getLogger(Client.class.getName()).log(Level.SEVERE, "Error making request", ex);
      }
   }

   private void cancelRequest() {
      try {
         sendRequest("cancel " + machine);
         trayIcon.displayMessage("Request removed", "Your request has been removed from the queue.", TrayIcon.MessageType.INFO);
      } catch (Exception ex) {
         trayIcon.displayMessage("Whoops", "Failed to cancel request", TrayIcon.MessageType.ERROR);
         Logger.getLogger(Client.class.getName()).log(Level.SEVERE, "Error cancelling request", ex);
      }
   }

   private void exit() {
      // cancel any requests the student has made
      cancelRequest();

      // remove any shutdown hooks so we don't cause a deadlock when we call System.exit below
      Runtime.getRuntime().removeShutdownHook(shutDownHook);

      // remove the tray icon from the system tray
      SystemTray.getSystemTray().remove(trayIcon);

      // force an exit since they are several threads lurking around that could keep the process running
      System.exit(0);
   }

   public static void main(String[] args) throws Exception {
      new Client();
   }
}
