package it.sosinski.manager;

import it.sosinski.chatworker.ChatWorker;

class HelpService {

    static void printAvailableCommands(ChatWorker chatWorker) {
        for (MainCommands value : MainCommands.values()) {
            chatWorker.sendServerMsg(value.toString());
        }
    }

}
