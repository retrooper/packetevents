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
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;

public class WrapperPlayServerEntityAnimation extends PacketWrapper<WrapperPlayServerEntityAnimation> {
    private int entityID;
    private EntityAnimationType type;
    public enum EntityAnimationType {
        SWING_MAIN_ARM,
        TAKE_DAMAGE,
        LEAVE_BED,
        SWING_OFFHAND,
        CRITICAL_EFFECT,
        MAGIC_CRITICAL_EFFECT;

        public static final EntityAnimationType[] VALUES = values();
    }

    public WrapperPlayServerEntityAnimation(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerEntityAnimation(int entityID, EntityAnimationType type) {
        super(PacketType.Play.Server.ENTITY_ANIMATION);
        this.entityID = entityID;
        this.type = type;
    }

    @Override
    public void readData() {
        entityID = readVarInt();
        type = EntityAnimationType.VALUES[readByte()];
    }

    @Override
    public void readData(WrapperPlayServerEntityAnimation wrapper) {
        entityID = wrapper.entityID;
        type = wrapper.type;
    }

    @Override
    public void writeData() {
        writeVarInt(entityID);
        writeByte(type.ordinal());
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
}
