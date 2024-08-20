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
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.protocol.nbt.NBTIntArray;
import com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.world.positionsource.PositionSource;
import com.github.retrooper.packetevents.protocol.world.positionsource.PositionSourceTypes;
import com.github.retrooper.packetevents.util.UniqueIdUtil;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Optional;
import java.util.UUID;

public class EntityPositionSource extends PositionSource {

    private static final UUID EMPTY_UNIQUE_ID = new UUID(0, 0);

    private Optional<UUID> entityUniqueId;
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

    public EntityPositionSource(Optional<UUID> entityUniqueId, float offsetY) {
        super(PositionSourceTypes.ENTITY);
        this.entityUniqueId = entityUniqueId;
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

    public static EntityPositionSource decodeSource(NBTCompound compound, ClientVersion version) {
        if (version.isNewerThanOrEquals(ClientVersion.V_1_19)) {
            int[] entityUniqueIdArr = compound.getTagOfTypeOrThrow("source_entity", NBTIntArray.class).getValue();
            UUID entityUniqueId = UniqueIdUtil.fromIntArray(entityUniqueIdArr);
            NBTNumber offsetYTag = compound.getNumberTagOrNull("y_offset");
            float offsetY = offsetYTag == null ? 0f : offsetYTag.getAsFloat();
            return new EntityPositionSource(Optional.of(entityUniqueId), offsetY);
        }
        int entityId = compound.getNumberTagOrThrow("source_entity_id").getAsInt();
        return new EntityPositionSource(entityId);
    }

    public static void encodeSource(EntityPositionSource source, ClientVersion version, NBTCompound compound) {
        if (version.isNewerThanOrEquals(ClientVersion.V_1_19)) {
            UUID uniqueId = source.entityUniqueId.orElse(EMPTY_UNIQUE_ID);
            compound.setTag("source_entity", new NBTIntArray(UniqueIdUtil.toIntArray(uniqueId)));
            compound.setTag("y_offset", new NBTFloat(source.offsetY));
        } else {
            compound.setTag("source_entity_id", new NBTInt(source.entityId));
        }
    }

    /**
     * Note: Only used when handling particles through registries and version is >= 1.19.
     */
    public Optional<UUID> getEntityUniqueId() {
        return this.entityUniqueId;
    }

    /**
     * Note: Only used when handling particles through registries and version is >= 1.19.
     */
    public void setEntityUniqueId(Optional<UUID> entityUniqueId) {
        this.entityUniqueId = entityUniqueId;
    }

    /**
     * Note: Only used when handling particles through particle packets or version is < 1.19.
     */
    public int getEntityId() {
        return this.entityId;
    }

    /**
     * Note: Only used when handling particles through particle packets or version is < 1.19.
     */
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
