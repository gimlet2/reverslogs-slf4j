package com.restmonkeys.reverslogs.slf4j;

import org.slf4j.LoggerFactory;

public class LoggerUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    static org.slf4j.Logger l = LoggerFactory.getLogger("LoggerUncaughtExceptionHandler");

    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    public LoggerUncaughtExceptionHandler(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        if (uncaughtExceptionHandler != null && !(uncaughtExceptionHandler instanceof LoggerUncaughtExceptionHandler)) {
            this.uncaughtExceptionHandler = uncaughtExceptionHandler;
        }
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (l instanceof ReversLogger) {
            ReversLogger l2 = (ReversLogger) l;
            l2.fallback(e.getMessage(), e);
        }
        if (uncaughtExceptionHandler != null) {
            uncaughtExceptionHandler.uncaughtException(t, e);
        }
    }
}
