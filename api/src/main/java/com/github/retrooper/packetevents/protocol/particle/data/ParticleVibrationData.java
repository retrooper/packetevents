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

package com.github.retrooper.packetevents.protocol.particle.data;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.protocol.nbt.NBTIntArray;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.world.positionsource.PositionSource;
import com.github.retrooper.packetevents.protocol.world.positionsource.PositionSourceType;
import com.github.retrooper.packetevents.protocol.world.positionsource.PositionSourceTypes;
import com.github.retrooper.packetevents.protocol.world.positionsource.builtin.BlockPositionSource;
import com.github.retrooper.packetevents.protocol.world.positionsource.builtin.EntityPositionSource;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ParticleVibrationData extends ParticleData {

    @Deprecated
    public enum PositionType implements MappedEntity {

        BLOCK(PositionSourceTypes.BLOCK),
        ENTITY(PositionSourceTypes.ENTITY);

        private final PositionSourceType<?> type;

        PositionType(PositionSourceType<?> type) {
            this.type = type;
        }

        @Contract("null -> null; !null -> !null")
        public static @Nullable PositionType byModern(@Nullable PositionSourceType<?> type) {
            if (type == null) {
                return null;
            }
            for (PositionType legacyType : values()) {
                if (legacyType.type == type) {
                    return legacyType;
                }
            }
            throw new UnsupportedOperationException("Unsupported modern type: " + type.getName());
        }

        public static PositionType getById(int id) {
            ClientVersion version = PacketEvents.getAPI().getServerManager().getVersion().toClientVersion();
            return byModern(PositionSourceTypes.getById(version, id));
        }

        public static @Nullable PositionType getByName(String name) {
            return getByName(new ResourceLocation(name));
        }

        public static @Nullable PositionType getByName(ResourceLocation name) {
            return byModern(PositionSourceTypes.getByName(name.toString()));
        }

        @Override
        public ResourceLocation getName() {
            return this.type.getName();
        }

        @Override
        public int getId(ClientVersion version) {
            return this.type.getId(version);
        }
    }

    private Vector3i startingPosition; // Removed in 1.19.4
    private PositionSource source;
    private int ticks;

    public ParticleVibrationData(@Nullable Vector3i startingPos, Vector3i blockPosition, int ticks) {
        this(startingPos, new BlockPositionSource(blockPosition), ticks);
    }

    public ParticleVibrationData(@Nullable Vector3i startingPos, int entityId, int ticks) {
        this(startingPos, new EntityPositionSource(entityId), ticks);
    }

    public ParticleVibrationData(@Nullable Vector3i startingPos, int entityId, float entityEyeHeight, int ticks) {
        this(startingPos, new EntityPositionSource(entityId, entityEyeHeight), ticks);
    }

    public ParticleVibrationData(PositionSource source, int ticks) {
        this(null, source, ticks);
    }

    public ParticleVibrationData(@Nullable Vector3i startingPos, PositionSource source, int ticks) {
        this.startingPosition = startingPos;
        this.source = source;
        this.ticks = ticks;
    }

    public static ParticleVibrationData read(PacketWrapper<?> wrapper) {
        Vector3i startingPos = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_19_4)
                ? Vector3i.zero() : wrapper.readBlockPosition();

        PositionSourceType<?> sourceType;
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20_3)) {
            sourceType = wrapper.readMappedEntity(PositionSourceTypes::getById);
        } else {
            String sourceTypeName = wrapper.readString();
            sourceType = PositionSourceTypes.getByName(sourceTypeName);
            if (sourceType == null) {
                throw new IllegalArgumentException("Illegal position type: " + sourceTypeName);
            }
        }

        PositionSource source = sourceType.read(wrapper);
        int ticks = wrapper.readVarInt();
        return new ParticleVibrationData(startingPos, source, ticks);
    }

    @SuppressWarnings("unchecked")
    public static void write(PacketWrapper<?> wrapper, ParticleVibrationData data) {
        if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_19_4)) {
            wrapper.writeBlockPosition(data.getStartingPosition());
        }

        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20_3)) {
            wrapper.writeMappedEntity(data.getSourceType());
        } else {
            wrapper.writeIdentifier(data.getType().getName());
        }

        PositionSourceType<PositionSource> sourceType =
                (PositionSourceType<PositionSource>) data.getSourceType();
        sourceType.write(wrapper, data.getSource());

        wrapper.writeVarInt(data.getTicks());
    }

    public static ParticleVibrationData decode(NBTCompound compound, ClientVersion version) {
        Vector3i origin = version.isNewerThanOrEquals(ClientVersion.V_1_19) ? null :
                new Vector3i(compound.getTagOfTypeOrThrow("origin", NBTIntArray.class).getValue());
        PositionSource destination = PositionSource.decode(compound.getCompoundTagOrThrow("destination"), version);
        int arrivalInTicks = compound.getNumberTagOrThrow("arrival_in_ticks").getAsInt();
        return new ParticleVibrationData(origin, destination, arrivalInTicks);
    }

    public static void encode(ParticleVibrationData data, ClientVersion version, NBTCompound compound) {
        if (version.isOlderThan(ClientVersion.V_1_19)) {
            Vector3i startPos = data.getStartingPosition();
            if (startPos != null) {
                compound.setTag("origin", new NBTIntArray(
                        new int[]{startPos.x, startPos.y, startPos.z}));
            }
        }
        compound.setTag("destination", PositionSource.encode(data.source, version));
        compound.setTag("arrival_in_ticks", new NBTInt(data.ticks));
    }

    @ApiStatus.Obsolete
    public Vector3i getStartingPosition() {
        return this.startingPosition;
    }

    @ApiStatus.Obsolete
    public void setStartingPosition(Vector3i startingPosition) {
        this.startingPosition = startingPosition;
    }

    @Deprecated
    public PositionType getType() {
        return PositionType.byModern(this.source.getType());
    }

    public PositionSourceType<?> getSourceType() {
        return this.source.getType();
    }

    public PositionSource getSource() {
        return this.source;
    }

    public Optional<Vector3i> getBlockPosition() {
        if (this.source instanceof BlockPositionSource) {
            return Optional.of(((BlockPositionSource) this.source).getPos());
        }
        return Optional.empty();
    }

    public void setBlockPosition(Vector3i blockPosition) {
        this.source = new BlockPositionSource(blockPosition);
    }

    public Optional<Integer> getEntityId() {
        if (this.source instanceof EntityPositionSource) {
            return Optional.of(((EntityPositionSource) this.source).getEntityId());
        }
        return Optional.empty();
    }

    public void setEntityId(int entityId) {
        float offsetY = this.getEntityEyeHeight().orElse(0f);
        this.source = new EntityPositionSource(entityId, offsetY);
    }

    public Optional<Float> getEntityEyeHeight() {
        if (this.source instanceof EntityPositionSource) {
            return Optional.of(((EntityPositionSource) this.source).getOffsetY());
        }
        return Optional.empty();
    }

    public void setEntityEyeHeight(Float offsetY) {
        this.setEntityEyeHeight(offsetY == null ? 0f : offsetY);
    }

    public void setEntityEyeHeight(float offsetY) {
        int entityId = this.getEntityId().orElse(0);
        this.source = new EntityPositionSource(entityId, offsetY);
    }

    public int getTicks() {
        return this.ticks;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
