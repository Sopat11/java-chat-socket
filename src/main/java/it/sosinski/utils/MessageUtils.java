package it.sosinski.utils;

import it.sosinski.messages.Message;
import it.sosinski.messages.MessageType;

public class MessageUtils {

    public static boolean isTextMessage(Message message) {
        return message.getMessageType() == MessageType.TEXT;
    }

    public static boolean hasFile(Message message) {
        return message.getFileBytes().length > 0;
    }
}
