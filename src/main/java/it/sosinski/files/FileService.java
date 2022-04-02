package it.sosinski.files;

import it.sosinski.messages.Message;
import lombok.extern.java.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;

@Log
public class FileService {

    private static final String FILE_DIRECTORY = "files\\";

    public static void decodeFile(Message message) {
        new Thread( () -> {
            byte[] fileBytes = message.getFileBytes();
            try (FileOutputStream fos = new FileOutputStream(FILE_DIRECTORY + message.getFileName())) {
                fos.write(fileBytes);
            } catch (IOException e) {
                log.log(Level.SEVERE, "Reading file failed: " + e.getMessage());
            }
        }).start();
    }
}
