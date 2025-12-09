package dbg.log;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Logger {
    private static List<String> historyLog = new ArrayList<>();

    public static void log(String message) {
        historyLog.add(message);
        System.out.println("[LOG] " + message);
    }

    public static void saveLogFile() {
        String filename = "./log.log";
        try (FileWriter fileWriter = new FileWriter(filename);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {

            for (String line : historyLog) {
                printWriter.println(line);
            }
            System.out.println("Log saved to " + filename);

        } catch (IOException e) {
            System.err.println("Error writing log file: " + e.getMessage());
        }
    }
}