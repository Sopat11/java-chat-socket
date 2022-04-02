package it.sosinski.channel;

import lombok.Getter;

@Getter
public enum ChannelCommands {

    PRINT_CHANNELS("available channels", "\\\\c --channels"),
    JOIN_CHANNEL("join a channel", "\\\\c --join \"{CHANNEL_NAME}\""),
    CREATE_PUBLIC_CHANNEL("create public channel", "\\\\c --create \"{CHANNEL_NAME}\""),
    CREATE_PRIVATE_CHANNEL("create private channel", "\\\\c --create --p \"{CHANNEL_NAME}\""),
    PRINT_CHAT_WORKERS("online users", "\\\\c --online"),
    LEAVE_CHANNEL("leave the channel", "\\\\c --leave"),
    PRINT_HISTORY("print history of current channel", "\\\\c --history"),
    SEND_FILE("send a file", "\\\\f \"{FILE_PATH}\"");

    private final String description;
    private final String code;

    ChannelCommands(String description, String code) {
        this.description = description;
        this.code = code;
    }

    @Override
    public String toString() {
        return code + " - " + description;
    }
}
