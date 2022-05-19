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
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerDeathCombatEvent extends PacketWrapper<WrapperPlayServerDeathCombatEvent> {
    private int playerId;
    private int entityId;
    private String deathMessage;

    public WrapperPlayServerDeathCombatEvent(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerDeathCombatEvent(int playerId, int entityId, String deathMessage) {
        super(PacketType.Play.Server.DEATH_COMBAT_EVENT);
        this.playerId = playerId;
        this.entityId = entityId;
        this.deathMessage = deathMessage;
    }

    @Override
    public void read() {
        this.playerId = readVarInt();
        this.entityId = readInt();
        this.deathMessage = readString();
    }

    @Override
    public void write() {
        writeVarInt(this.playerId);
        writeInt(this.entityId);
        writeString(this.deathMessage);
    }

    @Override
    public void copy(WrapperPlayServerDeathCombatEvent wrapper) {
        this.playerId = wrapper.playerId;
        this.entityId = wrapper.entityId;
        this.deathMessage = wrapper.deathMessage;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public String getDeathMessage() {
        return deathMessage;
    }

    public void setDeathMessage(String deathMessage) {
        this.deathMessage = deathMessage;
    }
}
