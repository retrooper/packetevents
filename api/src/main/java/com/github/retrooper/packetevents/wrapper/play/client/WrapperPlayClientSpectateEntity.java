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
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Mojang name: ServerboundSpectatorActionPacket
 * <p>
 * Named ServerboundSpectateEntityPacket before 26.2
 *
 * @versions 26.1+
 */
@NullMarked
public class WrapperPlayClientSpectateEntity extends PacketWrapper<WrapperPlayClientSpectateEntity> {

    private @Nullable Integer entityId;

    public WrapperPlayClientSpectateEntity(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientSpectateEntity(int entityId) {
        this(Integer.valueOf(entityId));
    }

    public WrapperPlayClientSpectateEntity(@Nullable Integer entityId) {
        super(PacketType.Play.Client.SPECTATE_ENTITY);
        this.entityId = entityId;
    }

    @Override
    public void read() {
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_26_2)) {
            this.entityId = this.readNullableVarInt();
        } else {
            this.entityId = this.readVarInt();
        }
    }

    @Override
    public void write() {
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_26_2)) {
            this.writeNullableVarInt(this.entityId);
        } else {
            this.writeVarInt(this.entityId != null ? this.entityId : 0);
        }
    }

    @Override
    public void copy(WrapperPlayClientSpectateEntity wrapper) {
        this.entityId = wrapper.entityId;
    }

    public boolean hasEntityId() {
        return this.entityId != null;
    }

    /**
     * Only handles null for backwards compat,
     * DO NOT compare return value against 0 for checking
     * whether this entity id is set or not.
     */
    public int getEntityId() {
        return this.entityId != null ? this.entityId : 0;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public void setEntityId(@Nullable Integer entityId) {
        this.entityId = entityId;
    }
}
