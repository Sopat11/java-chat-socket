package it.sosinski.client;

import it.sosinski.handler.GlobalExceptionHandler;
import it.sosinski.messages.ConsoleReader;
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
    private final MessageWriter messageWriter;

    public ChatClient(String host, int port) throws IOException {
        Socket socket = new Socket(host, port);

        messageWriter = new MessageWriter(socket);
        onText = message -> messageWriter.writeMessage(message, "Server");
        readFromSocket =  () -> new MessageReader(socket, x -> System.out.println(x.getText()), () -> {
        }).read();

        readFromConsole = () -> new ConsoleReader(System.in, onText).read();
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
