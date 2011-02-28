/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author geoma48p
 */
public class Client {

   private static void sendRequest(String request) throws Exception {
      Socket socket = new Socket("127.0.0.1", 7321);
      PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
      writer.println(request);
      writer.flush();
      writer.close();
   }

   public static void main(String[] args) throws UnknownHostException, IOException, AWTException {

      PopupMenu popup = new PopupMenu();
      Image image = Toolkit.getDefaultToolkit().getImage("icon.png");
      final TrayIcon icon = new TrayIcon(image, "Request Help", popup);
      icon.setImageAutoSize(true);

      MenuItem request = new MenuItem("Request Help");
      request.addActionListener(new ActionListener() {

         public void actionPerformed(ActionEvent e) {
            try {
               sendRequest("request 14");
               icon.displayMessage("Success", "Request sent", TrayIcon.MessageType.INFO);
            } catch (Exception ex) {
               icon.displayMessage("Whoops", "Failed to make request", TrayIcon.MessageType.ERROR);
            }
         }
      });


      MenuItem cancel = new MenuItem("Cancel Request");
      cancel.addActionListener(new ActionListener() {

         public void actionPerformed(ActionEvent e) {
            try {
               sendRequest("cancel 14");
               icon.displayMessage("Success", "Request cancelled", TrayIcon.MessageType.INFO);
            } catch (Exception ex) {
               icon.displayMessage("Whoops", "Failed to cancel request", TrayIcon.MessageType.ERROR);
            }
         }
      });


      MenuItem exit = new MenuItem("Exit");
      exit.addActionListener(new ActionListener() {

         public void actionPerformed(ActionEvent e) {
            System.exit(0);
         }
      });

      popup.add(request);
      popup.add(cancel);
      popup.addSeparator();
      popup.add(exit);


      SystemTray tray = SystemTray.getSystemTray();
      tray.add(icon);

   }
}
