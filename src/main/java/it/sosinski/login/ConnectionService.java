package it.sosinski.login;

import lombok.extern.java.Log;

@Log
public class ConnectionService {

    private final LoginService loginService;

    public ConnectionService(LoginService loginService) {
        this.loginService = loginService;
    }

//    @SneakyThrows // TODO: Przechwycić wyjątek
//    public String connectNewChatWorker(ChatWorker chatWorker) {
//        return readChatWorkerLogin(chatWorker);
//    }
//
//    private String readChatWorkerLogin(ChatWorker chatWorker) {
//
//        chatWorker.sendServerMsg("What's your login?");
//
//        String login = "";
//        boolean isFree;
//        boolean isCorrect;
//
//        do {
//            chatWorker.
//            login = reader.readLine().trim();
//            isFree = !loginService.isLoginTaken(login);
//            isCorrect = isLoginCorrect(login);
//
//            if (!isFree) {
//                messageWriter.writeMessage("Unfortunately this login is taken. Choose another one.", "Server");
//            } else if (!isCorrect) {
//                messageWriter.writeMessage("Login can't start with \\. Choose another one.", "Server");
//            }
//        } while (!isFree || !isCorrect);
//
//        return login;
//    }
//
//    private boolean isLoginCorrect(String login) {
//        return !login.trim().startsWith("\\");
//    }

}
