package it.sosinski.messages;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {

    private static final long serialVersionUID = 2082760007732815447L;

    private final MessageType messageType;
    private final LocalDateTime dateTime = LocalDateTime.now();
    private final String text;
    private final String login;

    public Message(MessageType messageType, String text, String login) {
        this.messageType = messageType;
        this.text = text;
        this.login = login;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getLogin() {
        return login;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageType=" + messageType +
                ", dateTime=" + dateTime +
                ", text='" + text + '\'' +
                ", login='" + login + '\'' +
                '}';
    }
}
