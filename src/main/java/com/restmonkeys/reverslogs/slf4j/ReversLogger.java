package com.restmonkeys.reverslogs.slf4j;

import com.restmonkeys.reverslogs.slf4j.collections.LimitedSizeList;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.spi.LocationAwareLogger;

import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class ReversLogger extends MarkerIgnoringBase implements LocationAwareLogger {

    private Logger logger;

    private static final List<LogEntity> GLOBAL_LOG_CACHE = new LimitedSizeList<>();
    public static final String REVERSLOG_MINLEVEL_PROPERNY_NAME = "reverslog.minlevel";
    public static final String REVERSLOG_FALLBACKLEVEL_PROPERNY_NAME = "reverslog.fallbacklevel";
    private static final LogLevel minLevel = LogLevel.valueOf(ResourceBundle.getBundle("reverslog").getString(ReversLogger.REVERSLOG_MINLEVEL_PROPERNY_NAME));
    private static final LogLevel fallbackLevel = LogLevel.valueOf(ResourceBundle.getBundle("reverslog").getString(ReversLogger.REVERSLOG_FALLBACKLEVEL_PROPERNY_NAME));

    public ReversLogger(Logger logger) {
        this.logger = logger;
    }

    public boolean isTraceEnabled() {
        return minLevel.compare(LogLevel.TRACE) <= 0;
    }

    public void trace(String msg) {
        log(LogLevel.TRACE, () -> logger.trace(msg));
    }

    public void trace(String format, Object arg) {
        log(LogLevel.TRACE, () -> logger.trace(format, arg));
    }


    public void trace(String format, Object arg1, Object arg2) {
        log(LogLevel.TRACE, () -> logger.trace(format, arg1, arg2));
    }

    public void trace(String format, Object... arguments) {
        log(LogLevel.TRACE, () -> logger.trace(format, arguments));
    }

    public void trace(String msg, Throwable t) {
        log(LogLevel.TRACE, () -> logger.trace(msg, t));
    }

    public boolean isDebugEnabled() {
        return minLevel.compare(LogLevel.DEBUG) <= 0;

    }

    public void debug(String msg) {
        log(LogLevel.DEBUG, () -> logger.debug(msg));
    }

    public void debug(String format, Object arg) {
        log(LogLevel.DEBUG, () -> logger.debug(format, arg));
    }

    public void debug(String format, Object arg1, Object arg2) {
        log(LogLevel.DEBUG, () -> logger.debug(format, arg1, arg2));
    }

    public void debug(String format, Object... arguments) {
        log(LogLevel.DEBUG, () -> logger.debug(format, arguments));
    }

    public void debug(String msg, Throwable t) {
        log(LogLevel.DEBUG, () -> logger.debug(msg, t));
    }

    public boolean isInfoEnabled() {
        return minLevel.compare(LogLevel.INFO) <= 0;

    }

    public void info(String msg) {
        log(LogLevel.INFO, () -> logger.info(msg));
    }

    public void info(String format, Object arg) {
        log(LogLevel.INFO, () -> logger.info(format, arg));
    }

    public void info(String format, Object arg1, Object arg2) {
        log(LogLevel.INFO, () -> logger.info(format, arg1, arg2));
    }

    public void info(String format, Object... arguments) {
        log(LogLevel.INFO, () -> logger.info(format, arguments));
    }

    public void info(String msg, Throwable t) {
        log(LogLevel.INFO, () -> logger.info(msg, t));
    }

    public boolean isWarnEnabled() {
        return minLevel.compare(LogLevel.WARN) <= 0;
    }

    public void warn(String msg) {
        log(LogLevel.WARN, () -> logger.warn(msg));
    }

    public void warn(String format, Object arg) {
        log(LogLevel.WARN, () -> logger.warn(format, arg));
    }

    public void warn(String format, Object... arguments) {
        log(LogLevel.WARN, () -> logger.warn(format, arguments));
    }

    public void warn(String format, Object arg1, Object arg2) {
        log(LogLevel.WARN, () -> logger.warn(format, arg1, arg2));
    }

    public void warn(String msg, Throwable t) {
        log(LogLevel.WARN, () -> logger.warn(msg, t));
    }

    public boolean isErrorEnabled() {
        return minLevel.compare(LogLevel.ERROR) <= 0;
    }

    public void error(String msg) {
        log(LogLevel.ERROR, () -> logger.error(msg));
    }

    public void error(String format, Object arg) {
        log(LogLevel.ERROR, () -> logger.error(format, arg));
    }

    public void error(String format, Object arg1, Object arg2) {
        log(LogLevel.ERROR, () -> logger.error(format, arg1, arg2));
    }

    public void error(String format, Object... arguments) {
        log(LogLevel.ERROR, () -> logger.error(format, arguments));
    }

    public void error(String msg, Throwable t) {
        log(LogLevel.ERROR, () -> logger.error(msg, t));
    }

    public void fallback(String msg, Throwable t) {
        GLOBAL_LOG_CACHE.add(new LogEntity(LogLevel.FALLBACK, () -> logger.error(msg, t)));
        Stream<LogEntity> logEntityStream = GLOBAL_LOG_CACHE.stream().filter((e) -> e.level.compare(fallbackLevel) <= 0);
        logEntityStream.forEach((l) -> l.logStatement.run());
        GLOBAL_LOG_CACHE.clear();
    }

    public void log(Marker marker, String fqcn, int level, String message, Object[] argArray, Throwable t) {

    }

    private VoidPredicate predicate(LogLevel logLevel) {
        switch (logLevel) {
            case TRACE:
                return this::isTraceEnabled;
            case DEBUG:
                return this::isDebugEnabled;
            case INFO:
                return this::isInfoEnabled;
            case WARN:
                return this::isWarnEnabled;
            case ERROR:
                return this::isErrorEnabled;
        }
        return () -> true;
    }

    private void log(LogLevel l, Runnable r) {
        if (predicate(l).test()) {
            r.run();
        } else {
            GLOBAL_LOG_CACHE.add(new LogEntity(l, r));
        }
    }

    public static class LogEntity {
        private String message;
        private LogLevel level;
        private Throwable throwable;
        private Runnable logStatement;

        public LogEntity(LogLevel level, Runnable logStatement) {
            this.level = level;
            this.logStatement = logStatement;
        }

        public LogEntity(LogLevel level, String message) {
            this.message = message;
            this.level = level;
        }

        public LogEntity(LogLevel level, String message, Throwable throwable) {
            this.message = message;
            this.level = level;
            this.throwable = throwable;
        }

        @Override
        public String toString() {
            return level.name() + " " + message;
        }
    }
}
