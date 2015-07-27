package com.restmonkeys.reverslogs.slf4j;

import org.slf4j.MarkerFactory;

public enum LogLevel {
    TRACE(1), DEBUG(2), INFO(3), WARN(4), ERROR(5), FALLBACK(100);
    private int level;

    LogLevel(int level) {
        this.level = level;
    }

    public int compare(LogLevel l) {
        if (this.level == l.level) return 0;
        if (this.level > l.level) return 1;
        return -1;
    }

    public void log(org.slf4j.Logger logger, ReversLogger.LogEntity logEntity, Throwable e) {
        switch (this) {
            case INFO:
                if (e != null) {
                    logger.info(logEntity.toString(), e);
                } else {
                    logger.info(logEntity.toString());
                }
                break;
            case DEBUG:
                if (e != null) {
                    logger.debug(logEntity.toString(), e);
                } else {
                    logger.debug(logEntity.toString());
                }
                break;
            case WARN:
                if (e != null) {
                    logger.warn(logEntity.toString(), e);
                } else {
                    logger.warn(logEntity.toString());
                }
                break;
            case ERROR:
                if (e != null) {
                    logger.error(logEntity.toString(), e);
                } else {
                    logger.error(logEntity.toString());
                }
                break;
            case FALLBACK:
                logger.error(MarkerFactory.getMarker("FALLBACK"), logEntity.toString());
                break;
        }
    }
}
