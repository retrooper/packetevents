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

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.manager.player.ClientVersion;
import io.github.retrooper.packetevents.manager.server.ServerVersion;
import io.github.retrooper.packetevents.utils.netty.buffer.ByteBufAbstract;

import java.nio.charset.StandardCharsets;
import java.util.UUID;


public class PacketWrapper {
    protected final ClientVersion clientVersion;
    private static ServerVersion serverVersion;
    protected final ByteBufAbstract byteBuf;

    public PacketWrapper(ClientVersion clientVersion, ByteBufAbstract byteBuf) {
        this.clientVersion = clientVersion;
        this.byteBuf = byteBuf;
    }

    public PacketWrapper(ByteBufAbstract byteBuf) {
        this.clientVersion = ClientVersion.UNKNOWN;
        this.byteBuf = byteBuf;
    }

    public ClientVersion getClientVersion() {
        return clientVersion;
    }

    public ServerVersion getServerVersion() {
        if (serverVersion == null) {
            serverVersion = PacketEvents.get().getServerManager().getVersion();
        }
        return serverVersion;
    }

    public ByteBufAbstract getByteBuf() {
        return byteBuf;
    }

    public byte readByte() {
        return byteBuf.readByte();
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

    public void writeVarInt(int value) {
        while ((value & -128) != 0) {
            byteBuf.writeByte(value & 127 | 128);
            value >>>= 7;
        }

        byteBuf.writeByte(value);
    }

    public String readString() {
        return readString(32767);
    }

    public String readString(int maxLen) {
        int protocolVersion = (clientVersion != ClientVersion.UNKNOWN) ? clientVersion.getProtocolVersion() : getServerVersion().getProtocolVersion();
        return readString(protocolVersion, maxLen);
    }

    public String readString(int protocolVersion, int maxLen) {
        //1.12 and higher
        if (protocolVersion >= 335) {
            return readStringModern(maxLen);
        } else {
            return readStringLegacy(maxLen);
        }
    }

    private String readStringLegacy(int i) {
        int j = readVarInt();
        if (j > i * 4) {
            throw new RuntimeException("The received encoded string buffer length is longer than maximum allowed (" + j + " > " + (i * 4) + ")");
        } else if (j < 0) {
            throw new RuntimeException("The received encoded string buffer length is less than zero! Weird string!");
        } else {
            ByteBufAbstract bb = byteBuf.readBytes(j);
            byte[] bytes;
            if (bb.hasArray()) {
                bytes = bb.array();
            } else {
                bytes = new byte[bb.readableBytes()];
                bb.getBytes(bb.readerIndex(), bytes);
            }
            String s = new String(bytes);
            if (s.length() > i) {
                throw new RuntimeException("The received string length is longer than maximum allowed (" + j + " > " + i + ")");
            }
            return s;
        }
    }

    private String readStringModern(int i) {
        int j = readVarInt();
        if (j > i * 4) {
            throw new RuntimeException("The received encoded string buffer length is longer than maximum allowed (" + j + " > " + i * 4 + ")");
        } else if (j < 0) {
            throw new RuntimeException("The received encoded string buffer length is less than zero! Weird string!");
        } else {
            String s = byteBuf.toString(byteBuf.readerIndex(), j, StandardCharsets.UTF_8);
            byteBuf.readerIndex(byteBuf.readerIndex() + j);
            if (s.length() > i) {
                throw new RuntimeException("The received string length is longer than maximum allowed (" + j + " > " + i + ")");
            } else {
                return s;
            }
        }
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

    public float readFloat() {
        return byteBuf.readFloat();
    }

    public double readDouble() {
        return byteBuf.readDouble();
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

    public static PacketWrapper createUniversalPacketWrapper(ByteBufAbstract byteBuf) {
        return new PacketWrapper(ClientVersion.UNKNOWN, byteBuf);
    }
}
