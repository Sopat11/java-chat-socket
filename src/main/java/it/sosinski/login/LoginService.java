package it.sosinski.login;

import it.sosinski.chatworker.ChatWorker;
import it.sosinski.chatworker.ChatWorkers;
import it.sosinski.utils.TextUtils;

public class LoginService {

    private final ChatWorkers chatWorkers;

    public LoginService(ChatWorkers chatWorkers) {
        this.chatWorkers = chatWorkers;
    }

    public synchronized String readChatWorkerLogin(ChatWorker chatWorker, String login) {

        boolean isFree = isLoginFree(login);
        boolean isCorrect = TextUtils.isLoginCorrect(login);

        if (!isFree) {
            chatWorker.sendServerMsg("Unfortunately this login is taken. Choose another one.");
            return null;
        } else if (!isCorrect) {
            chatWorker.sendServerMsg("Login can't start with \\. Choose another one.");
            return null;
        }
        chatWorker.sendServerMsg("Successfully logged in");

        return login;
    }

    public boolean isLoginFree(String login) {
        return chatWorkers.isLoginFree(login);
    }

    public ChatWorker getChatWorkerByLogin(String login) {
        return chatWorkers.getByLogin(login);
    }
}
