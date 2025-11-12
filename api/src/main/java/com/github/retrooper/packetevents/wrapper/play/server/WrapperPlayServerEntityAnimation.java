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

package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerEntityAnimation extends PacketWrapper<WrapperPlayServerEntityAnimation> {
    private int entityID;
    private EntityAnimationType type;

    public WrapperPlayServerEntityAnimation(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerEntityAnimation(int entityID, EntityAnimationType type) {
        super(PacketType.Play.Server.ENTITY_ANIMATION);
        this.entityID = entityID;
        this.type = type;
    }

    @Override
    public void read() {
        entityID = readVarInt();
        type = EntityAnimationType.getById(readUnsignedByte());
    }

    @Override
    public void write() {
        writeVarInt(entityID);
        writeByte(type.ordinal());
    }

    @Override
    public void copy(WrapperPlayServerEntityAnimation wrapper) {
        entityID = wrapper.entityID;
        type = wrapper.type;
    }

    public int getEntityId() {
        return entityID;
    }

    public void setEntityId(int entityID) {
        this.entityID = entityID;
    }

    public EntityAnimationType getType() {
        return type;
    }

    public void setType(EntityAnimationType type) {
        this.type = type;
    }

    public enum EntityAnimationType {
        SWING_MAIN_ARM,
        HURT,
        WAKE_UP,
        SWING_OFF_HAND,
        CRITICAL_HIT,
        MAGIC_CRITICAL_HIT;

        private static final EntityAnimationType[] VALUES = values();

        public static EntityAnimationType getById(int id) {
            return VALUES[id];
        }
    }
}
