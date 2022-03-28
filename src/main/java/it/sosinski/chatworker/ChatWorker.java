package it.sosinski.chatworker;

import it.sosinski.channel.Channel;
import it.sosinski.manager.MainCommands;
import it.sosinski.manager.ManagerService;
import it.sosinski.messages.MessageReader;
import it.sosinski.messages.MessageWriter;
import lombok.extern.java.Log;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;

@Log
public class ChatWorker implements Runnable {

    private final Socket socket;
    private final ChatWorkers chatWorkers;
    private final MessageWriter writer;
    private final MessageReader reader;
    private final ManagerService managerService;
    private final String login;
    private Channel currentChannel;

    public ChatWorker(Socket socket, ChatWorkers chatWorkers, ManagerService managerService, String login) {
        this.socket = socket;
        this.chatWorkers = chatWorkers;
        this.managerService = managerService;
        this.login = login;
        this.reader = new MessageReader(socket, this::onText, () -> chatWorkers.remove(this));
        this.writer = new MessageWriter(socket);
        this.writer.write("Wyświetlenie dostępnych komend: \\\\h");
    }

    @Override
    public void run() {
        reader.read();
    }

    private void onText(String text) {
        //Zamknięcie aplikacji
        if (isExitCommand(text)) {
            leaveCurrentChannel();
            closeSocket();
            //Przejście do managera komend
        } else if (isServerCommand(text.trim())) {
            managerService.process(this, text);
        } else {
            if (currentChannel != null) {
                currentChannel.sendMessageToUsers(login, text);
                currentChannel.saveMessage(login, text);
            } else {
                this.sendServerMsg("You need to connect a channel");
            }
        }
    }

    public void sendMsg(String fromLogin, String text) {
        writer.write(fromLogin + ": " + text);
    }

    public void sendServerMsg(String text) {
        writer.write(text);
    }

    public Channel getCurrentChannel() {
        return currentChannel;
    }

    public void setCurrentChannel(Channel channel) {
        this.currentChannel = channel;
    }

    public void leaveCurrentChannel() {
        if (currentChannel != null) {
            currentChannel.disconnectUser(this);
            this.currentChannel = null;
        }
        this.sendServerMsg("You left the channel");
    }

    public String getLogin() {
        return login;
    }

    private boolean isServerCommand(String text) {
        return text.startsWith("\\\\");
    }

    private boolean isExitCommand(String text) {
        return text.endsWith(MainCommands.END_SESSION.getCode());
    }

    private void closeSocket() {
        try {
            socket.close();
        } catch (IOException e) {
            log.log(Level.SEVERE, "Closing socked failed: " + e.getMessage());
        }
    }

}
