package it.sosinski.utils;

import it.sosinski.messages.Message;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MessageFormatter {

    public static String formatMessageStandard(Message message) {
        return String.format(MessageFormats.STANDARD, standardDateTime(message.getDateTime()), message.getLogin(), message.getText());
    }

    private static String standardDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return formatter.format(dateTime);
    }
}
