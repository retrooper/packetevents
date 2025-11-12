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
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.potion.PotionType;
import com.github.retrooper.packetevents.protocol.potion.PotionTypes;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerRemoveEntityEffect extends PacketWrapper<WrapperPlayServerRemoveEntityEffect> {
    private int entityId;
    private PotionType potionType;

    public WrapperPlayServerRemoveEntityEffect(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerRemoveEntityEffect(int entityId, PotionType potionType) {
        super(PacketType.Play.Server.REMOVE_ENTITY_EFFECT);
        this.entityId = entityId;
        this.potionType = potionType;
    }

    @Override
    public void read() {
        this.entityId = readVarInt();
        int effectId;
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_18_2)) {
            effectId = readVarInt();
        } else {
            effectId = readByte();
        }
        this.potionType = PotionTypes.getById(effectId, this.serverVersion);
    }

    @Override
    public void write() {
        writeVarInt(entityId);
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_18_2)) {
            writeVarInt(potionType.getId(serverVersion.toClientVersion()));
        } else {
            writeByte(potionType.getId(serverVersion.toClientVersion()));
        }
    }

    @Override
    public void copy(WrapperPlayServerRemoveEntityEffect wrapper) {
        entityId = wrapper.entityId;
        potionType = wrapper.potionType;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public PotionType getPotionType() {
        return potionType;
    }

    public void setPotionType(PotionType potionType) {
        this.potionType = potionType;
    }
}
