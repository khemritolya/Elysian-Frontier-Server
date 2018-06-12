package org.vtt.networking;

import org.vtt.Server;
import org.vtt.utilities.BufferUtilities;
import org.vtt.utilities.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.StringTokenizer;

import static org.vtt.utilities.Logger.LogLevel.ERROR;
import static org.vtt.utilities.Logger.LogLevel.USER;

public class InConnManager extends Thread {
    public void run() {
        DatagramSocket ds;

        try {
            ds = new DatagramSocket(Server.port);
        } catch (IOException e) {
            Logger.log(ERROR, "Could not create socket...");
            Logger.log(e);
            return;
        }

        byte[] inBuffer = new byte[1024];
        DatagramPacket dpReceive;

        while (Server.isOnline()) {
            dpReceive = new DatagramPacket(inBuffer, inBuffer.length);

            try {
                ds.receive(dpReceive);

                if (inBuffer[0] == Opcode.LOGIN) {
                    StringTokenizer text = new StringTokenizer(BufferUtilities.convertBuf(inBuffer, 1));
                    String name = text.nextToken();

                    // TODO potentially do other things, like password

                    Server.addClientByAddr(ds.getInetAddress(), name);
                } else if (inBuffer[0] == Opcode.IN_CHAT) {
                    String message = BufferUtilities.convertBuf(inBuffer, 1);
                    Logger.log(USER, dpReceive.getAddress() + ":-" + message);
                    Server.sendMessageByNotAddr(dpReceive.getAddress(), message);
                } else if (inBuffer[0] == Opcode.KEEP_ALIVE) {
                    // TODO register that the client is still connected;
                } else {
                    // Ignore it
                }
            } catch (Exception e) {
                Logger.log(e);
            }
        }
    }
}