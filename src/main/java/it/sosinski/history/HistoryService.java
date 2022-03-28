package it.sosinski.history;

import it.sosinski.channel.Channel;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class HistoryService {

    public static void saveMessageToTxtFile(String text, Channel channel) {
        new Thread(() -> {
            PrintWriter printWriter = null;
            try {
                FileWriter fileWriter = new FileWriter(channel.getName() + ".txt", true);
                printWriter = new PrintWriter(fileWriter);
                printWriter.write(text + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (printWriter != null) {
                    printWriter.close();
                }
            }
        }).start();
    }
}
