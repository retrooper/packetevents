/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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

/**
 * The priority of packet listeners affect the order they will be invoked in.
 * The lowest priority listeners are invoked first, the most high ones are invoked last.
 * The most high priority listener has the final decider on an event being cancelled.
 * This priority can be specified in the PacketListenerAbstract constructor.
 * If you don't specify a priority in the constructor, it will use the {@link #NORMAL} priority.
 *
 * @author retrooper
 * @since 1.8
 */
public enum PacketListenerPriority {
    /**
     * This listener will be run first and has little say in the outcome of events.
     */
    LOWEST,

    /**
     * Listener is of low importance.
     */
    LOW,

    /**
     * The normal listener priority.
     * If possible, always pick this.
     * It allows other projects to easily overturn your decisions.
     * Moreover, it is pretty bold to assume that your project should
     * always have the final say.
     */
    NORMAL,

    /**
     * Listener is of high importance.
     */
    HIGH,

    /**
     * Listener is of critical importance. Use this to decide the final state of packets.
     */
    HIGHEST,

    /**
     * Only use this priority if you want to perform logic based on the outcome of an event.
     * Please do not modify packets in this stage.
     */
    MONITOR;

    public static PacketListenerPriority getById(byte id) {
        return values()[id];
    }

    public byte getId() {
        return (byte) ordinal();
    }
}
