package org.slf4j.impl;

import com.restmonkeys.reverslogs.slf4j.LogScope;
import com.restmonkeys.reverslogs.slf4j.LoggerUncaughtExceptionHandler;
import com.restmonkeys.reverslogs.slf4j.ReversLoggerFactory;
import com.restmonkeys.reverslogs.slf4j.collections.Pair;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.ILoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

public class StaticLoggerBinder implements LoggerFactoryBinder {
    public static final Map<Pair<String, Integer>, LogScope> logScopes = new HashMap<>();
    private static final StaticLoggerBinder SINGLETON = new StaticLoggerBinder();
    private static final String loggerFactoryClassStr = ReversLoggerFactory.class.getName();
    public static String REQUESTED_API_VERSION = "1.6.99"; // !final
    private final ILoggerFactory loggerFactory;

    public StaticLoggerBinder() {
        this.loggerFactory = new ReversLoggerFactory();
        Thread.setDefaultUncaughtExceptionHandler(new LoggerUncaughtExceptionHandler(Thread.getDefaultUncaughtExceptionHandler()));
        ResourceBundle resourceBundle = ResourceBundle.getBundle("reverslog");
        if (resourceBundle.containsKey("reverslog.simpleMode") && !resourceBundle.getString("reverslog.simpleMode").equalsIgnoreCase("true")) {
            loadLoggerScopes();
        }
    }

    public static final StaticLoggerBinder getSingleton() {
        return SINGLETON;
    }

    private void loadLoggerScopes() {
        Reflections reflections = new Reflections(ConfigurationBuilder.build(new MethodAnnotationsScanner()));
        Set<Method> methods = reflections.getMethodsAnnotatedWith(LogScope.class);
        for (final Method method : methods) {

            ClassPool pool = ClassPool.getDefault();
            CtClass cc;
            try {
                cc = pool.get(method.getDeclaringClass().getCanonicalName());
                CtMethod[] declaredMethods = cc.getDeclaredMethods();
                for (CtMethod declaredMethod : declaredMethods) {
                    if (declaredMethod.hasAnnotation(LogScope.class)) {
                        LogScope annotation = (LogScope) declaredMethod.getAnnotation(LogScope.class);
                        int lineNumber = declaredMethod.getMethodInfo().getLineNumber(0);
                        logScopes.put(new Pair<>(method.getName(), lineNumber), annotation);
                    }
                }
            } catch (NotFoundException | ClassNotFoundException e) {
                e.printStackTrace(); // throw smth here
            }
        }
    }

    public ILoggerFactory getLoggerFactory() {
        return loggerFactory;
    }

    public String getLoggerFactoryClassStr() {
        return loggerFactoryClassStr;
    }
}
