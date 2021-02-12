/*
 * MIT License
 *
 * Copyright (c) 2020 retrooper
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.retrooper.packetevents.exceptions;

import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.reflection.ClassUtil;

/**
 * An exception thrown by PacketEvents when a wrapper is used although it isn't supported on the local server version.
 * Make sure to decompile a wrapper before using it, if you are unsure if it is supported through out all the server versions.
 * We override a {@link WrappedPacket#isSupported()} method if it isn't supported on all server versions.
 *
 * @author retrooper
 * @see WrappedPacket#isSupported()
 * @see WrappedPacket.SupportedVersions
 * @since 1.7
 */
public class WrapperUnsupportedUsageException extends RuntimeException {
    public WrapperUnsupportedUsageException(String message) {
        super(message);
    }

    public WrapperUnsupportedUsageException(Class<? extends WrappedPacket> wrapperClass) {
        this("You are using a packet wrapper which happens to be unsupported on the local server version. Packet wrapper you attempted to use: " + ClassUtil.getClassSimpleName(wrapperClass));
    }
}
