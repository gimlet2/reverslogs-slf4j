package org.slf4j.impl;

import com.restmonkeys.reverslogs.slf4j.Log;
import com.restmonkeys.reverslogs.slf4j.LogLevel;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import org.junit.Test;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Set;

public class StaticLoggerBinderTest {

    @Log(minLevel = LogLevel.DEBUG, name = "aaa", fallback = LogLevel.INFO)
    public int a() {
        return 0;
    }

    @Log(minLevel = LogLevel.DEBUG, name = "aaa", fallback = LogLevel.INFO)
    public int a(int b) {
        return b;
    }

    @Log(minLevel = LogLevel.DEBUG, name = "aaa", fallback = LogLevel.INFO)
    public int a(String b, int c) {
        return 1;
    }

    @Test
    public void aa() {
        Reflections reflections = new Reflections(ConfigurationBuilder.build(new MethodAnnotationsScanner()));
        Set<Method> methods = reflections.getMethodsAnnotatedWith(Log.class);
            ClassPool pool = ClassPool.getDefault();
        for (final Method method : methods) {

            CtClass cc = null;
            try {
                cc = pool.get(method.getDeclaringClass().getCanonicalName());
                CtClass[] params = new CtClass[method.getParameterCount()];
                for (int i = 0; i < method.getParameterCount(); i++) {
                    Parameter parameter = method.getParameters()[i];
                    params[i] = new CtClass(parameter.getType().getCanonicalName()) {
                    };
                }
                CtMethod methodX = cc.getDeclaredMethod(method.getName());
                methodX.getMethodInfo().getLineNumber(0);
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}