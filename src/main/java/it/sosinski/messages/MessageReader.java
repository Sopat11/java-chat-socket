package it.sosinski.messages;

import lombok.extern.java.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.function.Consumer;
import java.util.logging.Level;

@Log
public class MessageReader {

    private final Consumer<String> onText;
    private BufferedReader reader;
    private Runnable onClose;

    public MessageReader(InputStream inputStream, Consumer<String> onText) {
        this.onText = onText;
        reader = new BufferedReader(new InputStreamReader(inputStream));
    }

    public MessageReader(Socket socket, Consumer<String> onText, Runnable onClose) {
        this.onText = onText;
        this.onClose = onClose;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            log.log(Level.SEVERE, "Creating input stream failed: " + e.getMessage());
        }
    }

    public void read() {
        String text;
        try {
            while ((text = reader.readLine()) != null) {
                onText.accept(text);
            }
        } catch (IOException e) {
            log.log(Level.SEVERE, "Read message failed: " + e.getMessage());
        } finally {
            if (onClose != null) {
                onClose.run();
            }
        }
    }
}
