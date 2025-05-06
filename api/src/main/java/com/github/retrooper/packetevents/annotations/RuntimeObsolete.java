package com.github.retrooper.packetevents.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.Documented;

/**
 * Marks an API element as obsolete and makes this information available
 * at runtime via reflection. Use in conjunction with an IDE-recognized
 * annotation like @ApiStatus.Obsolete if IDE warnings are also desired.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD}) // Adjust targets as needed
public @interface RuntimeObsolete {
    /**
     * Specifies in which version the API became obsolete.
     * Should generally match the value in the corresponding @ApiStatus.Obsolete annotation.
     */
    String since() default "";

    /**
     * Optional: Add a reason or replacement suggestion.
     */
    String reason() default "";
}