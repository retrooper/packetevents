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

package io.github.retrooper.packetevents.wrapper;

import io.github.retrooper.packetevents.utils.netty.bytebuf.ByteBufAbstract;
import io.github.retrooper.packetevents.utils.player.ClientVersion;

import java.nio.charset.StandardCharsets;
import java.util.UUID;


public class PacketWrapper {
    private final ClientVersion version;
    private final ByteBufAbstract byteBuf;

    public PacketWrapper(ByteBufAbstract byteBuf) {
        this.version = ClientVersion.UNKNOWN;
        this.byteBuf = byteBuf;
    }

    public PacketWrapper(ClientVersion version, ByteBufAbstract byteBuf) {
        this.version = version;
        this.byteBuf = byteBuf;
    }

    public ClientVersion getClientVersion() {
        return version;
    }

    public ByteBufAbstract getByteBuf() {
        return byteBuf;
    }

    public int readInt() {
        return byteBuf.readInt();
    }

    public int readVarInt() {
        byte b0;
        int i = 0;
        int j = 0;
        do {
            b0 = byteBuf.readByte();
            i |= (b0 & Byte.MAX_VALUE) << j++ * 7;
            if (j > 5)
                throw new RuntimeException("VarInt too big");
        } while ((b0 & 0x80) == 128);
        return i;
    }

    public String readString() {
        return readString(32767);
    }

    public String readString(int maxLen) {
        int j = readVarInt();
        if (j > maxLen * 4)
            throw new RuntimeException("The received encoded string buffer length is longer than maximum allowed (" + j + " > " + (maxLen * 4) + ")");
        if (j < 0)
            throw new RuntimeException("The received encoded string buffer length is less than zero! Weird string!");
        ByteBufAbstract bb = byteBuf.readBytes(j);
        byte[] array;
        if (bb.hasArray()) {
            array = bb.array();
        }
        else {
            array = new byte[bb.readableBytes()];
            bb.getBytes(bb.readerIndex(), array);
        }
        String s = new String(array, StandardCharsets.UTF_8);
        if (s.length() > maxLen)
            throw new RuntimeException("The received string length is longer than maximum allowed (" + j + " > " + maxLen + ")");
        return s;
    }

    public int readUnsignedShort() {
        return byteBuf.readUnsignedShort();
    }

    public short readShort() {
        return byteBuf.readShort();
    }

    public long readLong() {
        return byteBuf.readLong();
    }

    public boolean readBoolean() {
        return byteBuf.readBoolean();
    }

    public byte[] readByteArray(int length) {
        byte[] ret = new byte[length];
        byteBuf.readBytes(ret);
        return ret;
    }

    public UUID readUUID() {
        long mostSigBits = readLong();
        long leastSigBits = readLong();
        return new UUID(mostSigBits, leastSigBits);
    }
}
