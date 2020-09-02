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

import io.github.retrooper.packetevents.packet.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.reflectionutils.Reflection;

public final class WrappedPacketInKeepAlive extends WrappedPacket {
    private static Class<?> packetClass;
    private static boolean integerPresentInIndex0;
    private long id;

    public WrappedPacketInKeepAlive(final Object packet) {
        super(packet);
    }

    public static void load() {
        packetClass = PacketTypeClasses.Client.KEEP_ALIVE;
        integerPresentInIndex0 = Reflection.getField(packetClass, int.class, 0) != null;
    }

    @Override
    protected void setup() {
        if (!integerPresentInIndex0) {
            try {
                this.id = Reflection.getField(packetClass, long.class, 0).getLong(packet);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            try {
                this.id = Reflection.getField(packetClass, int.class, 0).getInt(packet);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Optionally Cast this long to an integer if you are using 1.7.10 -&gt; 1.12.2!
     * In 1.13.2 -&gt; 1.16.1 a long is sent
     *
     * @return response is
     */
    public long getId() {
        return id;
    }
}
