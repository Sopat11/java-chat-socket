package it.sosinski.client;

import it.sosinski.files.FileService;
import it.sosinski.handler.GlobalExceptionHandler;
import it.sosinski.messages.*;
import it.sosinski.utils.CommandUtils;
import it.sosinski.utils.MessageUtils;
import it.sosinski.utils.TextUtils;
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
        if (CommandUtils.isFileSending(text)) {
            printInfoIfMissingParentheses(text);
            String path = TextUtils.getTextFromParentheses(text);
            Message message = FileService.encodeFile(path, "Server");
            messageWriter.writeFile(message);
        } else {
            messageWriter.writeServerMessage(text);
        }
    }

    private void onMessage(Message message) {
        if (MessageUtils.isTextMessage(message)) {
            System.out.println(message.getText());
        } else {
            System.out.println(message.getLogin() + " sent a file: " + message.getFileName());
            FileService.decodeFile(message);
        }
    }

    private void printInfoIfMissingParentheses(String text) {
        if (!TextUtils.hasTwoParentheses(text)) {
            messageWriter.writeServerMessage("No such command");
        }
    }

}
