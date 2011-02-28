/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import gui.MapPanel303;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author geoma48p
 */
public class Server {

   public static void main(String[] args) throws IOException {
      System.out.println("Server");

      JFrame frame = new JFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      MapPanel303 panel = new MapPanel303();
      frame.add(panel);
      frame.pack();
      frame.setVisible(true);

      ServerSocket ss = new ServerSocket(7321);

      while (true) {
         Socket socket = ss.accept();

         new Thread(new RequestThread(panel, socket)).start();
      }
   }
}
