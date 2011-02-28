/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import gui.IMapPanel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author geoma48p
 */
public class RequestThread implements IHelpService, Runnable {

    private final IMapPanel panel;
    private final Socket socket;

    public RequestThread(IMapPanel panel, Socket socket) {
        this.panel = panel;
        this.socket = socket;
    }

    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String message = reader.readLine();
            processMessage(message);
        } catch (IOException ex) {
            Logger.getLogger(RequestThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void processMessage(String message) {
        String[] messageBits = message.split(" ");

        String performative = messageBits[0];
        int machineId = Integer.parseInt(messageBits[1]);

        if (performative.equals("request")) {
            panel.requestHelp(machineId);
        } else {
            panel.cancelRequest(machineId);
        }

    }
}
