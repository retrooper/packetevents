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

//TODO Give netty credit

package com.github.retrooper.packetevents.netty.buffer;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ByteBufOutputStream extends OutputStream implements DataOutput {
    private final Object buffer;
    private final int startIndex;
    private final DataOutputStream utf8out = new DataOutputStream(this);

    public ByteBufOutputStream(Object buffer) {
        if (buffer == null) {
            throw new NullPointerException("buffer");
        } else {
            this.buffer = buffer;
            this.startIndex = ByteBufHelper.writerIndex(buffer);
        }
    }

    public int writtenBytes() {
        return ByteBufHelper.writerIndex(buffer) - this.startIndex;
    }

    public void write(byte[] b, int off, int len) throws IOException {
        if (len != 0) {
            ByteBufHelper.writeBytes(buffer, b, off, len);
        }
    }

    public void write(byte[] b) throws IOException {
        ByteBufHelper.writeBytes(buffer, b);
    }

    public void write(int b) throws IOException {
        ByteBufHelper.writeByte(buffer, b);
    }

    public void writeBoolean(boolean v) throws IOException {
        ByteBufHelper.writeBoolean(buffer, v);
    }

    public void writeByte(int v) throws IOException {
        ByteBufHelper.writeByte(buffer, v);
    }

    @Deprecated
    public void writeBytes(String s) throws IOException {
        throw new IllegalStateException("This operation is not supported!");
    }

    public void writeChar(int v) throws IOException {
        ByteBufHelper.writeChar(buffer, v);
    }

    public void writeChars(String s) throws IOException {
        int len = s.length();

        for (int i = 0; i < len; ++i) {
            ByteBufHelper.writeChar(buffer, s.charAt(i));
        }

    }

    public void writeDouble(double v) throws IOException {
        ByteBufHelper.writeDouble(buffer, v);
    }

    public void writeFloat(float v) throws IOException {
        ByteBufHelper.writeFloat(buffer, v);
    }

    public void writeInt(int v) throws IOException {
        ByteBufHelper.writeInt(buffer, v);
    }

    public void writeLong(long v) throws IOException {
        ByteBufHelper.writeLong(buffer, v);
    }

    public void writeShort(int v) throws IOException {
        ByteBufHelper.writeShort(buffer, v);
    }

    public void writeUTF(String s) throws IOException {
        this.utf8out.writeUTF(s);
    }

    public Object buffer() {
        return this.buffer;
    }
}

