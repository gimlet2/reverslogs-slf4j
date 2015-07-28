package com.restmonkeys.reverslogs.slf4j;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogScope {

    String DEFAULT_LOG_NAME = "DEFAULT_LOG_NAME";

    String name() default DEFAULT_LOG_NAME;

    LogLevel minLevel() default LogLevel.WARN;

    LogLevel fallback() default LogLevel.DEBUG;
}
