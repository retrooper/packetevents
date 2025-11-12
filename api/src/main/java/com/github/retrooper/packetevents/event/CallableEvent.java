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

import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;

/**
 * The {@link PacketEvent} implements this interface.
 * Every inbuilt event should implement the {@link #call(PacketListenerCommon)} method.
 * If you are making a custom event, don't implement this.
 * The {@link PacketListenerCommon#onPacketEventExternal(PacketEvent)} method is called for every event that is not in-built.
 * including custom events.
 *
 * @author retrooper
 * @since 1.8
 */
public interface CallableEvent {
    default void call(PacketListenerCommon listener) {
        listener.onPacketEventExternal((PacketEvent) this);
    }
}
