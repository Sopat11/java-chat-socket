package it.sosinski.manager;

import it.sosinski.channel.ChannelService;
import it.sosinski.chatworker.ChatWorker;
import it.sosinski.login.LoginService;
import lombok.extern.java.Log;

import java.util.logging.Level;

@Log
public class ManagerService {

    private final ChannelService channelService;
    private final LoginService loginService;

    public ManagerService(ChannelService channelService, LoginService loginService) {
        this.channelService = channelService;
        this.loginService = loginService;
    }

    public String login(ChatWorker chatWorker, String login) {
        String confirmedLogin = loginService.readChatWorkerLogin(chatWorker, login);
        return confirmedLogin;
    }

    public void process(ChatWorker chatWorker, String text) {
        log.log(Level.INFO, "Started processing request in ManagerService");
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
