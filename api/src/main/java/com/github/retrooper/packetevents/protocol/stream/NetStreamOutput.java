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

/*
TODO Correct
 * This class was taken from MCProtocolLib.
 *
 * https://github.com/Steveice10/MCProtocolLib
 */
package com.github.retrooper.packetevents.protocol.stream;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class NetStreamOutput extends FilterOutputStream {
    public NetStreamOutput(OutputStream out) {
        super(out);
    }


    public void writeBoolean(boolean b) {
        this.writeByte(b ? 1 : 0);
    }


    public void writeByte(int b) {
        try {
            this.write(b);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void writeShort(int s) {
        this.writeByte((byte) ((s >>> 8) & 0xFF));
        this.writeByte((byte) ((s >>> 0) & 0xFF));
    }


    public void writeChar(int c) {
        this.writeShort(c);
    }


    public void writeInt(int i) {
        this.writeByte((byte) ((i >>> 24) & 0xFF));
        this.writeByte((byte) ((i >>> 16) & 0xFF));
        this.writeByte((byte) ((i >>> 8) & 0xFF));
        this.writeByte((byte) ((i >>> 0) & 0xFF));
    }


    public void writeVarInt(int i) {
        while ((i & ~0x7F) != 0) {
            this.writeByte((i & 0x7F) | 0x80);
            i >>>= 7;
        }

        this.writeByte(i);
    }


    public void writeLong(long l) {
        this.writeByte((byte) (l >>> 56));
        this.writeByte((byte) (l >>> 48));
        this.writeByte((byte) (l >>> 40));
        this.writeByte((byte) (l >>> 32));
        this.writeByte((byte) (l >>> 24));
        this.writeByte((byte) (l >>> 16));
        this.writeByte((byte) (l >>> 8));
        this.writeByte((byte) (l >>> 0));
    }


    public void writeVarLong(long l) {
        while ((l & ~0x7F) != 0) {
            this.writeByte((int) (l & 0x7F) | 0x80);
            l >>>= 7;
        }

        this.writeByte((int) l);
    }


    public void writeFloat(float f) {
        this.writeInt(Float.floatToIntBits(f));
    }


    public void writeDouble(double d) {
        this.writeLong(Double.doubleToLongBits(d));
    }


    public void writeBytes(byte[] b) {
        this.writeBytes(b, b.length);
    }


    public void writeBytes(byte[] b, int length) {
        try {
            this.write(b, 0, length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void writeShorts(short[] s) {
        this.writeShorts(s, s.length);
    }


    public void writeShorts(short[] s, int length) {
        for (int index = 0; index < length; index++) {
            this.writeShort(s[index]);
        }
    }


    public void writeInts(int[] i) {
        this.writeInts(i, i.length);
    }


    public void writeInts(int[] i, int length) {
        for (int index = 0; index < length; index++) {
            this.writeInt(i[index]);
        }
    }


    public void writeLongs(long[] l) {
        this.writeLongs(l, l.length);
    }


    public void writeLongs(long[] l, int length) {
        for (int index = 0; index < length; index++) {
            this.writeLong(l[index]);
        }
    }


    public void writeString(String s) {
        if (s == null) {
            throw new IllegalArgumentException("String cannot be null!");
        }

        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        if (bytes.length > 32767) {
            try {
                throw new IOException("String too big (was " + s.length() + " bytes encoded, max " + 32767 + ")");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            this.writeVarInt(bytes.length);
            this.writeBytes(bytes);
        }
    }


    public void writeUUID(UUID uuid) {
        this.writeLong(uuid.getMostSignificantBits());
        this.writeLong(uuid.getLeastSignificantBits());
    }
}
