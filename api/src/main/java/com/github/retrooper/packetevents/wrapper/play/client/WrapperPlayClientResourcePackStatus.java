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
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.UUID;

public class WrapperPlayClientResourcePackStatus extends PacketWrapper<WrapperPlayClientResourcePackStatus> {

    private UUID packId;
    private String hash;
    private Result result;

    public WrapperPlayClientResourcePackStatus(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientResourcePackStatus(Result result) {
        this(UUID.randomUUID(), result);
    }

    public WrapperPlayClientResourcePackStatus(UUID packId, Result result) {
        super(PacketType.Play.Client.RESOURCE_PACK_STATUS);
        this.packId = packId;
        this.result = result;
    }

    @Deprecated
    public WrapperPlayClientResourcePackStatus(String hash, Result result) {
        super(PacketType.Play.Client.RESOURCE_PACK_STATUS);
        this.hash = hash;
        this.result = result;
    }

    @Override
    public void read() {
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_3)) {
            this.packId = this.readUUID();
        }

        if (serverVersion.isOlderThan(ServerVersion.V_1_10)) {
            //For now ignore hash, maybe make optional
            this.hash = readString(40);
        } else {
            this.hash = "";
        }
        int resultIndex = readVarInt();
        this.result = Result.VALUES[resultIndex];
    }

    @Override
    public void write() {
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_3)) {
            this.writeUUID(this.packId);
        }

        if (serverVersion.isOlderThan(ServerVersion.V_1_10)) {
            writeString(hash, 40);
        }
        writeVarInt(result.ordinal());
    }

    @Override
    public void copy(WrapperPlayClientResourcePackStatus wrapper) {
        this.packId = wrapper.packId;
        this.hash = wrapper.hash;
        this.result = wrapper.result;
    }

    public UUID getPackId() {
        return this.packId;
    }

    public void setPackId(UUID packId) {
        this.packId = packId;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public enum Result {

        SUCCESSFULLY_LOADED,
        DECLINED,
        FAILED_DOWNLOAD,
        ACCEPTED,
        DOWNLOADED,
        INVALID_URL,
        FAILED_RELOAD,
        DISCARDED;

        public static final Result[] VALUES = values();
    }
}
