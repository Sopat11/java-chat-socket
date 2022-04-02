package it.sosinski.client;

import it.sosinski.handler.GlobalExceptionHandler;
import it.sosinski.messages.*;
import lombok.extern.java.Log;

import java.io.IOException;
import java.net.Socket;

@Log
public class ChatClient {

    private final Runnable readFromSocket;
    private final Runnable readFromConsole;
    private final MessageWriter messageWriter;

    public ChatClient(String host, int port) throws IOException {
        Socket socket = new Socket(host, port);

        messageWriter = new MessageWriter(socket);

        readFromSocket = () -> new MessageReader(socket, this::onMessage, () -> {
        }).read();

        readFromConsole = () -> new ConsoleReader(System.in, this::writeMessage).read();
    }

    public static void main(String[] args) throws IOException {
        Thread.setDefaultUncaughtExceptionHandler(new GlobalExceptionHandler());
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        new ChatClient(host, port).start();
    }

    private void start() {
        new Thread(readFromSocket).start();
        Thread consoleMessageReader = new Thread(readFromConsole);
        consoleMessageReader.setDaemon(true);
        consoleMessageReader.start();
    }

    private void writeMessage(String text) {
        if (!isFileSending(text)) {
            messageWriter.writeTextMessage(text, "Server2");
        } else {
            System.out.println("Writing file message");
            messageWriter.writeFileMessage(getPathFromText(text), "Server");
        }
    }

    private void onMessage(Message message) {
        if (message.getMessageType() == MessageType.TEXT) {
            System.out.println(message.getText());
        } else {
            System.out.println("FILE RECEIVED");
        }
    }

    private boolean isFileSending(String text) {
        return text.trim().startsWith("\\\\f");
    }

    private String getPathFromText(String text) {
        return text.substring(text.indexOf("\"") + 1, text.lastIndexOf("\""));
    }
}
