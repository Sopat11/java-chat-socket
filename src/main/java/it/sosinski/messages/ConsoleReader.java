package it.sosinski.messages;

import lombok.extern.java.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;
import java.util.logging.Level;

@Log
public class ConsoleReader{

    private final Consumer<String> onText;
    private BufferedReader reader;

    public ConsoleReader(InputStream inputStream, Consumer<String> onText) {
        this.onText = onText;
        reader = new BufferedReader(new InputStreamReader(inputStream));
    }

    public void read() {
        String text;
        try {
            while ((text = reader.readLine()) != null) {
                onText.accept(text);
            }
        } catch (IOException e) {
            log.log(Level.SEVERE, "Read message failed: " + e.getMessage());
        }
    }
}

