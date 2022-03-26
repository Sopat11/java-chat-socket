package it.sosinski.manager;

import it.sosinski.channel.ChannelService;
import it.sosinski.chatworker.ChatWorker;

public class ManagerService {

    private final ChannelService channelService;

    public ManagerService(ChannelService channelService) {
        this.channelService = channelService;
    }

    public void process(ChatWorker chatWorker, String text) {
        if (asksForGeneralHelp(text)) {
            HelpService.printAvailableCommands(chatWorker);
        } else if (asksForChannelHelp(text)) {
            channelService.process(chatWorker, text);
        } else {
            chatWorker.sendServerMsg("Not a proper command");
        }
    }

    private boolean asksForGeneralHelp(String text) {
        return text.startsWith(MainCommands.HELP.getCode());
    }

    private boolean asksForChannelHelp(String text) {
        return text.startsWith(MainCommands.CHANNEL_HELP.getCode());
    }
}
