/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2026 retrooper and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.retrooper.packetevents.event;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;

/**
 * Class for defining listener priorities.
 * The priority of packet listeners affect the order they will be invoked in.
 * The lowest priority listeners are invoked first, the highest are invoked last.
 * The highest priority listener has the final decider on an event being cancelled.
 * <p>
 * Users can define custom priorities by using the {@link #custom(int)} method.
 * <p>
 * Predefined priorities are: {@link #LOWEST}, {@link #LOW}, {@link #NORMAL}, {@link #HIGH}, {@link #HIGHEST} and {@link #MONITOR}.
 *
 * @author ieatglu3
 */
@NullMarked
public final class ListenerPriority implements Comparable<ListenerPriority> {

    private final int ordinal;

    private ListenerPriority(int ordinal) {
        this.ordinal = ordinal;
    }

    /**
     * This listener will be run first and has little say in the outcome of events.
     */
    public static final ListenerPriority LOWEST = new ListenerPriority(0);

    /**
     * Listener is of low importance.
     */
    public static final ListenerPriority LOW = new ListenerPriority(1);

    /**
     * The normal listener priority.
     */
    public static final ListenerPriority NORMAL = new ListenerPriority(2);

    /**
     * Listener is of high importance.
     */
    public static final ListenerPriority HIGH = new ListenerPriority(3);

    /**
     * Listener is of critical importance. Use this to decide the final state of packets.
     */
    public static final ListenerPriority HIGHEST = new ListenerPriority(4);

    /**
     * Highest priority. Use this to perform logic based on the outcome of an event.
     */
    public static final ListenerPriority MONITOR = new ListenerPriority(5);

    private static final ListenerPriority[] VALUES = {LOWEST, LOW, NORMAL, HIGH, HIGHEST, MONITOR};

    /**
     * @return the ordinal of this priority. The higher the ordinal, the higher the priority.
     */
    public int getOrdinal() {
        return this.ordinal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || o.getClass() != ListenerPriority.class) return false;
        return this.ordinal == ((ListenerPriority) o).ordinal;
    }

    @Override
    public int hashCode() {
        return this.ordinal;
    }

    @Override
    public int compareTo(@NotNull ListenerPriority o) {
        return Integer.compare(this.ordinal, o.ordinal);
    }

    /**
     * Create a custom listener priority with the specified ordinal. The higher the ordinal, the higher the priority.
     * <p>
     * The predefined priorities have ordinals from 0 to 5. Custom priorities can be negative.
     *
     * @param ordinal the ordinal of this priority
     * @return a new {@link ListenerPriority} with the specified ordinal
     */
    public static ListenerPriority custom(int ordinal) {
        return new ListenerPriority(ordinal);
    }

    /**
     * @return a copy of all the predefined priorities in order of their ordinals.
     */
    public static ListenerPriority[] predefined() {
        return VALUES.clone();
    }

    @Deprecated
    public static ListenerPriority fromLegacy(PacketListenerPriority priority) {
        switch (priority) {
            case LOWEST:
                return LOWEST;
            case LOW:
                return LOW;
            case NORMAL:
                return NORMAL;
            case HIGH:
                return HIGH;
            case HIGHEST:
                return HIGHEST;
            case MONITOR:
                return MONITOR;
            default:
                throw new IllegalArgumentException("unknown legacy priority: " + priority);
        }
    }

    @Deprecated
    public PacketListenerPriority legacyType() {
        switch (this.ordinal) {
            case 0:
                return PacketListenerPriority.LOWEST;
            case 1:
                return PacketListenerPriority.LOW;
            case 2:
                return PacketListenerPriority.NORMAL;
            case 3:
                return PacketListenerPriority.HIGH;
            case 4:
                return PacketListenerPriority.HIGHEST;
            default:
                return PacketListenerPriority.MONITOR;
        }
    }
}
