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

//TODO Give netty credit

package com.github.retrooper.packetevents.netty.buffer;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ByteBufAbstractOutputStream extends OutputStream implements DataOutput {
    private final ByteBufAbstract buffer;
    private final int startIndex;
    private final DataOutputStream utf8out = new DataOutputStream(this);

    public ByteBufAbstractOutputStream(ByteBufAbstract buffer) {
        if (buffer == null) {
            throw new NullPointerException("buffer");
        } else {
            this.buffer = buffer;
            this.startIndex = buffer.writerIndex();
        }
    }

    public int writtenBytes() {
        return this.buffer.writerIndex() - this.startIndex;
    }

    public void write(byte[] b, int off, int len) throws IOException {
        if (len != 0) {
            this.buffer.writeBytes(b, off, len);
        }
    }

    public void write(byte[] b) throws IOException {
        this.buffer.writeBytes(b);
    }

    public void write(int b) throws IOException {
        this.buffer.writeByte(b);
    }

    public void writeBoolean(boolean v) throws IOException {
        this.buffer.writeBoolean(v);
    }

    public void writeByte(int v) throws IOException {
        this.buffer.writeByte(v);
    }

    @Deprecated
    public void writeBytes(String s) throws IOException {
        throw new IllegalStateException("This operation is not supported!");
    }

    public void writeChar(int v) throws IOException {
        this.buffer.writeChar(v);
    }

    public void writeChars(String s) throws IOException {
        int len = s.length();

        for (int i = 0; i < len; ++i) {
            this.buffer.writeChar(s.charAt(i));
        }

    }

    public void writeDouble(double v) throws IOException {
        this.buffer.writeDouble(v);
    }

    public void writeFloat(float v) throws IOException {
        this.buffer.writeFloat(v);
    }

    public void writeInt(int v) throws IOException {
        this.buffer.writeInt(v);
    }

    public void writeLong(long v) throws IOException {
        this.buffer.writeLong(v);
    }

    public void writeShort(int v) throws IOException {
        this.buffer.writeShort((short) v);
    }

    public void writeUTF(String s) throws IOException {
        this.utf8out.writeUTF(s);
    }

    public ByteBufAbstract buffer() {
        return this.buffer;
    }
}

