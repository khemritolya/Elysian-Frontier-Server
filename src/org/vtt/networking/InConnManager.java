package org.vtt.networking;

import org.vtt.Server;
import org.vtt.utilities.BufferUtilities;
import org.vtt.utilities.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.StringTokenizer;

import static org.vtt.utilities.Logger.LogLevel.*;

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
        DatagramPacket dp;

        while (Server.isOnline()) {
            dp = new DatagramPacket(inBuffer, inBuffer.length);

            try {
                ds.receive(dp);

                if (inBuffer[0] == Opcode.LOGIN) {
                    StringTokenizer text = new StringTokenizer(BufferUtilities.convertBuf(inBuffer, 1));
                    String name = text.nextToken();

                    // TODO potentially do other things, like password

                    Logger.log(INFO, "Added a new user " + name + " from " + dp.getAddress());

                    Server.addClientByAddr(dp.getAddress(), name);
                } else if (inBuffer[0] == Opcode.IN_CHAT &&
                        Server.getClientByAddr(dp.getAddress()) != null) {
                    String message = BufferUtilities.convertBuf(inBuffer, 1);
                    Logger.log(USER, dp.getAddress() + ":-" + message);
                    Server.sendMessageByNotAddr(dp.getAddress(), message);
                } else if (inBuffer[0] == Opcode.KEEP_ALIVE) {
                    // TODO register that the client is still connected;
                } else {
                    // Ignore it
                }

                inBuffer = new byte[1024];
            } catch (Exception e) {
                Logger.log(e);
            }
        }
    }
}