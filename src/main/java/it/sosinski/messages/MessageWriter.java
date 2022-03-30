package it.sosinski.messages;

import lombok.extern.java.Log;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
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

    public void writeMessage(String text, String login) {
        Message msg = new Message(MessageType.TEXT, text, login);
        try {
            messageWriter.writeObject(msg);
            messageWriter.flush();
        } catch (IOException e) {
            log.log(Level.SEVERE, "Sending object failed: " + e.getMessage());
        }
    }
}
