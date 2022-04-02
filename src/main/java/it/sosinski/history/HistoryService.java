package it.sosinski.history;

import it.sosinski.channel.Channel;
import it.sosinski.chatworker.ChatWorker;
import lombok.extern.java.Log;

import java.io.*;
import java.util.logging.Level;

@Log
public class HistoryService {

    private static final String HISTORY_PATH = "history\\";

    public static void saveMessageToTxtFile(String text, Channel channel) {
        new Thread(() -> {
            String filePath = HISTORY_PATH + channel.getName() + ".txt";
            try (FileWriter fileWriter = new FileWriter(filePath, true);
                 PrintWriter printWriter = new PrintWriter(fileWriter)) {

                printWriter.write(text + "\n");
            } catch (IOException e) {
                log.log(Level.SEVERE, "Saving message to history failed: " + e.getMessage());
            }
        }).start();
    }

    public static void readHistory(ChatWorker chatWorker) {
        new Thread(() -> {
            Channel channel = chatWorker.getCurrentChannel();
            String filePath = HISTORY_PATH + channel.getName() + ".txt";

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String text;
                while ((text = reader.readLine()) != null) {
                    chatWorker.sendServerMsg(text);
                }
            } catch (IOException e) {
                log.log(Level.SEVERE, "Reading message from history failed: " + e.getMessage());
            }
        }).start();
    }
}
