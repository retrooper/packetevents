package com.github.retrooper.packetevents.protocol.world;

public enum Rotation {

    /**
     * No rotation
     */
    NONE,
    /**
     * Rotated clockwise by 45 degrees
     */
    CLOCKWISE_45,
    /**
     * Rotated clockwise by 90 degrees
     */
    CLOCKWISE,
    /**
     * Rotated clockwise by 135 degrees
     */
    CLOCKWISE_135,
    /**
     * Flipped upside-down, a 180-degree rotation
     */
    FLIPPED,
    /**
     * Flipped upside-down + 45-degree rotation
     */
    FLIPPED_45,
    /**
     * Rotated counter-clockwise by 90 degrees
     */
    COUNTER_CLOCKWISE,
    /**
     * Rotated counter-clockwise by 45 degrees
     */
    COUNTER_CLOCKWISE_45;

}