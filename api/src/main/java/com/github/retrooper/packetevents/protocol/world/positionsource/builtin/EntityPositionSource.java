/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.world.positionsource.builtin;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.world.positionsource.PositionSource;
import com.github.retrooper.packetevents.protocol.world.positionsource.PositionSourceTypes;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class EntityPositionSource extends PositionSource {

    private int entityId;
    private float offsetY;

    public EntityPositionSource(int entityId) {
        this(entityId, 0f);
    }

    public EntityPositionSource(int entityId, float offsetY) {
        super(PositionSourceTypes.ENTITY);
        this.entityId = entityId;
        this.offsetY = offsetY;
    }

    public static EntityPositionSource read(PacketWrapper<?> wrapper) {
        int entityId = wrapper.readVarInt();
        float offsetY = 0f;
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_19)) {
            offsetY = wrapper.readFloat();
        }
        return new EntityPositionSource(entityId, offsetY);
    }

    public static void write(PacketWrapper<?> wrapper, EntityPositionSource source) {
        wrapper.writeVarInt(source.entityId);
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_19)) {
            wrapper.writeFloat(source.offsetY);
        }
    }

    public int getEntityId() {
        return this.entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public float getOffsetY() {
        return this.offsetY;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }
}
