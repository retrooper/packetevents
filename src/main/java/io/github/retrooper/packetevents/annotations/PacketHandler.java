package io.github.retrooper.packetevents.annotations;

import io.github.retrooper.packetevents.annotations.data.EventSynchronization;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
/**
 * Just like Bukkit's @EventHandler, but for PacketEvents' custom event system.
 * Make sure your class implements the PacketListener interface, and is registered when annotating your method with this annotation!
 */
public @interface PacketHandler {
    EventSynchronization synchronization() default EventSynchronization.NORMAL;
}
