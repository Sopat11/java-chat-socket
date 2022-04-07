package it.sosinski.chatworker;

import it.sosinski.channel.Channel;
import it.sosinski.manager.ManagerService;
import it.sosinski.messages.Message;
import it.sosinski.messages.MessageReader;
import it.sosinski.messages.MessageWriter;
import it.sosinski.utils.CommandUtils;
import it.sosinski.utils.MessageFormatter;
import it.sosinski.utils.MessageUtils;
import it.sosinski.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;
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
    @Getter
    private String login;
    @Getter
    @Setter
    private Channel currentChannel;

    public ChatWorker(Socket socket, ChatWorkers chatWorkers, ManagerService managerService) {
        this.socket = socket;

        this.chatWorkers = chatWorkers;
        this.managerService = managerService;

        this.messageWriter = new MessageWriter(socket);
    }

    @Override
    public void run() {
        this.messageWriter.writeServerMessage("Type your login");
        new MessageReader(socket, this::onMessage, () -> chatWorkers.remove(this)).read();
    }

    private void onMessage(Message message) {

        // Czytanie loginu aż do powodzenia
        if (login == null) {
            login(TextUtils.getTrimmedText(message));

        } else {

            //Wysłanie wiadomości do użytkowników na kanale
            if (!CommandUtils.isServerCommand(message.getText())) {
                sendMessageToChannel(message);
                saveMessageIfTextType(message);
            }

            // Zamknięcie aplikacji
            else if (CommandUtils.isExitCommand(TextUtils.getTrimmedText(message))) {
                leaveCurrentChannel();
                closeSocket();
            }

            //Przejście do managera komend
            else if (CommandUtils.isServerCommand(TextUtils.getTrimmedText(message))) {
                managerService.process(this, TextUtils.getTrimmedText(message));
            }
        }
    }

    private void login(String login) {
        this.login = managerService.login(this, login);
    }

    public void sendMsg(Message message) {
        if (MessageUtils.isTextMessage(message)) {
//            messageWriter.writeTextMessage(message.getLogin() + ": " + message.getText(), message.getLogin());
            messageWriter.writeTextMessage(MessageFormatter.formatMessageStandard(message), message.getLogin());
        } else {
            messageWriter.writeFile(message);
        }
    }

    public void sendServerMsg(String text) {
        messageWriter.writeServerMessage(text);
    }

    public void leaveCurrentChannel() {
        if (isOnChannel()) {
            currentChannel.disconnectUser(this);
            this.currentChannel = null;
            this.sendServerMsg("You left the channel");
        }
    }

    private void sendMessageToChannel(Message message) {
        if (isOnChannel()) {
            message.setLogin(login);
            currentChannel.sendMessageToUsers(message);
        } else {
            this.sendServerMsg("You need to connect a channel");
        }
    }

    private void saveMessageIfTextType(Message message) {
        if (MessageUtils.isTextMessage(message) && isOnChannel()) {
            currentChannel.saveMessage(MessageFormatter.formatMessageStandard(message));
        }
    }

    private boolean isOnChannel() {
        return currentChannel != null;
    }

    private void closeSocket() {
        try {
            socket.close();
        } catch (IOException e) {
            log.log(Level.SEVERE, "Closing socked failed: " + e.getMessage());
        }
    }

}
