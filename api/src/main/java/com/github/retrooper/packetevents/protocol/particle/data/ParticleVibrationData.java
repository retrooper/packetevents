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

import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ParticleVibrationData extends ParticleData {
    public enum PositionType {
        BLOCK("minecraft:block"), ENTITY("minecraft:entity");

        private final String name;

        PositionType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static PositionType getByName(String name) {
            for(PositionType type : values()) {
                if(type.getName().equals(name)) {
                    return type;
                }
            }
            return null;
        }
    }

    private Vector3i startingPosition;
    private PositionType type;
    private @Nullable Vector3i blockPosition;
    private @Nullable Integer entityId;
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

    public int getTicks() {
        return ticks;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

    public static ParticleVibrationData read(PacketWrapper<?> wrapper) {
        Vector3i startingPos = wrapper.readBlockPosition();
        String positionTypeName = wrapper.readString();
        PositionType positionType = PositionType.getByName(positionTypeName);
        if (positionType == PositionType.BLOCK) {
            return new ParticleVibrationData(startingPos, wrapper.readBlockPosition(), wrapper.readVarInt());
        }
        else if (positionType == PositionType.ENTITY) {
            return new ParticleVibrationData(startingPos, wrapper.readVarInt(), wrapper.readVarInt());
        }
        else {
            throw new IllegalArgumentException("Unknown position type: " + positionTypeName);
        }
    }


    public static void write(PacketWrapper<?> wrapper, ParticleVibrationData data) {
        wrapper.writeBlockPosition(data.getStartingPosition());
        wrapper.writeString(data.getType().getName());
        if (data.getType() == PositionType.BLOCK) {
            wrapper.writeBlockPosition(data.getBlockPosition().get());
        }
        else if (data.getType() == PositionType.ENTITY) {
            wrapper.writeVarInt(data.getEntityId().get());
        }
        wrapper.writeVarInt(data.getTicks());
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
