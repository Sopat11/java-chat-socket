package it.sosinski.handler;

import lombok.extern.java.Log;

import java.util.logging.Level;

@Log
public class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.log(Level.SEVERE, e.toString());
        e.printStackTrace();
    }
}
