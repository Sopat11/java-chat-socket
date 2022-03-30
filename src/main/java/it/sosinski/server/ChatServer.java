package it.sosinski.server;

import it.sosinski.chatworker.ChatWorker;
import it.sosinski.chatworker.ChatWorkers;
import it.sosinski.login.ConnectionService;
import it.sosinski.login.LoginService;
import it.sosinski.manager.ManagerService;
import lombok.extern.java.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;

@Log
public class ChatServer {

    private final ChatServerFactory factory = new BasicChatServerFactory();
    private final ChatWorkers chatWorkers = factory.createChatWorkers();
    private final ExecutorService executorService = factory.createExecutorService();
    private final LoginService loginService = factory.createLoginService(chatWorkers);
    private final ManagerService managerService = factory.createManagerService(loginService);
    private final ConnectionService connectionService = factory.createConnectionService(loginService);

    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
        new ChatServer().start(port);
    }

    private void start(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            listen(serverSocket, port);
        } catch (IOException e) {
            log.log(Level.SEVERE, "Server failed to start: " + e.getMessage());
        }
    }

    private void listen(ServerSocket serverSocket, int port) throws IOException {
        log.log(Level.INFO, "Server is listening on port: " + port);
        while (true) {
            Socket socket = serverSocket.accept();
            log.log(Level.INFO, "New connection established...");

            new Thread( () -> {
                ChatWorker chatWorker = new ChatWorker(socket, chatWorkers, managerService);
                chatWorkers.add(chatWorker);
                executorService.execute(chatWorker);
            }).start();

        }
    }
}
