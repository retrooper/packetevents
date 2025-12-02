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

package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.UUID;

/**
 * Teleports the player to the given entity if the player is in spectator mode.
 */
public class WrapperPlayClientSpectate extends PacketWrapper<WrapperPlayClientSpectate> {
    private UUID targetUUID;

    public WrapperPlayClientSpectate(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientSpectate(UUID uuid) {
        super(PacketType.Play.Client.SPECTATE);
        this.targetUUID = uuid;
    }

    @Override
    public void read() {
        this.targetUUID = readUUID();
    }

    @Override
    public void write() {
        writeUUID(targetUUID);
    }

    @Override
    public void copy(WrapperPlayClientSpectate wrapper) {
        this.targetUUID = wrapper.targetUUID;
    }

    /**
     * UUID of the entity we want to teleport to.
     *
     * @return UUID of target entity
     */
    public UUID getTargetUUID() {
        return targetUUID;
    }

    /**
     * Modify the UUID of the entity we want to teleport to.
     *
     * @param uuid UUID of target entity
     */
    public void setTargetUUID(UUID uuid) {
        this.targetUUID = uuid;
    }
}
