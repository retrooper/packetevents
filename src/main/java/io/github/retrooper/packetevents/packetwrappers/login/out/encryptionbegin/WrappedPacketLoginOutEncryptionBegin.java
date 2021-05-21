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

package io.github.retrooper.packetevents.packetwrappers.login.out.encryptionbegin;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;

import java.security.PublicKey;

public class WrappedPacketLoginOutEncryptionBegin extends WrappedPacket {
    public WrappedPacketLoginOutEncryptionBegin(NMSPacket packet) {
        super(packet);
    }

    public String getEncodedString() {
        return readString(0);
    }

    public void setEncodedString(String encodedString) {
        writeString(0, encodedString);
    }

    public PublicKey getPublicKey() {
        return readObject(1, PublicKey.class);
    }

    public void setPublicKey(PublicKey key) {
        writeObject(0, key);
    }

    public byte[] getVerifyToken() {
        return readByteArray(0);
    }

    public void setVerifyToken(byte[] verifyToken) {
        writeByteArray(0, verifyToken);
    }

    @Override
    public boolean isSupported() {
        return PacketTypeClasses.Login.Server.ENCRYPTION_BEGIN != null;
    }
}
