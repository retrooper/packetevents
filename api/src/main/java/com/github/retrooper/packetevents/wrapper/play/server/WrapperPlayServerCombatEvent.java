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
import com.github.retrooper.packetevents.protocol.player.Combat;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.OptionalInt;

public class WrapperPlayServerCombatEvent extends PacketWrapper<WrapperPlayServerCombatEvent> {
    private Combat combat;
    private int duration;
    private int entityId;
    private int playerId;
    private @Nullable Component deathMessage;

    public WrapperPlayServerCombatEvent(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerCombatEvent(Combat combat) {
        super(PacketType.Play.Server.COMBAT_EVENT);
        this.combat = combat;
    }

    public WrapperPlayServerCombatEvent(Combat combat, int duration, int entityId) {
        super(PacketType.Play.Server.COMBAT_EVENT);
        this.combat = combat;
        this.duration = duration;
        this.entityId = entityId;
    }

    public WrapperPlayServerCombatEvent(Combat combat, int playerId, int entityId, @Nullable Component deathMessage) {
        super(PacketType.Play.Server.COMBAT_EVENT);
        this.combat = combat;
        this.playerId = playerId;
        this.entityId = entityId;
        this.deathMessage = deathMessage;
    }

    @Override
    public void read() {
        this.combat = Combat.getById(readVarInt());
        switch (combat) {
            case END_COMBAT:
                this.duration = readVarInt();
                this.entityId = readInt();
                break;
            case ENTITY_DEAD:
                this.playerId = readVarInt();
                this.entityId = readInt();
                this.deathMessage = readComponent();
                break;
            case ENTER_COMBAT:
            default:
                break;
        }
    }

    @Override
    public void write() {
        writeVarInt(combat.getId());
        switch (combat) {
            case END_COMBAT:
                writeVarInt(duration);
                writeInt(entityId);
                break;
            case ENTITY_DEAD:
                writeVarInt(playerId);
                writeInt(entityId);
                writeComponent(deathMessage);
                break;
            case ENTER_COMBAT:
            default:
                break;
        }
    }

    @Override
    public void copy(WrapperPlayServerCombatEvent wrapper) {
        this.combat = wrapper.combat;
        this.duration = wrapper.duration;
        this.entityId = wrapper.entityId;
        this.playerId = wrapper.playerId;
        this.deathMessage = wrapper.deathMessage;
    }

    public Combat getCombat() {
        return combat;
    }

    public void setCombat(Combat combat) {
        this.combat = combat;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public Optional<Component> getDeathMessage() {
        return Optional.ofNullable(deathMessage);
    }

    public void setDeathMessage(@Nullable Component deathMessage) {
        this.deathMessage = deathMessage;
    }
}
