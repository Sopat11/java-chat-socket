package it.sosinski.files;

import it.sosinski.messages.Message;
import it.sosinski.messages.MessageType;
import lombok.extern.java.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

@Log
public class FileService {

    private static final String FILE_DIRECTORY = "files\\";

    public static void decodeFile(Message message) {
        new Thread(() -> {
            byte[] fileBytes = message.getFileBytes();
            try (FileOutputStream fos = new FileOutputStream(FILE_DIRECTORY + message.getFileName())) {
                fos.write(fileBytes);
            } catch (IOException e) {
                log.log(Level.SEVERE, "Reading file failed: " + e.getMessage());
            }
        }).start();
    }

    public static Message encodeFile(String filePath, String login) {
        CompletableFuture<Message> completableFuture = CompletableFuture.supplyAsync(() -> {
            Path path = Path.of(filePath);
            String fileName = path.getFileName().toString();

            byte[] bytes = new byte[0];
            try {
                bytes = Files.readAllBytes(path);
            } catch (IOException e) {
                log.log(Level.SEVERE, "Writing file failed: " + e.getMessage());
            }
            return new Message(MessageType.FILE, bytes, login, fileName);
        });

        Message message = null;
        try {
            message = completableFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            log.log(Level.SEVERE, "Writing file failed: " + e.getMessage());
        }

        return message;
    }

}
