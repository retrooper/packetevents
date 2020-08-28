/*
 * Copyright (c) 2020 retrooper
 */

package io.github.retrooper.packetevents.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation indicates that something might be unstable.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Beta {
}
