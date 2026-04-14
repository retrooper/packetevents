/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2026 retrooper and contributors
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
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

/**
 * Mojang name: ServerboundAttackPacket
 *
 * @versions 26.1+
 */
@NullMarked
public class WrapperPlayClientAttack extends PacketWrapper<WrapperPlayClientAttack> {

    private int entityId;

    public WrapperPlayClientAttack(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientAttack(int entityId) {
        super(PacketType.Play.Client.ATTACK);
        this.entityId = entityId;
    }

    @Override
    public void read() {
        this.entityId = this.readVarInt();
    }

    @Override
    public void write() {
        this.writeVarInt(this.entityId);
    }

    @Override
    public void copy(WrapperPlayClientAttack wrapper) {
        this.entityId = wrapper.entityId;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }
}
