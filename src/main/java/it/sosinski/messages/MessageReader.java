package it.sosinski.messages;

import lombok.extern.java.Log;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.function.Consumer;
import java.util.logging.Level;

@Log
public class MessageReader {

    private final Consumer<Message> onMessage;
    private final Runnable onClose;
    private final Socket socket;

    public MessageReader(Socket socket, Consumer<Message> onMessage, Runnable onClose) {
        this.onMessage = onMessage;
        this.onClose = onClose;
        this.socket = socket;
    }

    public void read() {
        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            while (true) {

                Object object = ois.readObject();
                Message message = (Message) object;
                onMessage.accept(message);
            }
        } catch (ClassNotFoundException | EOFException e) {
            log.log(Level.SEVERE, "Reading input stream failed: " + e.getMessage());
        } catch (IOException e) {
            log.log(Level.SEVERE, "Creating input stream failed: " + e.getMessage());
        } finally {
            if (onClose != null) {
                onClose.run();
            }
        }
    }
}
