package it.sosinski.chatworker;

import it.sosinski.channel.Channel;
import it.sosinski.manager.MainCommands;
import it.sosinski.manager.ManagerService;
import it.sosinski.messages.*;
import lombok.extern.java.Log;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;

@Log
public class ChatWorker implements Runnable {

    private final Socket socket;
    private final ChatWorkers chatWorkers;
    private final MessageWriter messageWriter;
    private final ManagerService managerService;
    private String login = "Login"; // TODO: Zaimplementować
    private Channel currentChannel;

    public ChatWorker(Socket socket, ChatWorkers chatWorkers, ManagerService managerService) {
        this.socket = socket;

        this.chatWorkers = chatWorkers;
        this.managerService = managerService;

        this.messageWriter = new MessageWriter(socket);
    }

    @Override
    public void run() {
        this.messageWriter.writeMessage("Print available commands: \\\\h", "Server");
        new MessageReader(socket, this::onMessage, () -> chatWorkers.remove(this)).read();
    }

    private void onMessage(Message message) {
        log.log(Level.INFO, "onMessage" + message);
        String text = message.getText().trim();
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

    public void sendMsg(Message message) {
        messageWriter.writeMessage(message.getLogin() + ": " + message.getText(), message.getLogin());
    }

    public void sendServerMsg(String text) {
        messageWriter.writeMessage(text, "Server");
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
