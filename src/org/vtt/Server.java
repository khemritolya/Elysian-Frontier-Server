package org.vtt;

import org.vtt.networking.InConnManager;
import org.vtt.networking.OutConnManager;
import org.vtt.utilities.ConfigManager;
import org.vtt.utilities.Logger;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Scanner;

import static org.vtt.utilities.Logger.LogLevel.*;

public class Server {
    public static boolean online;
    public static int port;

    private static InConnManager inconn;
    private static OutConnManager outconn;
    private static HashMap<InetAddress, String> clients;

    public static void main(String[] args) {
        Logger.log(Logger.LogLevel.INFO, "Initializing Elysian Frontier Server...");

        online = true;
        clients = new HashMap<>();
        ConfigManager.init();

        try {
            port = Integer.parseInt(ConfigManager.requestSetting("port"));
        } catch (Exception e) {
            Logger.log(e);
            System.exit(0);
        }

        Logger.log(Logger.LogLevel.INFO, "Done Initializing.");
        Logger.log(Logger.LogLevel.INFO, "============================");

        inconn = new InConnManager();
        inconn.setDaemon(true);
        inconn.start();

        outconn = new OutConnManager();
        outconn.setDaemon(true);
        outconn.start();

        Scanner userInput = new Scanner(System.in);

        while (Server.online) {
            String input = userInput.nextLine();

            Logger.log(USER, input);

            if (input.toLowerCase().equals("exit")) {
                Logger.log(INFO, "Shutting down server...");
                break;
            } else if (input.toLowerCase().equals("stats")) {
                Logger.log(INFO, "Elysian Frontier Server Info: ");
                Logger.log(INFO, "Port: " + port);
                Logger.log(INFO, "Number of Clients: " + clients.keySet().size());
            } else if (input.toLowerCase().equals("help")) {
                Logger.log(HELP, "[  Elysian Frontier Server  ]");
                Logger.log(HELP, "stats -  various server statistics");
                Logger.log(HELP, "exit  -  quit the server");
                Logger.log(HELP, "help  -  print this message");
            } else {
                Logger.log(INFO, "Unknown Command " + input);
            }
        }

        inconn.interrupt();
        outconn.interrupt();
    }

    public static boolean isOnline() {
        return online;
    }

    public static synchronized void addClientByAddr(InetAddress addr, String name) {
        clients.put(addr, name);
    }

    public static synchronized void sendMessageByNotAddr(InetAddress addr, String message) {
        for (InetAddress a:clients.keySet()) {
            //if (a != addr) {
                outconn.sendMessage(a, addr, message);
            //}
        }

        Logger.log("Rebroadcasted from " + addr + ": " + message);
    }

    public static synchronized void sendMessageByAddr(InetAddress addr, InetAddress from, String message) {
        outconn.sendMessage(addr, from, message);
    }
}
