package it.sosinski.utils;

import it.sosinski.messages.Message;

public class TextUtils {

    public static String getTextFromParentheses(String text) {
        return text.substring(text.indexOf("\"") + 1, text.lastIndexOf("\""));
    }

    public static String getTrimmedText(Message message) {
        return message.getText().trim();
    }

    public static boolean isLoginCorrect(String login) {
        return !login.trim().startsWith("\\");
    }
}
