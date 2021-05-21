/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2016-2021 retrooper and contributors
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

package io.github.retrooper.packetevents.packetwrappers.login.in.encryptionbegin;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketLoginInEncryptionBegin extends WrappedPacket {
    public WrappedPacketLoginInEncryptionBegin(NMSPacket packet) {
        super(packet);
    }

    public byte[] getPublicKey() {
        return readByteArray(0);
    }

    public void setPublicKey(byte[] key) {
        writeByteArray(0, key);
    }

    public byte[] getVerifyToken() {
        return readByteArray(1);
    }

    public void setVerifyToken(byte[] token) {
        writeByteArray(1, token);
    }

    @Override
    public boolean isSupported() {
        return PacketTypeClasses.Login.Client.ENCRYPTION_BEGIN != null;
    }
}
