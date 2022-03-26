package it.sosinski.channel;

import it.sosinski.chatworker.ChatWorker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Channel {

    private final List<ChatWorker> loggedChatWorkers = new ArrayList<>();
    private final List<ChatWorker> allowedChatWorkers = new ArrayList<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final String name;
    private final Boolean isPrivate;

    Channel(ChatWorker chatWorker, String name, Boolean isPrivate) {
        this.name = name;
        this.isPrivate = isPrivate;
        loggedChatWorkers.add(chatWorker);
        if (isPrivate) {
            allowedChatWorkers.add(chatWorker);
        }
    }

    public void disconnectUser(ChatWorker chatWorker) {
        loggedChatWorkers.remove(chatWorker);

        lock.readLock().lock();
        loggedChatWorkers.forEach(chw -> chw.sendServerMsg(chatWorker.getLogin() + " left the channel"));
        lock.readLock().unlock();
    }

    public void sendMessageToUsers(String fromLogin, String text) {
        lock.readLock().lock();
        loggedChatWorkers.forEach(chatWorker -> chatWorker.sendMsg(fromLogin, text));
        lock.readLock().unlock();
    }

    public boolean isAllowed(ChatWorker chatWorker) {
        lock.readLock().lock();
        boolean allowed = allowedChatWorkers.contains(chatWorker);
        lock.readLock().lock();
        return allowed;
    }

    public void allow(ChatWorker chatWorker) {
        allowedChatWorkers.add(chatWorker);
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    List<ChatWorker> getLoggedChatWorkers() {
        return loggedChatWorkers;
    }

    void connectUser(ChatWorker chatWorker) {
        loggedChatWorkers.add(chatWorker);
    }

    String getName() {
        return name;
    }

    @Override
    public String toString() {
        String privateWord = isPrivate ? "(private)" : "(public)";

        return String.format("Channel: %s %s logged users: %d", name, privateWord, loggedChatWorkers.size());
    }
}
