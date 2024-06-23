package dev.revere.delta.api.command.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Remi
 * @project Delta
 * @date 6/17/2024
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

    String name();

    String permission() default "";

    String[] aliases() default {};

    String description() default "";

    String usage() default "";

    boolean inGameOnly() default true;
}
