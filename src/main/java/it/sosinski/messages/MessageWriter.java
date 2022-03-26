package it.sosinski.messages;

import lombok.extern.java.Log;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;

@Log
public class MessageWriter {

    private PrintWriter writer;

    public MessageWriter(Socket socket) {
        try {
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            log.log(Level.SEVERE, "Creating output stream failed: " + e.getMessage());
        }
    }

    public void write(String text) {
        writer.println(text);
    }
}
