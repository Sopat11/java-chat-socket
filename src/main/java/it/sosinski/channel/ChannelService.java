package it.sosinski.channel;

import it.sosinski.chatworker.ChatWorker;
import it.sosinski.login.LoginService;
import it.sosinski.manager.MainCommands;

import java.util.ArrayList;
import java.util.List;

public class ChannelService {

    private final List<Channel> channels = new ArrayList<>();
    private final LoginService loginService;

    public ChannelService(LoginService loginService) {
        this.loginService = loginService;
    }

    public void process(ChatWorker chatWorker, String text) {
        if (askForHelp(text)) {
            printAvailableCommands(chatWorker);

        } else if (askToPrintChannels(text)) {
            printChannelList(chatWorker);

        } else if (askToJoinChannel(text)) {
            joinChannel(chatWorker, text);

        } else if (askToLeaveChannel(text)) {
            chatWorker.leaveCurrentChannel();

        } else if (askToCreateChannel(text)) {
            Channel createdChannel = createChannel(chatWorker, text);
            chatWorker.setCurrentChannel(createdChannel);
            chatWorker.sendServerMsg("Server created and you're logged in");

        } else if (askToAllowChatWorkerToChannel(text)) {
            allowChatWorkerToChannel(chatWorker, text);

        } else if (askForLoggedChatWorkers(text)) {
            if (isInChannel(chatWorker)) {
                printLoggedChatWorkers(chatWorker);
            } else {
                chatWorker.sendServerMsg("You need to connect to a channel");
            }
        } else {

            chatWorker.sendServerMsg("No such command");
        }
    }

    private void joinChannel(ChatWorker chatWorker, String text) {
        String name = getTextFromParentheses(text);
        if (!channelExists(name)) {
            chatWorker.sendServerMsg("No such channel");
            return;
        }
        Channel channel = getChannel(name);
        if (channel.isPrivate() && !channel.isAllowed(chatWorker)) {
            chatWorker.sendServerMsg("You're not allowed to this channel!");
            return;
        }
        channel.connectUser(chatWorker);
        chatWorker.setCurrentChannel(channel);
        chatWorker.sendServerMsg("Connected to channel: " + name);
    }

    private Channel createChannel(ChatWorker chatWorker, String text) {
        String channelName = getTextFromParentheses(text);
        boolean shouldBePrivate = isPrivateFlag(text);
        Channel channel = null;

        synchronized (this) {
            if (!channelExists(channelName)) {
                channel = new Channel(chatWorker, channelName, shouldBePrivate);
                channels.add(channel);
            } else {
                chatWorker.sendServerMsg("Server with given name already exists");
            }
        }
        return channel;
    }

    private void allowChatWorkerToChannel(ChatWorker chatWorker, String text) {

        if (!isInChannel(chatWorker)) {
            chatWorker.sendServerMsg("You need to connect a channel");
            return;
        }

        if (!isInPrivateChannel(chatWorker)) {
            chatWorker.sendServerMsg("You need to be in a private channel");
            return;
        }

        Channel currentChannel = chatWorker.getCurrentChannel();
        String login = getTextFromParentheses(text);

        if (!loginService.isLoginFree(login)) {
            chatWorker.sendServerMsg("User with given login doesn't exist");
            return;
        }

        ChatWorker chatWorkerToAllow = loginService.getChatWorkerByLogin(login);

        if (currentChannel.isAllowed(chatWorkerToAllow)) {
            chatWorker.sendServerMsg("User is already allowed to the channel");
            return;
        }

        currentChannel.allow(chatWorkerToAllow);
        chatWorker.sendServerMsg("User allowed to the channel");
    }

    private void printAvailableCommands(ChatWorker chatWorker) {
        for (ChannelCommands command : ChannelCommands.values()) {
            chatWorker.sendServerMsg(command.toString());
        }
    }

    private void printChannelList(ChatWorker chatWorker) {
        if (channels.size() == 0) {
            chatWorker.sendServerMsg("No available channels");
        } else {
            channels.forEach(ch -> chatWorker.sendServerMsg(ch.toString()));
        }
    }

    private void printLoggedChatWorkers(ChatWorker chatWorker) {
        Channel currentChannel = chatWorker.getCurrentChannel();
        List<ChatWorker> loggedChatWorkers = currentChannel.getLoggedChatWorkers();
        for (ChatWorker loggedChatWorker : loggedChatWorkers) {
            chatWorker.sendServerMsg(loggedChatWorker.getLogin());
        }
    }

    private boolean isPrivateFlag(String text) {
        return text.contains("--p");
    }

    private boolean isInChannel(ChatWorker chatWorker) {
        return chatWorker.getCurrentChannel() != null;
    }

    private boolean isInPrivateChannel(ChatWorker chatWorker) {
        return chatWorker.getCurrentChannel().isPrivate();
    }

    private boolean askForLoggedChatWorkers(String text) {
        return text.contains("--online");
    }

    private boolean askToLeaveChannel(String text) {
        return text.contains("--leave");
    }

    private boolean askToJoinChannel(String text) {
        return text.contains("--join");
    }

    private boolean askToCreateChannel(String text) {
        return text.contains("--create");
    }

    private boolean askToPrintChannels(String text) {
        return text.equals(ChannelCommands.PRINT_CHANNELS.getCode());
    }

    private boolean askToAllowChatWorkerToChannel(String text) {
        return text.contains("--allow");
    }

    private boolean askForHelp(String text) {
        return text.equals(MainCommands.CHANNEL_HELP.getCode());
    }

    private String getTextFromParentheses(String text) {
        return text.substring(text.indexOf("\"") + 1, text.lastIndexOf("\""));
    }

    private Channel getChannel(String channelName) {
        return channels.stream().filter(x -> x.getName().equals(channelName)).findFirst().get();
    }

    private boolean channelExists(String channelName) {
        return channels.stream().anyMatch(x -> x.getName().equals(channelName));
    }

}
