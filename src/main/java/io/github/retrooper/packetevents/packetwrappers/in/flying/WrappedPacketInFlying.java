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

package io.github.retrooper.packetevents.packetwrappers.in.flying;


import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketInFlying extends WrappedPacket {
    public WrappedPacketInFlying(Object packet) {
        super(packet);
    }

    public double getX() {
        return readDouble(0);
    }

    public double getY() {
        return readDouble(1);
    }

    public double getZ() {
        return readDouble(2);
    }

    public float getYaw() {
        return readFloat(0);
    }

    public float getPitch() {
        return readFloat(1);
    }

    public boolean isOnGround() {
        return readBoolean(0);
    }

    public final boolean isPosition() {
        return readBoolean(1);
    }

    public final boolean isLook() {
        return readBoolean(2);
    }
}
