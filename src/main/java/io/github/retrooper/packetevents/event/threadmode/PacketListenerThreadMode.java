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

package io.github.retrooper.packetevents.event.threadmode;

/**
 * What threads should PacketEvents use to process your dynamic packet event listener?
 * By default it will process them on the netty threads, but you may have PacketEvents process them
 * on PacketEvents' own allocated threads.
 * The fixed thread count of the threads can be assigned in the PacketEventsSettings with the
 * {@link io.github.retrooper.packetevents.settings.PacketEventsSettings#packetProcessingThreadCount(int)} setting
 */
public enum PacketListenerThreadMode {
    /**
     * Your listener's event3
     */
    NETTY((byte)0),

    PACKETEVENTS((byte)1);

    private final byte id;
    PacketListenerThreadMode(final byte id) {
        this.id = id;
    }

    public final byte getId() {
        return id;
    }
}
