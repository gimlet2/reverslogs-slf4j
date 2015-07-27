package com.restmonkeys.reverslogs.slf4j;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ReversLoggerFactory implements ILoggerFactory {
    ConcurrentMap<String, Logger> loggerMap = new ConcurrentHashMap<String, Logger>();

    public Logger getLogger(String name) {
        Logger logger = loggerMap.get(name);
        if (logger != null) {
            return logger;
        }
        logger = new ReversLogger(LoggerFactory2.getLogger(name));
        Logger oldLogger = loggerMap.putIfAbsent(name, logger);
        return oldLogger == null ? logger : oldLogger;
    }
}
