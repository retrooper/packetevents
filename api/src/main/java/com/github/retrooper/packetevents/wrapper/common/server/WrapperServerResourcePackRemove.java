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

package com.github.retrooper.packetevents.wrapper.common.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.CommonPacketWrapper;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class WrapperServerResourcePackRemove extends CommonPacketWrapper<WrapperServerResourcePackRemove> {

    private @Nullable UUID packId;

    public WrapperServerResourcePackRemove(PacketSendEvent event) {
        super(event);
    }

    public WrapperServerResourcePackRemove(ConnectionState state, @Nullable UUID packId) {
        super(state == ConnectionState.CONFIGURATION ? PacketType.Configuration.Server.RESOURCE_PACK_REMOVE : PacketType.Play.Server.RESOURCE_PACK_REMOVE);
        this.packId = packId;
    }

    @Override
    public void read() {
        this.packId = this.readOptional(PacketWrapper::readUUID);
    }

    @Override
    public void write() {
        this.writeOptional(this.packId, PacketWrapper::writeUUID);
    }

    @Override
    public void copy(WrapperServerResourcePackRemove wrapper) {
        this.packId = wrapper.packId;
    }

    @Override
    public WrapperServerResourcePackRemove copy(ConnectionState state) {
        return new WrapperServerResourcePackRemove(state, this.packId);
    }

    public @Nullable UUID getPackId() {
        return this.packId;
    }

    public void setPackId(@Nullable UUID packId) {
        this.packId = packId;
    }
}
