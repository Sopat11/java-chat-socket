package it.sosinski.chatworker;

import it.sosinski.channel.Channel;
import it.sosinski.files.FileService;
import it.sosinski.manager.MainCommands;
import it.sosinski.manager.ManagerService;
import it.sosinski.messages.Message;
import it.sosinski.messages.MessageReader;
import it.sosinski.messages.MessageType;
import it.sosinski.messages.MessageWriter;
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
    private String login;
    private Channel currentChannel;

    public ChatWorker(Socket socket, ChatWorkers chatWorkers, ManagerService managerService) {
        this.socket = socket;

        this.chatWorkers = chatWorkers;
        this.managerService = managerService;

        this.messageWriter = new MessageWriter(socket);
    }

    @Override
    public void run() {
        this.messageWriter.writeTextMessage("Type your login", "Server");
        new MessageReader(socket, this::onMessage, () -> chatWorkers.remove(this)).read();
    }

    private void onMessage(Message message) {
        if (login == null) {
            login(getTrimmedTextFromMessage(message));
        } else {
            //Zamknięcie aplikacji
            if (isExitCommand(getTrimmedTextFromMessage(message))) {
                leaveCurrentChannel();
                closeSocket();

                //Przejście do managera komend
            } else if (isServerCommand(getTrimmedTextFromMessage(message))) {
                managerService.process(this, getTrimmedTextFromMessage(message));
            } else {

                //Wysłanie wiadomości do użytkowników na kanale
                if (currentChannel != null) {
                    message.setLogin(login);
                    currentChannel.sendMessageToUsers(message);
                    currentChannel.saveMessage(login, getTrimmedTextFromMessage(message));
                } else {
                    this.sendServerMsg("You need to connect a channel");
                }
            }
        }
    }

    private void login(String login) {
        this.login = managerService.login(this, login);
    }

    public void sendMsg(Message message) {
        if (message.getMessageType() == MessageType.TEXT) {
            messageWriter.writeTextMessage(message.getLogin() + ": " + message.getText(), message.getLogin());
        } else {
            FileService.decodeFile(message);
        }
    }

    public void sendServerMsg(String text) {
        messageWriter.writeTextMessage(text, "Server");
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

    private String getTrimmedTextFromMessage(Message message) {
        return message.getText().trim();
    }

    private void closeSocket() {
        try {
            socket.close();
        } catch (IOException e) {
            log.log(Level.SEVERE, "Closing socked failed: " + e.getMessage());
        }
    }

}
