package io.github.retrooper.packetevents.commandsystem;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Command {
    String arguments();
    String description();
    String usageMessage();
    String[] aliases() default "";
}
