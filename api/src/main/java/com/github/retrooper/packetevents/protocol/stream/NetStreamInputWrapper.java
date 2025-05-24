/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2025 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.stream;

import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class NetStreamInputWrapper extends NetStreamInput {

    private final PacketWrapper<?> wrapper;

    public NetStreamInputWrapper(PacketWrapper<?> wrapper) {
        super(null);
        this.wrapper = wrapper;
    }

    @Override
    public int read() {
        return this.wrapper.readUnsignedByte();
    }

    @Override
    public int read(byte[] b) {
        return this.read(b, 0, b.length);
    }

    @Override
    public int read(byte[] b, int off, int len) {
        int ri = ByteBufHelper.readerIndex(this.wrapper.buffer);
        ByteBufHelper.readBytes(this.wrapper.buffer, b, off, len);
        return ByteBufHelper.readerIndex(this.wrapper.buffer) - ri;
    }

    @Override
    public long skip(long n) {
        int ri = ByteBufHelper.readerIndex(this.wrapper.buffer);
        ByteBufHelper.skipBytes(this.wrapper.buffer, (int) n);
        return ByteBufHelper.readerIndex(this.wrapper.buffer) - ri;
    }

    @Override
    public int available() {
        return ByteBufHelper.readableBytes(this.wrapper.buffer);
    }

    @Override
    public void close() {
        // NO-OP
    }

    @Override
    public void mark(int readlimit) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean markSupported() {
        return false;
    }

    public PacketWrapper<?> getWrapper() {
        return this.wrapper;
    }
}
