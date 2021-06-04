/*
 *
 *  * This file is part of packetevents - https://github.com/retrooper/packetevents
 *  * Copyright (C) 2016-2021 retrooper and contributors
 *  *
 *  * This program is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package io.github.retrooper.packetevents.event.priority;

/**
 * The priority of a packet listener.
 * This priority can be specified in the PacketListenerAbstract constructor.
 * If you don't specify a priority in the constructor, it will use the {@link #NORMAL} priority.
 *
 * @author retrooper
 * @since 1.8
 */
public enum PacketListenerPriority {
    /**
     * The weakest listener priority.
     * The first to be processed(IN THE DYNAMIC EVENT SYSTEM ONLY).
     * Use this if you need to be the first processing the event and
     * need no power in cancelling an event or preventing an event cancellation.
     */
    LOWEST,

    /**
     * A weak listener priority.
     * Second to be processed(IN THE DYNAMIC EVENT SYSTEM ONLY).
     * Use this if you would prefer to be one of the first to process the event,
     * but don't mind if some other listener processes before you.
     */
    LOW,

    /**
     * Default listener priority.
     * Third to be processed(IN THE DYNAMIC EVENT SYSTEM ONLY).
     * Use this if you don't really care/know when you process or just want to
     * be in the middle.
     */
    NORMAL,

    /**
     * Higher than the {@link PacketListenerPriority#NORMAL} listener priority.
     * Fourth to be processed(IN THE DYNAMIC EVENT SYSTEM ONLY).
     * Use this if you want to process before the default event prioritized listeners.
     */
    HIGH,

    /**
     * Second most powerful listener priority.
     * Fifth to be processed(IN THE DYNAMIC EVENT SYSTEM ONLY).
     * Use this if you prefer to be one of the last to process,
     * but don't mind if some other listener really needs to process after you.
     * Also use this if you prefer deciding if the event cancelled or not, but don't
     * mind if some other listener urgently needs to decide over you.
     * {@link PacketListenerPriority#MONITOR} is rarely ever recommended to use.
     */
    HIGHEST,

    /**
     * Most powerful listener priority.
     * Last(Sixth) to be processed(IN THE DYNAMIC EVENT SYSTEM ONLY).
     * Use this if you urgently need to be the last to process
     * or urgently need to be the final decider whether the event has cancelled or not.
     * This is rarely recommended.
     */
    MONITOR;


    public static PacketListenerPriority getByPriorityValue(byte priorityValue) {
        return values()[priorityValue];
    }

    public byte getPriorityValue() {
        return (byte) ordinal();
    }
}
