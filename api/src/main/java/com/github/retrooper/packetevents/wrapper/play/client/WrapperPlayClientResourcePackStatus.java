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

package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.resourcepack.ResourcePacketResult;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class WrapperPlayClientResourcePackStatus extends PacketWrapper<WrapperPlayClientResourcePackStatus> {
    private @Nullable String hash;
    private ResourcePacketResult result;

    public WrapperPlayClientResourcePackStatus(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientResourcePackStatus(ResourcePacketResult result) {
        this(null, result);
    }

    @Deprecated
    public WrapperPlayClientResourcePackStatus(@Nullable String hash, ResourcePacketResult result) {
        super(PacketType.Play.Client.RESOURCE_PACK_STATUS);
        this.hash = hash;
        this.result = result;
    }

    @Override
    public void read() {
        if (serverVersion.isOlderThan(ServerVersion.V_1_10)) {
            this.hash = readString(40);
        }
        this.result = ResourcePacketResult.getById(readVarInt());
    }

    @Override
    public void write() {
        if (serverVersion.isOlderThan(ServerVersion.V_1_10)) {
            writeString(hash, 40);
        }
        writeVarInt(result.ordinal());
    }

    @Override
    public void copy(WrapperPlayClientResourcePackStatus wrapper) {
        this.hash = wrapper.hash;
        this.result = wrapper.result;
    }

    public ResourcePacketResult getResult() {
        return result;
    }

    public void setResult(ResourcePacketResult result) {
        this.result = result;
    }

    public Optional<String> getHash() {
        return Optional.ofNullable(hash);
    }

    public void setHash(@Nullable String hash) {
        this.hash = hash;
    }
}
