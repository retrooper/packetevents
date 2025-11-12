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
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class WrapperPlayServerDeathCombatEvent extends PacketWrapper<WrapperPlayServerDeathCombatEvent> {
    private int playerId;
    private Integer entityId;
    private Component deathMessage;

    public WrapperPlayServerDeathCombatEvent(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerDeathCombatEvent(int playerId, @Nullable Integer entityId, Component deathMessage) {
        super(PacketType.Play.Server.DEATH_COMBAT_EVENT);
        this.playerId = playerId;
        this.entityId = entityId;
        this.deathMessage = deathMessage;
    }

    @Override
    public void read() {
        this.playerId = readVarInt();
        if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_19_4)) {
            this.entityId = readInt();
        }
        this.deathMessage = readComponent();
    }

    @Override
    public void write() {
        writeVarInt(this.playerId);
        if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_19_4)) {
            int id = entityId != null ? entityId : 0;
            writeInt(id);
        }
        writeComponent(this.deathMessage);
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

    public Optional<Integer> getEntityId() {
        return Optional.ofNullable(entityId);
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public Component getDeathMessage() {
        return deathMessage;
    }

    public void setDeathMessage(Component deathMessage) {
        this.deathMessage = deathMessage;
    }
}
