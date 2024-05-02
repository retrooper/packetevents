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
import net.kyori.adventure.text.Component;

public class WrapperPlayServerDeathCombatEvent extends PacketWrapper<WrapperPlayServerDeathCombatEvent> {
    private int playerId;
    private Component deathMessage;

    public WrapperPlayServerDeathCombatEvent(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerDeathCombatEvent(int playerId, Component deathMessage) {
        super(PacketType.Play.Server.DEATH_COMBAT_EVENT);
        this.playerId = playerId;
        this.deathMessage = deathMessage;
    }

    @Override
    public void read() {
        this.playerId = readVarInt();
        this.deathMessage = readComponent();
    }

    @Override
    public void write() {
        writeVarInt(this.playerId);
        writeComponent(this.deathMessage);
    }

    @Override
    public void copy(WrapperPlayServerDeathCombatEvent wrapper) {
        this.playerId = wrapper.playerId;
        this.deathMessage = wrapper.deathMessage;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public Component getDeathMessage() {
        return deathMessage;
    }

    public void setDeathMessage(Component deathMessage) {
        this.deathMessage = deathMessage;
    }
}
