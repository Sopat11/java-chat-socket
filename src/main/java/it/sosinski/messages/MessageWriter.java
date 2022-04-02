package it.sosinski.messages;

import lombok.extern.java.Log;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;

@Log
public class MessageWriter {

    private ObjectOutputStream messageWriter;

    public MessageWriter(Socket socket) {
        try {
            messageWriter = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            log.log(Level.SEVERE, "Creating output stream failed: " + e.getMessage());
        }
    }

    public void writeTextMessage(String text, String login) {
        Message msg = new Message(MessageType.TEXT, text, null, login);
        try {
            messageWriter.writeObject(msg);
            messageWriter.flush();
        } catch (IOException e) {
            log.log(Level.SEVERE, "Sending object failed: " + e.getMessage());
        }
    }

    public void writeFileMessage(String filePath, String login) {
        try {
            byte[] bytes = Files.readAllBytes(Path.of(filePath));
            Message message = new Message(MessageType.FILE, "File", bytes, login);
            messageWriter.writeObject(message);
            messageWriter.flush();
        } catch (IOException e) {
            log.log(Level.SEVERE, "Reading object failed: " + e.getMessage());
        }
    }
}
