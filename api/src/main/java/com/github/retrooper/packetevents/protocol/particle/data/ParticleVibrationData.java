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

import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ParticleVibrationData extends ParticleData {

    public enum PositionType implements MappedEntity {

        BLOCK(new ResourceLocation("minecraft:block")),
        ENTITY(new ResourceLocation("minecraft:entity"));

        private final ResourceLocation name;

        PositionType(ResourceLocation name) {
            this.name = name;
        }

        @Override
        public ResourceLocation getName() {
            return name;
        }

        public static PositionType getById(int id) {
            switch (id) {
                case 0x00:
                    return BLOCK;
                case 0x01:
                    return ENTITY;
                default:
                    throw new IllegalArgumentException("Illegal position type id: " + id);
            }
        }

        public static @Nullable PositionType getByName(String name) {
            return getByName(new ResourceLocation(name));
        }

        public static @Nullable PositionType getByName(ResourceLocation name) {
            for (PositionType type : values()) {
                if (type.getName().equals(name)) {
                    return type;
                }
            }
            return null;
        }

        @Override
        public int getId(ClientVersion version) {
            return this.ordinal();
        }
    }

    private Vector3i startingPosition; // Removed in 1.19.4
    private PositionType type;
    private @Nullable Vector3i blockPosition;
    private @Nullable Integer entityId;
    private @Nullable Float entityEyeHeight; // Added in 1.19
    private int ticks;

    public ParticleVibrationData(Vector3i startingPosition, @Nullable Vector3i blockPosition, int ticks) {
        this.startingPosition = startingPosition;
        this.type = PositionType.BLOCK;
        this.blockPosition = blockPosition;
        this.entityId = null;
        this.ticks = ticks;
    }

    public ParticleVibrationData(Vector3i startingPosition, int entityId, int ticks) {
        this.startingPosition = startingPosition;
        this.type = PositionType.ENTITY;
        this.blockPosition = null;
        this.entityId = entityId;
        this.entityEyeHeight = null;
        this.ticks = ticks;
    }

    public ParticleVibrationData(Vector3i startingPosition, int entityId, float entityEyeHeight, int ticks) {
        this.startingPosition = startingPosition;
        this.type = PositionType.ENTITY;
        this.blockPosition = null;
        this.entityId = entityId;
        this.entityEyeHeight = entityEyeHeight;
        this.ticks = ticks;
    }

    public Vector3i getStartingPosition() {
        return startingPosition;
    }

    public void setStartingPosition(Vector3i startingPosition) {
        this.startingPosition = startingPosition;
    }

    public PositionType getType() {
        return type;
    }

    public Optional<Vector3i> getBlockPosition() {
        return Optional.ofNullable(blockPosition);
    }

    public void setBlockPosition(@Nullable Vector3i blockPosition) {
        this.blockPosition = blockPosition;
    }

    public Optional<Integer> getEntityId() {
        return Optional.ofNullable(entityId);
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public Optional<Float> getEntityEyeHeight() {
        return Optional.ofNullable(entityEyeHeight);
    }

    public void setEntityEyeHeight(@Nullable Float entityEyeHeight) {
        this.entityEyeHeight = entityEyeHeight;
    }

    public int getTicks() {
        return ticks;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

    public static ParticleVibrationData read(PacketWrapper<?> wrapper) {
        Vector3i startingPos = Vector3i.zero();

        PositionType positionType;
        positionType = PositionType.getById(wrapper.readVarInt());

        switch (positionType) {
            case BLOCK:
                return new ParticleVibrationData(startingPos, wrapper.readBlockPosition(), wrapper.readVarInt());
            case ENTITY:
                return new ParticleVibrationData(startingPos, wrapper.readVarInt(),
                        wrapper.readFloat(), wrapper.readVarInt());
            default:
                throw new IllegalArgumentException("Illegal position type: " + positionType);
        }
    }

    public static void write(PacketWrapper<?> wrapper, ParticleVibrationData data) {

        wrapper.writeVarInt(data.getType().getId(wrapper.getServerVersion().toClientVersion()));

        if (data.getType() == PositionType.BLOCK) {
            wrapper.writeBlockPosition(data.getBlockPosition().get());
        } else if (data.getType() == PositionType.ENTITY) {
            wrapper.writeVarInt(data.getEntityId().get());
            wrapper.writeFloat(data.getEntityEyeHeight().orElse(0f));
        }
        wrapper.writeVarInt(data.getTicks());
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
