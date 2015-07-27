package org.slf4j.impl;

import com.restmonkeys.reverslogs.slf4j.Log;
import com.restmonkeys.reverslogs.slf4j.LoggerUncaughtExceptionHandler;
import com.restmonkeys.reverslogs.slf4j.ReversLoggerFactory;
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
import java.util.Set;

public class StaticLoggerBinder implements LoggerFactoryBinder {
    private static final StaticLoggerBinder SINGLETON = new StaticLoggerBinder();
    private static final String loggerFactoryClassStr = ReversLoggerFactory.class.getName();
    public static String REQUESTED_API_VERSION = "1.6.99"; // !final
//    private final Set<Method> methods;

    public static final StaticLoggerBinder getSingleton() {
        return SINGLETON;
    }

    private final ILoggerFactory loggerFactory;

    public StaticLoggerBinder() {
        this.loggerFactory = new ReversLoggerFactory();
        Thread.setDefaultUncaughtExceptionHandler(new LoggerUncaughtExceptionHandler(Thread.getDefaultUncaughtExceptionHandler()));
//        Reflections reflections = new Reflections(ConfigurationBuilder.build(new MethodAnnotationsScanner()));
//        methods = reflections.getMethodsAnnotatedWith(Log.class);
//        for (final Method method : methods) {
//
//            ClassPool pool = ClassPool.getDefault();
//            CtClass cc = null;
//            try {
//                cc = pool.get(method.getDeclaringClass().getCanonicalName());
//                CtMethod[] declaredMethods = cc.getDeclaredMethods();
//                for (CtMethod declaredMethod : declaredMethods) {
//                    if (declaredMethod.hasAnnotation(Log.class)) {
//                        Object annotation = declaredMethod.getAnnotation(Log.class);
//                        int lineNumber = declaredMethod.getMethodInfo().getLineNumber(0);
//                    }
//                }
//
//            } catch (NotFoundException e) {
//                e.printStackTrace();
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
    }

    public ILoggerFactory getLoggerFactory() {
        return loggerFactory;
    }

    public String getLoggerFactoryClassStr() {
        return loggerFactoryClassStr;
    }
}
