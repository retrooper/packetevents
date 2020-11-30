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

package io.github.retrooper.packetevents.packetwrappers.in.keepalive;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.reflection.Reflection;

public final class WrappedPacketInKeepAlive extends WrappedPacket {
    private static boolean integerPresentInIndex0;

    public WrappedPacketInKeepAlive(final Object packet) {
        super(packet);
    }

    public static void load() {
        Class<?> packetClass = PacketTypeClasses.Client.KEEP_ALIVE;
        integerPresentInIndex0 = Reflection.getField(packetClass, int.class, 0) != null;
    }

    /**
     * Get the ID response from the client.
     * <p>
     * You may cast this down to an int if you are on 1.7.10 - 1.12.2.
     * On 1.13.2 - 1.16.3 a long is sent.
     *
     * @return response ID
     */
    public long getId() {
        if (!integerPresentInIndex0) {
            return readLong(0);
        } else {
            return readInt(0);
        }
    }
}
