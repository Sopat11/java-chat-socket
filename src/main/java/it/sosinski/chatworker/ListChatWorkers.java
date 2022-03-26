package it.sosinski.chatworker;

import java.util.ArrayList;
import java.util.List;

public class ListChatWorkers implements ChatWorkers {

    private final List<ChatWorker> chatWorkers = new ArrayList<>();

    @Override
    public void add(ChatWorker chatWorker) {
        chatWorkers.add(chatWorker);
    }

    @Override
    public void remove(ChatWorker chatWorker) {
        chatWorker.leaveCurrentChannel();
        chatWorkers.remove(chatWorker);
    }


    @Override
    public void broadcast(String fromLogin, String text) {
        chatWorkers.forEach(chatWorker -> chatWorker.sendMsg(fromLogin, text));
    }

    @Override
    public boolean isLoginFree(String login) {
        return chatWorkers.stream()
                .map(ChatWorker::getLogin)
                .filter(x -> x.equals(login))
                .findFirst()
                .isEmpty();
    }

    @Override
    public ChatWorker getByLogin(String login) {
        // W docelowym miejscu użycia sprawdzamy, czy login istnieje, więc tutaj użytkownik na pewno istnieje
        return chatWorkers.stream()
                .filter(x -> x.getLogin().equals(login))
                .findFirst()
                .get();
    }
}
