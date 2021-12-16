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

package com.github.retrooper.packetevents.wrapper.login.server;

import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.event.impl.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.gameprofile.GameProfile;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.UUID;

/**
 * This packet switches the connection state to {@link ConnectionState#PLAY}.
 */
public class WrapperLoginServerLoginSuccess extends PacketWrapper<WrapperLoginServerLoginSuccess> {
    private GameProfile gameProfile;

    public WrapperLoginServerLoginSuccess(PacketSendEvent event) {
        super(event);
    }

    public WrapperLoginServerLoginSuccess(UUID uuid, String username) {
        super(PacketType.Login.Server.LOGIN_SUCCESS);
        this.gameProfile = new GameProfile(uuid, username);
    }

    public WrapperLoginServerLoginSuccess(GameProfile gameProfile) {
        super(PacketType.Login.Server.LOGIN_SUCCESS);
        this.gameProfile = gameProfile;
    }

    public static int[] serializeUUID(UUID uuid) {
        long mostSigBits = uuid.getMostSignificantBits();
        long leastSigBits = uuid.getLeastSignificantBits();
        return new int[]{(int) (mostSigBits >> 32), (int) mostSigBits, (int) (leastSigBits >> 32), (int) leastSigBits};
    }

    @Override
    public void readData() {
        UUID uuid;
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
            int[] data = new int[4];
            for (int i = 0; i < 4; i++) {
                data[i] = readInt();
            }
            uuid = deserializeUUID(data);
        } else {
            uuid = UUID.fromString(readString(36));
        }
        String username = readString(16);
        this.gameProfile = new GameProfile(uuid, username);
    }

    @Override
    public void readData(WrapperLoginServerLoginSuccess wrapper) {
        this.gameProfile = wrapper.gameProfile;
    }

    @Override
    public void writeData() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
            int[] data = serializeUUID(gameProfile.getId());
            for (int i = 0; i < 4; i++) {
                writeInt(data[i]);
            }
        } else {
            writeString(gameProfile.getId().toString(), 36);
        }
        writeString(gameProfile.getName(), 16);
    }

    private UUID deserializeUUID(int[] data) {
        return new UUID((long) data[0] << 32 | data[1] & 4294967295L, (long) data[2] << 32 | data[3] & 4294967295L);
    }

    public GameProfile getGameProfile() {
        return gameProfile;
    }

    public void setGameProfile(GameProfile gameProfile) {
        this.gameProfile = gameProfile;
    }
}
