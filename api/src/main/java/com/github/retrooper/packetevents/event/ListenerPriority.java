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

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * The priority of packet listeners affect the order they will be invoked in.
 * The lowest priority listeners are invoked first, the highest are invoked last.
 * The highest priority listener has the final decider on an event being canceled.
 * <p>
 * Predefined priorities are: {@link #LOWEST}, {@link #LOW}, {@link #NORMAL}, {@link #HIGH}, {@link #HIGHEST} and {@link #MONITOR}.
 * <p>
 * Custom integer priorities can be defined using {@link #custom(int)}.
 */
@NullMarked
public final class ListenerPriority implements Comparable<ListenerPriority> {

    private static final int LOWEST_PRIORITY = -5000;
    private static final int LOW_PRIORITY = -2500;
    private static final int NORMAL_PRIORITY = 0;
    private static final int HIGH_PRIORITY = 2500;
    private static final int HIGHEST_PRIORITY = 5000;
    private static final int MONITOR_PRIORITY = Integer.MAX_VALUE;

    /**
     * This listener will be run first and has little say in the outcome of events.
     */
    public static final ListenerPriority LOWEST = new ListenerPriority(LOWEST_PRIORITY);

    /**
     * Listener is of low importance.
     */
    public static final ListenerPriority LOW = new ListenerPriority(LOW_PRIORITY);

    /**
     * The normal listener priority.
     */
    public static final ListenerPriority NORMAL = new ListenerPriority(NORMAL_PRIORITY);

    /**
     * Listener is of high importance.
     */
    public static final ListenerPriority HIGH = new ListenerPriority(HIGH_PRIORITY);

    /**
     * Listener is of critical importance. Use this to decide the final state of packets.
     */
    public static final ListenerPriority HIGHEST = new ListenerPriority(HIGHEST_PRIORITY);

    /**
     * Highest priority. Use this to perform logic based on the outcome of an event.
     */
    public static final ListenerPriority MONITOR = new ListenerPriority(MONITOR_PRIORITY);

    private final int priority;

    private ListenerPriority(int priority) {
        this.priority = priority;
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
                throw new AssertionError();
        }
    }

    @Deprecated
    public PacketListenerPriority legacyType() {
        switch (this.priority) {
            case LOWEST_PRIORITY:
                return PacketListenerPriority.LOWEST;
            case LOW_PRIORITY:
                return PacketListenerPriority.LOW;
            case NORMAL_PRIORITY:
                return PacketListenerPriority.NORMAL;
            case HIGH_PRIORITY:
                return PacketListenerPriority.HIGH;
            case HIGHEST_PRIORITY:
                return PacketListenerPriority.HIGHEST;
            case MONITOR_PRIORITY:
                return PacketListenerPriority.MONITOR;
            default:
                throw new IllegalStateException("Can't convert " + this + " to legacy listener priority");
        }
    }

    @Override
    public int compareTo(ListenerPriority o) {
        return Integer.compare(this.priority, o.priority);
    }

    public int getPriority() {
        return this.priority;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || o.getClass() != ListenerPriority.class) return false;
        return this.priority == ((ListenerPriority) o).priority;
    }

    @Override
    public int hashCode() {
        return this.priority;
    }
}
