package it.sosinski.server;

import it.sosinski.chatworker.ChatWorkers;
import it.sosinski.login.ConnectionService;
import it.sosinski.login.LoginService;
import it.sosinski.manager.ManagerService;

import java.util.concurrent.ExecutorService;

public interface ChatServerFactory {

    ChatWorkers createChatWorkers();

    ExecutorService createExecutorService();

    ManagerService createManagerService(LoginService loginService);

    ConnectionService createConnectionService(LoginService loginService);

    LoginService createLoginService(ChatWorkers chatWorkers);
}
