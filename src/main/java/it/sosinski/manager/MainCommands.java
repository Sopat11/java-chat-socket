package it.sosinski.manager;

public enum MainCommands {

    HELP("display available commands", "\\\\h"),
    CHANNEL_HELP("display channel commands ", "\\\\c"),
    END_SESSION("close application", "\\\\quit");

    private final String description;
    private final String code;

    MainCommands(String description, String code) {
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
