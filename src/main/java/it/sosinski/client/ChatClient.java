package it.sosinski.client;

import it.sosinski.handler.GlobalExceptionHandler;
import it.sosinski.messages.MessageReader;
import it.sosinski.messages.MessageWriter;
import lombok.extern.java.Log;

import java.io.IOException;
import java.net.Socket;
import java.util.function.Consumer;

@Log
public class ChatClient {

    private final Consumer<String> onText;
    private final Runnable readFromSocket;
    private final Runnable readFromConsole;

    public ChatClient(String host, int port) throws IOException {
        Socket socket = new Socket(host, port);
        onText = text -> new MessageWriter(socket).write(text);
        readFromSocket = () -> new MessageReader(socket, System.out::println, () -> {
        }).read();
        readFromConsole = () -> new MessageReader(System.in, onText).read();
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
}
