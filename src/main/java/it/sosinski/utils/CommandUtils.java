package it.sosinski.utils;

import it.sosinski.channel.ChannelCommands;
import it.sosinski.manager.MainCommands;

public class CommandUtils {

    public static boolean isAskingToPrintChannels(String text) {
        return text.equals(ChannelCommands.PRINT_CHANNELS.getCode());
    }

    public static boolean isAskingForLoggedChatWorkers(String text) {
        return text.contains("--online");
    }

    public static boolean isAskingForGeneralHelp(String text) {
        return text.startsWith(MainCommands.HELP.getCode());
    }

    public static boolean isAskingForChannelHelp(String text) {
        return text.equals(MainCommands.CHANNEL_HELP.getCode());
    }

    public static boolean isChannelCommand(String text) {
        return text.startsWith(MainCommands.CHANNEL_HELP.getCode());
    }

    public static boolean hasPrivateFlag(String text) {
        return text.contains("--p");
    }

    public static boolean isAskingToLeaveChannel(String text) {
        return text.contains("--leave");
    }

    public static boolean isAskingToAllowChatWorker(String text) {
        return text.contains("--allow");
    }

    public static boolean isAskingToJoinChannel(String text) {
        return text.contains("--join");
    }

    public static boolean isAskingToCreateChannel(String text) {
        return text.contains("--create");
    }

    public static boolean isServerCommand(String text) {
        return text != null && text.startsWith("\\\\");
    }

    public static boolean isExitCommand(String text) {
        return text.endsWith(MainCommands.END_SESSION.getCode());
    }

    public static boolean isFileSending(String text) {
        return text.trim().startsWith("\\\\f");
    }

    public static boolean isAskingForHistory(String text) {
        return text.contains("--history");
    }
}
