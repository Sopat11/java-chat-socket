package it.sosinski.login;

import it.sosinski.chatworker.ChatWorker;
import it.sosinski.chatworker.ChatWorkers;

public class LoginService {

    private final ChatWorkers chatWorkers;

    public LoginService(ChatWorkers chatWorkers) {
        this.chatWorkers = chatWorkers;
    }

    public boolean isLoginTaken(String login) {
        return !chatWorkers.isLoginFree(login);
    }

    public ChatWorker getChatWorkerByLogin(String login) {
        return chatWorkers.getByLogin(login);
    }
}
