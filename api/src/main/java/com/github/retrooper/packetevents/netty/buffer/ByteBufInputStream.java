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

package com.github.retrooper.packetevents.netty.buffer;

import com.github.retrooper.packetevents.exception.PacketProcessException;

import java.io.*;
//TODO give netty credit
public class ByteBufInputStream extends InputStream implements DataInput {
    private final Object buffer;
    private final int startIndex;
    private final int endIndex;
    private final boolean releaseOnClose;
    private final StringBuilder lineBuf;
    private boolean closed;

    public ByteBufInputStream(Object buffer) {
        this(buffer, ByteBufHelper.readableBytes(buffer));
    }

    public ByteBufInputStream(Object buffer, int length) {
        this(buffer, length, false);
    }

    public ByteBufInputStream(Object buffer, boolean releaseOnClose) {
        this(buffer, ByteBufHelper.readableBytes(buffer), releaseOnClose);
    }

    public ByteBufInputStream(Object buffer, int maxLength, boolean releaseOnClose) {
        this.lineBuf = new StringBuilder();
        if (buffer == null) {
            throw new NullPointerException("buffer");
        } else if (maxLength < 0) {
            if (releaseOnClose) {
                ByteBufHelper.release(buffer);
            }

            throw new IllegalArgumentException("maxLength: " + maxLength);
        } else if (ByteBufHelper.readableBytes(buffer) > maxLength) {
            if (releaseOnClose) {
                ByteBufHelper.release(buffer);
            }

            throw new IndexOutOfBoundsException("Too many bytes to be read - Found " + ByteBufHelper.readableBytes(buffer) + ", maximum is " + maxLength);
        } else {
            this.releaseOnClose = releaseOnClose;
            this.buffer = buffer;
            this.startIndex = ByteBufHelper.readerIndex(buffer);
            this.endIndex = this.startIndex + ByteBufHelper.readableBytes(buffer);
            ByteBufHelper.markReaderIndex(buffer);
        }
    }

    public int readBytes() {
        return ByteBufHelper.readerIndex(buffer) - this.startIndex;
    }

    public void close() throws IOException {
        try {
            super.close();
        } finally {
            if (this.releaseOnClose && !this.closed) {
                this.closed = true;
                ByteBufHelper.release(buffer);
            }

        }

    }

    public int available() throws IOException {
        return this.endIndex - ByteBufHelper.readerIndex(buffer);
    }

    public void mark(int readlimit) {
        ByteBufHelper.markReaderIndex(buffer);
    }

    public boolean markSupported() {
        return true;
    }

    public int read() throws IOException {
        return !(ByteBufHelper.isReadable(buffer)) ? -1 : ByteBufHelper.readByte(buffer) & 255;
    }


    public int read(byte[] b, int off, int len) throws IOException {
        int available = this.available();
        if (available == 0) {
            return -1;
        } else {
            len = Math.min(available, len);
            ByteBufHelper.readBytes(buffer, b, off, len);
            return len;
        }
    }

    public void reset() throws IOException {
        ByteBufHelper.resetReaderIndex(buffer);
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
        ByteBufHelper.readBytes(buffer, bytes);
        return bytes;
    }

    public byte readByte() throws IOException {
        if (!(ByteBufHelper.isReadable(buffer))) {
            throw new EOFException();
        } else {
            return ByteBufHelper.readByte(buffer);
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
        ByteBufHelper.readBytes(buffer, b, off, len);
    }

    public int readInt() throws IOException {
        this.checkAvailable(4);
        return ByteBufHelper.readInt(buffer);
    }

    public String readLine() throws IOException {
        this.lineBuf.setLength(0);

        while (ByteBufHelper.isReadable(buffer)) {
            int c = ByteBufHelper.readUnsignedByte(buffer);
            switch (c) {
                case 13:
                    if (ByteBufHelper.isReadable(buffer)
                            && (char) ByteBufHelper.getUnsignedByte(buffer, ByteBufHelper.readerIndex(buffer)) == '\n') {
                        ByteBufHelper.skipBytes(buffer, 1);
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
        return ByteBufHelper.readLong(buffer);
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
        return ByteBufHelper.readShort(buffer);
    }

    public String readUTF() throws IOException {
        String text = DataInputStream.readUTF(this);
        return text;
    }

    public int readUnsignedByte() throws IOException {
        return this.readByte() & 255;
    }

    public int readUnsignedShort() throws IOException {
        return this.readShort() & '\uffff';
    }

    public int skipBytes(int n) throws IOException {
        int nBytes = Math.min(this.available(), n);
        ByteBufHelper.skipBytes(buffer, nBytes);
        return nBytes;
    }

    private void checkAvailable(int fieldSize) throws IOException {
        if (fieldSize < 0) {
            throw new IndexOutOfBoundsException("fieldSize cannot be a negative number");
        } else if (fieldSize > this.available()) {
            int value = this.available();
            String msg = "fieldSize is too long! Length is " + fieldSize + ", but maximum is " + value;
            if (value == 0) {
                throw new PacketProcessException(msg);
            }
            else {
                throw new EOFException(msg);
            }
        }
    }
}

