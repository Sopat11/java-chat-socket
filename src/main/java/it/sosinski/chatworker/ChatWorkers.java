package it.sosinski.chatworker;

public interface ChatWorkers {

    void add(ChatWorker chatWorker);

    void remove(ChatWorker chatWorker);

    void broadcast(String fromLogin, String text);

    boolean isLoginFree(String login);

    ChatWorker getByLogin(String login);
}
