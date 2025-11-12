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

import com.github.retrooper.packetevents.protocol.debug.path.DebugPath;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

/**
 * @versions 1.21.9+
 */
@NullMarked
public final class DebugPathInfo {

    private final DebugPath path;
    private final float maxNodeDistance;

    public DebugPathInfo(DebugPath path, float maxNodeDistance) {
        this.path = path;
        this.maxNodeDistance = maxNodeDistance;
    }

    public static DebugPathInfo read(PacketWrapper<?> wrapper) {
        DebugPath path = DebugPath.read(wrapper);
        float maxNodeDistance = wrapper.readFloat();
        return new DebugPathInfo(path, maxNodeDistance);
    }

    public static void write(PacketWrapper<?> wrapper, DebugPathInfo info) {
        DebugPath.write(wrapper, info.path);
        wrapper.writeFloat(info.maxNodeDistance);
    }

    public DebugPath getPath() {
        return this.path;
    }

    public float getMaxNodeDistance() {
        return this.maxNodeDistance;
    }
}
