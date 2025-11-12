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

import com.github.retrooper.packetevents.event.PacketReceiveEvent;

/**
 * Every event that supports cancellation should implement this interface.
 * PacketEvents' event system lets the highest priority listener be have the highest priority
 * in deciding whether the event will cancel.
 * This means an event with a lower priority than the higher priority one would not be able to decide.
 * Cancelling the event means the action assosiated with the event will be cancelled.
 * For example, cancelling the {@link PacketReceiveEvent}
 * will prevent minecraft from processing the incoming packet.
 *
 * @author retrooper
 * @see PacketReceiveEvent
 * @since 1.7
 */
public interface CancellableEvent {

    /**
     * This method returns if the event will be cancelled.
     *
     * @return Will the event be cancelled.
     */
    boolean isCancelled();

    /**
     * Cancel or proceed with the event.
     *
     * @param val Is the event cancelled
     */
    void setCancelled(boolean val);
}
