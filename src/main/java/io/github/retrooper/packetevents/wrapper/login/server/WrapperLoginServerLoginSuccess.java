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

package io.github.retrooper.packetevents.wrapper.login.server;

import io.github.retrooper.packetevents.utils.bytebuf.ByteBufAbstract;
import io.github.retrooper.packetevents.manager.player.ClientVersion;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.UUID;

public class WrapperLoginServerLoginSuccess extends PacketWrapper {
    private final UUID uuid;
    private final String username;
    public WrapperLoginServerLoginSuccess(ClientVersion version, ByteBufAbstract byteBuf) {
        super(version, byteBuf);
        if (version.isNewerThanOrEquals(ClientVersion.v_1_16)) {
            int[] data = new int[4];
            for (int i = 0; i < 4; i++) {
                data[i] = readInt();
            }
            this.uuid = convertToUUID(data);
        }
        else {
            this.uuid = UUID.fromString(readString(36));
        }
        this.username = readString(16);
    }

    private UUID convertToUUID(int[] data) {
        return new UUID((long)data[0] << 32 | data[1] & 4294967295L, (long)data[2] << 32 | data[3] & 4294967295L);
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }
}
