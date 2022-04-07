package it.sosinski.channel;

import it.sosinski.chatworker.ChatWorker;
import it.sosinski.history.HistoryService;
import it.sosinski.login.LoginService;
import it.sosinski.utils.CommandUtils;
import it.sosinski.utils.TextUtils;

import java.util.List;

public class ChannelService {

    private final Object createMonitor = new Object();
    private final Object allowMonitor = new Object();
    private final Channels channels;
    private final LoginService loginService;

    public ChannelService(Channels channels, LoginService loginService) {
        this.channels = channels;
        this.loginService = loginService;
    }

    public void process(ChatWorker chatWorker, String text) {
        if (CommandUtils.isAskingForChannelHelp(text)) {
            printAvailableCommands(chatWorker);

        } else if (CommandUtils.isAskingForHistory(text)) {
            printHistory(chatWorker);

        } else if (CommandUtils.isAskingToPrintChannels(text)) {
            printChannelList(chatWorker);

        } else if (CommandUtils.isAskingToJoinChannel(text)) {
            joinChannel(chatWorker, text);

        } else if (CommandUtils.isAskingToLeaveChannel(text)) {
            chatWorker.leaveCurrentChannel();

        } else if (CommandUtils.isAskingToCreateChannel(text)) {
            Channel createdChannel = createChannel(chatWorker, text);
            leaveCurrentChannelIfExists(chatWorker);
            chatWorker.setCurrentChannel(createdChannel);

            if (chatWorker.getCurrentChannel() != null) {
                chatWorker.sendServerMsg("Server created and you're logged in");
            }

        } else if (CommandUtils.isAskingToAllowChatWorker(text)) {
            allowChatWorkerToChannel(chatWorker, text);

        } else if (CommandUtils.isAskingForLoggedChatWorkers(text)) {

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
        if (!TextUtils.hasTwoParentheses(text)) {
            chatWorker.sendServerMsg("No such command");
            return;
        }

        String name = TextUtils.getTextFromParentheses(text);
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
        if (!TextUtils.hasTwoParentheses(text)) {
            chatWorker.sendServerMsg("No such command");
            return null;
        }

        String channelName = TextUtils.getTextFromParentheses(text);

        boolean shouldBePrivate = CommandUtils.hasPrivateFlag(text);
        Channel channel = null;

        synchronized (createMonitor) {
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

        if (!TextUtils.hasTwoParentheses(text)) {
            chatWorker.sendServerMsg("No such command");
            return;
        }

        String login = TextUtils.getTextFromParentheses(text);

        if (loginService.isLoginFree(login)) {
            chatWorker.sendServerMsg("User with given login doesn't exist");
            return;
        }

        ChatWorker chatWorkerToAllow = loginService.getChatWorkerByLogin(login);

        synchronized (allowMonitor){
            if (currentChannel.isAllowed(chatWorkerToAllow)) {
                chatWorker.sendServerMsg("User is already allowed to the channel");
                return;
            }

            currentChannel.allow(chatWorkerToAllow);
            chatWorker.sendServerMsg("User allowed to the channel");
        }
    }

    private void leaveCurrentChannelIfExists(ChatWorker chatWorker) {
        chatWorker.leaveCurrentChannel();
    }

    private void printHistory(ChatWorker chatWorker) {
        HistoryService.readHistory(chatWorker);
    }

    private void printAvailableCommands(ChatWorker chatWorker) {
        for (ChannelCommands command : ChannelCommands.values()) {
            chatWorker.sendServerMsg(command.toString());
        }
    }

    private void printChannelList(ChatWorker chatWorker) {
        if (channels.getSize() == 0) {
            chatWorker.sendServerMsg("No available channels");
        } else {
            channels.getChannels().forEach(ch -> chatWorker.sendServerMsg(ch.toString()));
        }
    }

    private void printLoggedChatWorkers(ChatWorker chatWorker) {
        Channel currentChannel = chatWorker.getCurrentChannel();
        List<ChatWorker> loggedChatWorkers = currentChannel.getLoggedChatWorkers();
        for (ChatWorker loggedChatWorker : loggedChatWorkers) {
            chatWorker.sendServerMsg(loggedChatWorker.getLogin());
        }
    }

    private boolean isInChannel(ChatWorker chatWorker) {
        return chatWorker.getCurrentChannel() != null;
    }

    private boolean isInPrivateChannel(ChatWorker chatWorker) {
        return chatWorker.getCurrentChannel().isPrivate();
    }

    private Channel getChannel(String channelName) {
        return channels.get(channelName);
    }

    private boolean channelExists(String channelName) {
        return channels.exists(channelName);
    }

}
