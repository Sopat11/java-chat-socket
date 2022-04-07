package it.sosinski.server;

import it.sosinski.channel.ChannelService;
import it.sosinski.channel.Channels;
import it.sosinski.channel.ListChannel;
import it.sosinski.channel.SynchronizedChannels;
import it.sosinski.chatworker.ChatWorkers;
import it.sosinski.chatworker.ListChatWorkers;
import it.sosinski.chatworker.SynchronizedChatWorkersProxy;
import it.sosinski.login.LoginService;
import it.sosinski.manager.ManagerService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BasicChatServerFactory implements ChatServerFactory {

    @Override
    public ChatWorkers createChatWorkers() {
        return new SynchronizedChatWorkersProxy(new ListChatWorkers());
    }

    @Override
    public Channels createChannels() {
        return new SynchronizedChannels(new ListChannel());
    }

    @Override
    public ExecutorService createExecutorService() {
        return Executors.newFixedThreadPool(1024);
    }

    @Override
    public ManagerService createManagerService(LoginService loginService) {
        return new ManagerService(new ChannelService(createChannels(), loginService), loginService);
    }

    @Override
    public LoginService createLoginService(ChatWorkers chatWorkers) {
        return new LoginService(chatWorkers);
    }

}
