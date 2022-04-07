package it.sosinski.server;

import it.sosinski.channel.Channels;
import it.sosinski.chatworker.ChatWorkers;
import it.sosinski.login.LoginService;
import it.sosinski.manager.ManagerService;

import java.util.concurrent.ExecutorService;

public interface ChatServerFactory {

    ChatWorkers createChatWorkers();

    Channels createChannels();

    ExecutorService createExecutorService();

    ManagerService createManagerService(LoginService loginService);

    LoginService createLoginService(ChatWorkers chatWorkers);
}
