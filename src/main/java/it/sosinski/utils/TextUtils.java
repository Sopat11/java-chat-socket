package it.sosinski.utils;

import it.sosinski.messages.Message;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static boolean hasTwoParentheses(String text) {
        Pattern pattern = Pattern.compile("\"");
        Matcher matcher = pattern.matcher(text);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count == 2;
    }
}
