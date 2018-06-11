package utilities;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

public class ConfigManager {
    private static HashMap<String, String> settings;

    public static void init() {
        Logger.log(Logger.LogLevel.INFO, "Loading Configuration...");
        settings = new HashMap<>();

        try {
            Scanner reader = new Scanner(new File("config.txt"));
            while (reader.hasNext()) {
                String line = reader.nextLine();
                if (!line.startsWith("#") && !(line.length() == 0)) {
                    StringTokenizer st = new StringTokenizer(line);
                    settings.put(st.nextToken().toLowerCase(), st.nextToken());
                }
            }

        } catch (Exception e) {
            Logger.log(Logger.LogLevel.ERROR,"Service critical error in config load");
            Logger.log(e);

            System.exit(0);
        }

        if (settings.get("uname") == null) {
            Logger.log(Logger.LogLevel.ERROR,"Service critical error in config load");
            Logger.log(Logger.LogLevel.ERROR, "There is no uname");

            System.exit(0);
        }

        if (settings.get("pw") == null) {
            Logger.log(Logger.LogLevel.ERROR,"Service critical error in config load");
            Logger.log(Logger.LogLevel.ERROR, "There is no password");

            System.exit(0);
        }
    }

    public static String requestSetting(String id) {
        if (settings.get(id) == null) Logger.log(Logger.LogLevel.WARN, "Unable to find config " + id);

        return settings.get(id);
    }
}
