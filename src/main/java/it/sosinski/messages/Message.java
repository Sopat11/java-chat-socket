package it.sosinski.messages;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Message implements Serializable {

    private static final long serialVersionUID = 2082760007732815447L;

    private final LocalDateTime dateTime = LocalDateTime.now();
    private final MessageType messageType;
    private final String text;
    private final byte[] fileBytes;
    private String login;

}
