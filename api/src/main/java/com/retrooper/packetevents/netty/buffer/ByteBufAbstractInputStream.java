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

package com.retrooper.packetevents.netty.buffer;

import java.io.*;

//TODO Give netty credit
public class ByteBufAbstractInputStream extends InputStream implements DataInput {
    private final ByteBufAbstract buffer;
    private final int startIndex;
    private final int endIndex;
    private final boolean releaseOnClose;
    private final StringBuilder lineBuf;
    private boolean closed;

    public ByteBufAbstractInputStream(ByteBufAbstract buffer) {
        this(buffer, buffer.readableBytes());
    }

    public ByteBufAbstractInputStream(ByteBufAbstract buffer, int length) {
        this(buffer, length, false);
    }

    public ByteBufAbstractInputStream(ByteBufAbstract buffer, boolean releaseOnClose) {
        this(buffer, buffer.readableBytes(), releaseOnClose);
    }

    public ByteBufAbstractInputStream(ByteBufAbstract buffer, int length, boolean releaseOnClose) {
        this.lineBuf = new StringBuilder();
        if (buffer == null) {
            throw new NullPointerException("buffer");
        } else if (length < 0) {
            if (releaseOnClose) {
                buffer.release();
            }

            throw new IllegalArgumentException("length: " + length);
        } else if (length > buffer.readableBytes()) {
            if (releaseOnClose) {
                buffer.release();
            }

            throw new IndexOutOfBoundsException("Too many bytes to be read - Needs " + length + ", maximum is " + buffer.readableBytes());
        } else {
            this.releaseOnClose = releaseOnClose;
            this.buffer = buffer;
            this.startIndex = buffer.readerIndex();
            this.endIndex = this.startIndex + length;
            buffer.markReaderIndex();
        }
    }

    public int readBytes() {
        return this.buffer.readerIndex() - this.startIndex;
    }

    public void close() throws IOException {
        try {
            super.close();
        } finally {
            if (this.releaseOnClose && !this.closed) {
                this.closed = true;
                this.buffer.release();
            }

        }

    }

    public int available() throws IOException {
        return this.endIndex - this.buffer.readerIndex();
    }

    public void mark(int readlimit) {
        this.buffer.markReaderIndex();
    }

    public boolean markSupported() {
        return true;
    }

    public int read() throws IOException {
        return !this.buffer.isReadable() ? -1 : this.buffer.readByte() & 255;
    }


    public int read(byte[] b, int off, int len) throws IOException {
        int available = this.available();
        if (available == 0) {
            return -1;
        } else {
            len = Math.min(available, len);
            this.buffer.readBytes(b, off, len);
            return len;
        }
    }

    public void reset() throws IOException {
        this.buffer.resetReaderIndex();
    }

    public long skip(long n) throws IOException {
        return n > 2147483647L ? (long) this.skipBytes(2147483647) : (long) this.skipBytes((int) n);
    }

    public boolean readBoolean() throws IOException {
        this.checkAvailable(1);
        return this.read() != 0;
    }

    public byte[] readBytes(int len) {
        byte[] bytes = new byte[len];
        buffer.readBytes(bytes);
        return bytes;
    }

    public byte readByte() throws IOException {
        if (!this.buffer.isReadable()) {
            throw new EOFException();
        } else {
            return this.buffer.readByte();
        }
    }

    public char readChar() throws IOException {
        return (char) this.readShort();
    }

    public double readDouble() throws IOException {
        return Double.longBitsToDouble(this.readLong());
    }

    public float readFloat() throws IOException {
        return Float.intBitsToFloat(this.readInt());
    }

    public void readFully(byte[] b) throws IOException {
        this.readFully(b, 0, b.length);
    }

    public void readFully(byte[] b, int off, int len) throws IOException {
        this.checkAvailable(len);
        this.buffer.readBytes(b, off, len);
    }

    public int readInt() throws IOException {
        this.checkAvailable(4);
        return this.buffer.readInt();
    }

    public String readLine() throws IOException {
        this.lineBuf.setLength(0);

        while (this.buffer.isReadable()) {
            int c = this.buffer.readUnsignedByte();
            switch (c) {
                case 13:
                    if (this.buffer.isReadable() && (char) this.buffer.getUnsignedByte(this.buffer.readerIndex()) == '\n') {
                        this.buffer.skipBytes(1);
                    }
                case 10:
                    return this.lineBuf.toString();
                default:
                    this.lineBuf.append((char) c);
            }
        }

        return this.lineBuf.length() > 0 ? this.lineBuf.toString() : null;
    }

    public long readLong() throws IOException {
        this.checkAvailable(8);
        return this.buffer.readLong();
    }

    public long[] readLongs(int size) throws IOException {
        long[] array = new long[size];

        for (int i = 0; i < array.length; i++) {
            array[i] = readLong();
        }
        return array;
    }

    public short readShort() throws IOException {
        this.checkAvailable(2);
        return this.buffer.readShort();
    }

    public String readUTF() throws IOException {
        return DataInputStream.readUTF(this);
    }

    public int readUnsignedByte() throws IOException {
        return this.readByte() & 255;
    }

    public int readUnsignedShort() throws IOException {
        return this.readShort() & '\uffff';
    }

    public int skipBytes(int n) throws IOException {
        int nBytes = Math.min(this.available(), n);
        this.buffer.skipBytes(nBytes);
        return nBytes;
    }

    private void checkAvailable(int fieldSize) throws IOException {
        if (fieldSize < 0) {
            throw new IndexOutOfBoundsException("fieldSize cannot be a negative number");
        } else if (fieldSize > this.available()) {
            throw new EOFException("fieldSize is too long! Length is " + fieldSize + ", but maximum is " + this.available());
        }
    }
}

