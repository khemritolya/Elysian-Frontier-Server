package org.vtt.utilities;

import org.vtt.Server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Logger {
    public enum LogLevel {
        INFO(" INFO  "), DEBUG(" DEBUG "),
        WARN(" WARN  "), ERROR(" ERROR "),
        USER(" USER  "), HELP(" HELP  ");

        public final String name;

        LogLevel(String name) {
            this.name = name;
        }
    }

    private static ArrayList<String> log;
    private static Calendar calendar;
    private static String startTime;
    private static BufferedWriter out;

    static {
        log = new ArrayList<>();
        calendar = Calendar.getInstance();
        startTime = new SimpleDateFormat("HH-mm-ss-SS").format(calendar.getTime());
        try {
            out = new BufferedWriter(new OutputStreamWriter(System.out, "ASCII"), 256);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final Thread currThread = Thread.currentThread();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Server.kill();

            log(LogLevel.INFO, "Dumping Logs...");
            dump();
            try {
                currThread.join();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }));
    }

    public static void log(LogLevel level, String msg) {
        String logMessage = String.format("[%s] %s", level.name, msg);
        log.add(logMessage);
        try {
            out.write(logMessage + "\n");
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void log(String msg) {
        log(LogLevel.DEBUG, msg);
    }

    public static void log(Exception e) {
        log(LogLevel.ERROR, e);
    }

    public static void log(LogLevel l, Exception e) {
        Logger.log(l, e.toString());
        for (StackTraceElement st:e.getStackTrace()) {
            Logger.log(l, st.toString());
        }
    }

    private static void dump() {
        if (Files.notExists(Paths.get("logs"))) {
            File logsDir = new File("logs");
            if(!logsDir.mkdir()) {
                System.err.println("Critical error cannot dump logs");
            }
        }

        Path file = Paths.get("logs/elysian-frontier-server-" + startTime +".log");
        Path recent = Paths.get("logs/elysian-frontier-server-recent.log");

        try {
            if (Files.exists(recent)) Files.delete(recent);
            Files.write(recent, log, Charset.forName("ASCII"), StandardOpenOption.CREATE);
            Files.write(file, log, Charset.forName("ASCII"), StandardOpenOption.CREATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
