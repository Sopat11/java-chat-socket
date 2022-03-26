package it.sosinski.login;

import it.sosinski.messages.MessageWriter;
import lombok.SneakyThrows;
import lombok.extern.java.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

@Log
public class ConnectionService {

    private final LoginService loginService;

    public ConnectionService(LoginService loginService) {
        this.loginService = loginService;
    }

    @SneakyThrows // TODO: Przechwycić wyjątek
    public String connectNewChatWorker(Socket socket) {
        return readChatWorkerLogin(socket);
    }

    private String readChatWorkerLogin(Socket socket) throws IOException {
        MessageWriter messageWriter = new MessageWriter(socket);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        messageWriter.write("What's your login?");

        String login;
        boolean isFree;
        boolean isCorrect;
        do {
            login = reader.readLine().trim();
            isFree = !loginService.isLoginTaken(login);
            isCorrect = isLoginCorrect(login);

            if (!isFree) {
                messageWriter.write("Unfortunately this login is taken. Choose another one.");
            } else if (!isCorrect) {
                messageWriter.write("Login can't start with \\. Choose another one.");
            }
        } while (!isFree || !isCorrect);

        return login;
    }

    private boolean isLoginCorrect(String login) {
        return !login.trim().startsWith("\\");
    }

}
