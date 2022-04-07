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
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.UUID;

/**
 * This packet switches the connection state to {@link ConnectionState#PLAY}.
 */
public class WrapperLoginServerLoginSuccess extends PacketWrapper<WrapperLoginServerLoginSuccess> {
    private UserProfile userProfile;

    public WrapperLoginServerLoginSuccess(PacketSendEvent event) {
        super(event);
    }

    public WrapperLoginServerLoginSuccess(UUID uuid, String username) {
        super(PacketType.Login.Server.LOGIN_SUCCESS);
        this.userProfile = new UserProfile(uuid, username);
    }

    public WrapperLoginServerLoginSuccess(UserProfile userProfile) {
        super(PacketType.Login.Server.LOGIN_SUCCESS);
        this.userProfile = userProfile;
    }

    private UUID readUUIDAsIntArray() {
        long mostSignificantBits = (long) readInt() << 32L | readInt() & 0xFFFFFFFFL;
        long leastSignificantBits = (long) readInt() << 32L | readInt() & 0xFFFFFFFFL;
        return new UUID(mostSignificantBits, leastSignificantBits);
    }

    private void writeUUIDAsIntArray(UUID uuid) {
        writeInt((int)(uuid.getMostSignificantBits() >> 32L));
        writeInt((int)uuid.getMostSignificantBits());
        writeInt((int)(uuid.getLeastSignificantBits() >> 32L));
        writeInt((int)uuid.getLeastSignificantBits());
    }

    private UUID readUUIDAsString() {
        return UUID.fromString(readString(36));
    }

    private void writeUUIDAsString(UUID uuid) {
        writeString(uuid.toString(), 36);
    }
    @Override
    public void read() {
        UUID uuid;
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
            uuid = readUUIDAsIntArray();
        } else {
            uuid = readUUIDAsString();
        }
        String username = readString(16);
        this.userProfile = new UserProfile(uuid, username);
    }

    @Override
    public void copy(WrapperLoginServerLoginSuccess wrapper) {
        this.userProfile = wrapper.userProfile;
    }

    @Override
    public void write() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
           writeUUIDAsIntArray(userProfile.getUUID());
        } else {
            writeUUIDAsString(userProfile.getUUID());
        }
        writeString(userProfile.getName(), 16);
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }
}
