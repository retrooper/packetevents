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

package io.github.retrooper.packetevents.wrapper.game.client;

import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.manager.player.ClientVersion;
import io.github.retrooper.packetevents.protocol.PacketType;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.UUID;

/**
 * Teleports the player to the given entity if the player is in spectator mode.
 */
public class WrapperGameClientSpectate extends PacketWrapper<WrapperGameClientSpectate> {
    private UUID targetUUID;

    public WrapperGameClientSpectate(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperGameClientSpectate(ClientVersion clientVersion, UUID uuid) {
        super(PacketType.Game.Client.SPECTATE.getPacketID(clientVersion), clientVersion);
        this.targetUUID = uuid;
    }

    @Override
    public void readData() {
        this.targetUUID = readUUID();
    }

    @Override
    public void readData(WrapperGameClientSpectate wrapper) {
        this.targetUUID = wrapper.targetUUID;
    }

    @Override
    public void writeData() {
        writeUUID(targetUUID);
    }

    /**
     * UUID of the entity we want to teleport to.
     *
     * @return UUID of target entity
     */
    public UUID getTargetUUID() {
        return targetUUID;
    }

    public void setTargetUUID(UUID uuid) {
        this.targetUUID = uuid;
    }
}
