package clients;

import constants.Constants;
import discovery.ServiceLocator;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import network.MessageGenerator;
import network.RequestSender;

/**
 * The student's client for requesting help.
 * 
 * It adds a system tray icon that has a pop-up menu that the student can use to
 * request help or cancel their request. Requests are automatically canceled
 * when the student clicks the "exit" menu item, or the client is terminated by
 * the OS (via a shutdown hook) meaning there should never be requests in the
 * queue for students who have gone home.
 *
 * This aims to be both simple and robust, therefor the StudentClient stores no
 * state relating to requests, uses no persistent connections to the server,
 * and receives no information from the server.
 *
 * The client uses a multi-cast broadcast to discover the IP of the server,
 * which it then uses to send requests to the server.
 *
 * The wire protocol is very simple. A string in the form "request 23" is a
 * request for from the machine with ID 23, and "cancel 23" will cancel that
 * request. Sending multiple "request" commands or multiple "cancel" commands to
 * the server should cause no duplication or problems on the server and are
 * effectively ignored.
 *
 * @author Mark
 */
public class StudentClient {

   private final PopupMenu trayPopopMenu = new PopupMenu();
   private final Image trayIconImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon.png"));
   private final TrayIcon systemTrayIcon = new TrayIcon(trayIconImage, "Demo Call " + Constants.VERSION, trayPopopMenu);
   private final MessageGenerator generator = new MessageGenerator();

   private String serverIp;
   private String machineId;
   private  RequestSender requestSender;

   private boolean foundServer = false;

   private final Thread shutDownHook = new Thread() {

      @Override
      public void run() {
         cleanUp();
      }

   };

   public StudentClient(String compName) {
      try {
         createTrayMenu();

         // not sure this is necessary but Windows is currently not exiting when the shutdown hook kicks in...
         this.shutDownHook.setDaemon(true);

         Runtime.getRuntime().addShutdownHook(this.shutDownHook);

         if (compName == null) {
            this.systemTrayIcon.displayMessage("Whoops", "COMPUTERNAME environment variable is not set.", TrayIcon.MessageType.ERROR);

            // give user a chance to read message
            Thread.sleep(5000);

            // shutdown
            exit();
         }

         // lab machines usually have a COMPUTERNAME in the form "labname-machineId", eg SB316-23

         // process computer name to extract lab and machine ID
         String[] nameBits = compName.split("-");

         if (nameBits.length < 2) {
            this.systemTrayIcon.displayMessage("Whoops", "COMPUTERNAME environment variable is not in the expected format.", TrayIcon.MessageType.ERROR);

            // give user a chance to read message
            Thread.sleep(5000);

            // shutdown
            exit();
         }

         String lab = nameBits[0];
         this.machineId = nameBits[1];

         // create a timer to cause a timeout if server IP not found within 5 seconds
         Timer timer = new Timer();
         timer.schedule(new TimeoutTask(this.systemTrayIcon), 5000);

         // find server IP (uses network multicast)
         this.serverIp =  new ServiceLocator().locateServer(lab);

         // if we get to this point we got an IP so cancel timer
         timer.cancel();

         this.foundServer = true;

         this.requestSender = new RequestSender(serverIp);

      } catch (Exception ex) {
         Logger.getLogger(StudentClient.class.getName()).log(Level.SEVERE, null, ex);
      }


   }

   private void createTrayMenu() {
      try {

         this.systemTrayIcon.setImageAutoSize(true);

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

         this.trayPopopMenu.add(request);
         this.trayPopopMenu.add(cancel);
         this.trayPopopMenu.addSeparator();
         this.trayPopopMenu.add(exit);

         SystemTray.getSystemTray().add(this.systemTrayIcon);

      } catch (Exception ex) {
         Logger.getLogger(StudentClient.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   private void makeRequest() {
      try {
         requestSender.sendRequest(generator.requestHelp(machineId));
         systemTrayIcon.displayMessage("Help is on the way", "Your request is now in the queue.", TrayIcon.MessageType.INFO);
      } catch (Exception ex) {
         systemTrayIcon.displayMessage("Whoops", "Failed to make request", TrayIcon.MessageType.ERROR);
         Logger.getLogger(StudentClient.class.getName()).log(Level.SEVERE, "Error making request", ex);
      }
   }

   private void cancelRequest() {
      try {
         requestSender.sendRequest(generator.cancelRequest(machineId));
         systemTrayIcon.displayMessage("Request removed", "Your request has been removed from the queue.", TrayIcon.MessageType.INFO);
      } catch (Exception ex) {
         systemTrayIcon.displayMessage("Whoops", "Failed to cancel request", TrayIcon.MessageType.ERROR);
         Logger.getLogger(StudentClient.class.getName()).log(Level.SEVERE, "Error cancelling request", ex);
      }
   }

   private void exit() {
      this.cleanUp();

      // force an exit since they are several threads lurking around that could keep the process running
      System.exit(0);
   }

   private void cleanUp() {
      // cancel any requests the student has made
      if(this.foundServer) {
         cancelRequest();
      }

      // remove the tray icon from the system tray
      SystemTray.getSystemTray().remove(systemTrayIcon);
   }

   @SuppressWarnings("ResultOfObjectAllocationIgnored")
   public static void main(String[] args) throws Exception {

//      String compName = System.getenv("COMPUTERNAME");
      String compName = "SBEASTCAL1-30";

      if(args.length > 0) {
         compName = args[0];
      }

      new StudentClient(compName);
   }
}
