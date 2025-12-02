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

package com.github.retrooper.packetevents.protocol.debug.path;

import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

/**
 * @versions 1.21.9+
 */
@NullMarked
public final class DebugNode {

    private final Vector3i pos;
    private final float walkedDistance;
    private final float costMalus;
    private final boolean closed;
    private final DebugPathType type;
    private final float f;

    public DebugNode(
            Vector3i pos, float walkedDistance, float costMalus,
            boolean closed, DebugPathType type, float f
    ) {
        this.pos = pos;
        this.walkedDistance = walkedDistance;
        this.costMalus = costMalus;
        this.closed = closed;
        this.type = type;
        this.f = f;
    }

    public static DebugNode read(PacketWrapper<?> wrapper) {
        Vector3i pos = new Vector3i(wrapper.readInt(), wrapper.readInt(), wrapper.readInt());
        float walkedDistance = wrapper.readFloat();
        float costMalus = wrapper.readFloat();
        boolean closed = wrapper.readBoolean();
        DebugPathType type = wrapper.readEnum(DebugPathType.values());
        float f = wrapper.readFloat();
        return new DebugNode(pos, walkedDistance, costMalus, closed, type, f);
    }

    public static void write(PacketWrapper<?> wrapper, DebugNode node) {
        wrapper.writeInt(node.pos.x);
        wrapper.writeInt(node.pos.y);
        wrapper.writeInt(node.pos.z);
        wrapper.writeFloat(node.walkedDistance);
        wrapper.writeFloat(node.costMalus);
        wrapper.writeBoolean(node.closed);
        wrapper.writeEnum(node.type);
        wrapper.writeFloat(node.f);
    }

    public Vector3i getPos() {
        return this.pos;
    }

    public float getWalkedDistance() {
        return this.walkedDistance;
    }

    public float getCostMalus() {
        return this.costMalus;
    }

    public boolean isClosed() {
        return this.closed;
    }

    public DebugPathType getType() {
        return this.type;
    }

    public float getF() {
        return this.f;
    }
}
