package org.vtt.networking;

import org.vtt.Server;
import org.vtt.utilities.BufferUtilities;
import org.vtt.utilities.Logger;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static org.vtt.utilities.Logger.LogLevel.ERROR;

public class OutConnManager extends Thread {
    DatagramSocket ds;

    public OutConnManager() {
        try {
            ds = new DatagramSocket();
        } catch (Exception e) {
            Logger.log(ERROR, "Could not initialize outconnmanager");
        }
    }

    @Override
    public void run() {
        while (Server.isOnline()) {
            // TODO keep alive
        }
    }

    public synchronized void sendMessage(InetAddress addr, InetAddress addrFrom, String message) {
        try {
            Logger.log(message);
            byte[] buf = BufferUtilities.convertString(Opcode.OUT_CHAT, addrFrom, message);
            DatagramPacket dp = new DatagramPacket(buf, buf.length, addr, Server.port);

            ds.send(dp);
        } catch (Exception e) {
            Logger.log(e);
        }
    }

}
