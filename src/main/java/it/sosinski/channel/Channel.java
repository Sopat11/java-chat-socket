package it.sosinski.channel;

import it.sosinski.chatworker.ChatWorker;
import it.sosinski.history.HistoryService;
import it.sosinski.messages.Message;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Channel {

    private final List<ChatWorker> loggedChatWorkers = new ArrayList<>();
    private final List<ChatWorker> allowedChatWorkers = new ArrayList<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    @Getter
    private final String name;
    @Getter
    private final boolean isPrivate;

    Channel(ChatWorker chatWorker, String name, Boolean isPrivate) {
        this.name = name;
        this.isPrivate = isPrivate;
        loggedChatWorkers.add(chatWorker);
        if (isPrivate) {
            allowedChatWorkers.add(chatWorker);
        }
    }

    public void disconnectUser(ChatWorker chatWorker) {
        lock.writeLock().lock();
        loggedChatWorkers.remove(chatWorker);
        lock.writeLock().unlock();

        lock.readLock().lock();
        loggedChatWorkers.forEach(chw -> chw.sendServerMsg(chatWorker.getLogin() + " left the channel"));
        lock.readLock().unlock();
    }

    public void sendMessageToUsers(Message message) {
        lock.readLock().lock();
        loggedChatWorkers.forEach(chatWorker -> chatWorker.sendMsg(message));
        lock.readLock().unlock();
    }

    public void saveMessage(String message) {
        HistoryService.saveMessageToTxtFile(message, this);
    }

    public boolean isAllowed(ChatWorker chatWorker) {
        lock.readLock().lock();
        boolean allowed = allowedChatWorkers.contains(chatWorker);
        lock.readLock().unlock();
        return allowed;
    }

    public void allow(ChatWorker chatWorker) {
        lock.writeLock().lock();
        allowedChatWorkers.add(chatWorker);
        lock.writeLock().unlock();
    }

    List<ChatWorker> getLoggedChatWorkers() {
        lock.readLock().lock();
        List<ChatWorker> loggedChatWorkers = this.loggedChatWorkers;
        lock.readLock().unlock();
        return loggedChatWorkers;
    }

    void connectUser(ChatWorker chatWorker) {
        lock.writeLock().lock();
        loggedChatWorkers.add(chatWorker);
        lock.writeLock().unlock();
    }

    @Override
    public String toString() {
        String privateWord = isPrivate ? "(private)" : "(public)";

        return String.format("Channel: %s %s logged users: %d", name, privateWord, loggedChatWorkers.size());
    }
}
