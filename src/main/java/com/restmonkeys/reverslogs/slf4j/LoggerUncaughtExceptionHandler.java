package com.restmonkeys.reverslogs.slf4j;

import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public class LoggerUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    static org.slf4j.Logger l = LoggerFactory.getLogger("LoggerUncaughtExceptionHandler");
    static ReversLogger ld;

    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    public LoggerUncaughtExceptionHandler(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        if (uncaughtExceptionHandler != null && !(uncaughtExceptionHandler instanceof LoggerUncaughtExceptionHandler)) {
            this.uncaughtExceptionHandler = uncaughtExceptionHandler;
        }

    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (ld == null) {
            try {
                Field delegate = l.getClass().getDeclaredField("_delegate");
                delegate.setAccessible(true);
                ld = (ReversLogger) delegate.get(l);
            } catch (NoSuchFieldException | IllegalAccessException e1) {
                e1.printStackTrace();// throw smth here
            }
        }
        ld.fallback(e.getMessage(), e);
        if (uncaughtExceptionHandler != null) {
            uncaughtExceptionHandler.uncaughtException(t, e);
        }
    }
}
