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

package io.github.retrooper.packetevents.packetwrappers.out.position;

import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;

public final class WrappedPacketOutPosition extends WrappedPacket {
    private final boolean isListening = true;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    public WrappedPacketOutPosition(Object packet) {
        super(packet);
    }

    public static void load() {
    }

    @Override
    protected void setup() {
        //TODO boolean onGround and flags
        this.x = readDouble(0);
        this.y = readDouble(1);
        this.z = readDouble(2);

        this.yaw = readFloat(0);
        this.pitch = readFloat(1);
    }

    /**
     * Get the X position.
     *
     * @return Get X Position
     */
    public double getX() {
        if (isListening) {
            return readDouble(0);
        } else {
            return x;
        }
    }

    /**
     * Get the Y position.
     *
     * @return Get Y Position
     */
    public double getY() {
        if (isListening) {
            return readDouble(1);
        } else {
            return y;
        }
    }

    /**
     * Get the Z position.
     *
     * @return Get Z Position
     */
    public double getZ() {
        if (isListening) {
            return readDouble(2);
        } else {
            return z;
        }
    }

    /**
     * Get the Yaw.
     *
     * @return Get Yaw
     */
    public float getYaw() {
        if (isListening) {
            return readFloat(0);
        } else {
            return yaw;
        }
    }

    /**
     * Get the Pitch,
     *
     * @return Get Pitch
     */
    public float getPitch() {
        if (isListening) {
            return readFloat(1);
        } else {
            return pitch;
        }
    }
}
