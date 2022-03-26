package it.sosinski.channel;

enum ChannelCommands {

    PRINT_CHANNELS("available channels", "\\\\c --channels"),
    JOIN_CHANNEL("join a channel", "\\\\c --join \"{CHANNEL_NAME}\""),
    CREATE_PUBLIC_CHANNEL("create public channel", "\\\\c --create \"{CHANNEL_NAME}\""),
    CREATE_PRIVATE_CHANNEL("create private channel", "\\\\c --create --p \"{CHANNEL_NAME}\""),
    PRINT_CHAT_WORKERS("online users", "\\\\c --online"),
    LEAVE_CHANNEL("leave the channel", "\\\\c --leave");

    private final String description;
    private final String code;

    ChannelCommands(String description, String code) {
        this.description = description;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code + " - " + description;
    }
}
