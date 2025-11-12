/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2025 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.debug.struct;

import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

/**
 * @versions 1.21.9+
 */
@NullMarked
public final class DebugGameEventInfo {

    /**
     * Possible values include all keys of the built-in <code>minecraft:game_event</code> registry.
     */
    private final ResourceLocation event;
    private final Vector3d pos;

    public DebugGameEventInfo(ResourceLocation event, Vector3d pos) {
        this.event = event;
        this.pos = pos;
    }

    public static DebugGameEventInfo read(PacketWrapper<?> wrapper) {
        ResourceLocation event = ResourceLocation.read(wrapper);
        Vector3d pos = Vector3d.read(wrapper);
        return new DebugGameEventInfo(event, pos);
    }

    public static void write(PacketWrapper<?> wrapper, DebugGameEventInfo info) {
        ResourceLocation.write(wrapper, info.event);
        Vector3d.write(wrapper, info.pos);
    }

    public ResourceLocation getEvent() {
        return this.event;
    }

    public Vector3d getPos() {
        return this.pos;
    }
}
