package it.sosinski.manager;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MainCommands {

    HELP("display available commands", "\\\\h"),
    CHANNEL_HELP("display channel commands ", "\\\\c"),
    END_SESSION("close application", "\\\\quit");

    private final String description;
    private final String code;

    @Override
    public String toString() {
        return code + " - " + description;
    }
}
