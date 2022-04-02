package it.sosinski.messages;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Message implements Serializable {

    private static final long serialVersionUID = 2082760007732815447L;

    private final LocalDateTime dateTime = LocalDateTime.now();
    private final MessageType messageType;
    private byte[] fileBytes;
    private String text;
    private String login;
    private String fileName;

    // Konstruktor dla wiadomości tekstowej
    public Message(MessageType messageType, String text, String login) {
        this.messageType = messageType;
        this.text = text;
        this.login = login;
    }

    // Konstruktor dla wiadomości z plikiem
    public Message(MessageType messageType, byte[] fileBytes, String login, String fileName) {
        this.messageType = messageType;
        this.fileBytes = fileBytes;
        this.login = login;
        this.fileName = fileName;
    }
}
