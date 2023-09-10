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

package com.github.retrooper.packetevents.wrapper.login.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.util.crypto.SignatureData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class WrapperLoginClientLoginStart extends PacketWrapper<WrapperLoginClientLoginStart> {
    private String username;
    private @Nullable SignatureData signatureData;
    private @Nullable UUID playerUUID;

    public WrapperLoginClientLoginStart(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperLoginClientLoginStart(ClientVersion clientVersion, String username) {
        this(clientVersion, username, null, null);
    }

    public WrapperLoginClientLoginStart(ClientVersion clientVersion, String username, @Nullable SignatureData signatureData) {
        this(clientVersion, username, signatureData, null);
    }

    public WrapperLoginClientLoginStart(ClientVersion clientVersion, String username, @Nullable SignatureData signatureData,
                                        @Nullable UUID playerUUID) {
        super(PacketType.Login.Client.LOGIN_START.getId(), clientVersion);
        this.username = username;
        this.signatureData = signatureData;
        this.playerUUID = playerUUID;
    }

    @Override
    public void read() {
        this.username = readString(16);
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
            if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_19_3)) {
                //Removed in 1.19.4
                this.signatureData = readOptional(PacketWrapper::readSignatureData);
            }
           
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_2)) {
                this.playerUUID = readUUID();
            } else if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_1)) {
                this.playerUUID = readOptional(PacketWrapper::readUUID);
            }
        }
    }

    @Override
    public void write() {
        writeString(username, 16);
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
            if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_19_3)) {
                //Removed in 1.19.4
                writeOptional(signatureData, PacketWrapper::writeSignatureData);
            }
           
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_2)) {
                writeUUID(java.util.Objects.requireNonNull(playerUUID, "playerUUID is required for >= 1.20.2"));
            } else if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_1)) {
                writeOptional(playerUUID, PacketWrapper::writeUUID);
            }
        }
    }

    @Override
    public void copy(WrapperLoginClientLoginStart wrapper) {
        this.username = wrapper.username;
        this.signatureData = wrapper.signatureData;
        this.playerUUID = wrapper.playerUUID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Optional<SignatureData> getSignatureData() {
        return Optional.ofNullable(signatureData);
    }

    public void setSignatureData(@Nullable SignatureData signatureData) {
        this.signatureData = signatureData;
    }

    public Optional<UUID> getPlayerUUID() {
        return Optional.ofNullable(playerUUID);
    }

    public void setPlayerUUID(@Nullable UUID playerUUID) {
        this.playerUUID = playerUUID;
    }
}
