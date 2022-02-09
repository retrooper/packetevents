/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
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

package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

public class WrapperPlayServerFacePlayer extends PacketWrapper<WrapperPlayServerFacePlayer> {
    private EntitySection aimUnit;
    private Vector3d targetPosition;
    @Nullable
    private TargetEntity targetEntity;

    public WrapperPlayServerFacePlayer(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerFacePlayer(EntitySection aimUnit, Vector3d targetPosition, @Nullable TargetEntity targetEntity) {
        super(PacketType.Play.Server.FACE_PLAYER);
        this.aimUnit = aimUnit;
        this.targetPosition = targetPosition;
        this.targetEntity = targetEntity;
    }

    @Override
    public void readData() {
        aimUnit = EntitySection.getById(readVarInt());
        targetPosition = new Vector3d(readDouble(), readDouble(), readDouble());
        if (readBoolean()) {
            targetEntity = new TargetEntity(readVarInt(), EntitySection.getById(readVarInt()));
        } else {
            targetEntity = null;
        }
    }

    @Override
    public void readData(WrapperPlayServerFacePlayer wrapper) {
        aimUnit = wrapper.aimUnit;
        targetPosition = wrapper.targetPosition;
        targetEntity = wrapper.targetEntity;
    }

    @Override
    public void writeData() {
        writeVarInt(aimUnit.getId());
        writeDouble(targetPosition.getX());
        writeDouble(targetPosition.getY());
        writeDouble(targetPosition.getZ());
        if (targetEntity != null) {
            writeBoolean(true);
            writeVarInt(targetEntity.getEntityId());
            writeVarInt(targetEntity.getEntitySection().getId());
        } else {
            writeBoolean(false);
        }
    }

    public EntitySection getAimUnit() {
        return aimUnit;
    }

    public void setAimUnit(EntitySection aimUnit) {
        this.aimUnit = aimUnit;
    }

    public Vector3d getTargetPosition() {
        return targetPosition;
    }

    public void setTargetPosition(Vector3d targetPosition) {
        this.targetPosition = targetPosition;
    }

    @Nullable
    public TargetEntity getTargetEntity() {
        return targetEntity;
    }

    public void setTargetEntity(@Nullable TargetEntity targetEntity) {
        this.targetEntity = targetEntity;
    }

    public enum EntitySection {
        EYES, FEET;

        public int getId() {
            return ordinal();
        }

        public static EntitySection getById(int id) {
            return values()[id];
        }
    }

    public static class TargetEntity {
        private int entityId;
        private EntitySection entitySection;

        public TargetEntity(int entityId, EntitySection entitySection) {
            this.entityId = entityId;
            this.entitySection = entitySection;
        }

        public int getEntityId() {
            return entityId;
        }

        public void setEntityId(int entityId) {
            this.entityId = entityId;
        }

        public EntitySection getEntitySection() {
            return entitySection;
        }

        public void setEntitySection(EntitySection entitySection) {
            this.entitySection = entitySection;
        }
    }
}
