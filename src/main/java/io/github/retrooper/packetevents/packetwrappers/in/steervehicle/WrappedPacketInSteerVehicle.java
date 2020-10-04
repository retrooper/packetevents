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

package io.github.retrooper.packetevents.packetwrappers.in.steervehicle;

import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketInSteerVehicle extends WrappedPacket {
    public WrappedPacketInSteerVehicle(Object packet) {
        super(packet);
    }

    /**
     * Get the side value.
     * <p>
     * If positive, they are moving to the left, if negative, they are moving to the right.
     *
     * @return Side Value
     */
    public float getSideValue() {
        return readFloat(0);
    }

    /**
     * Get the forward value.
     * <p>
     * If positive, they are moving forward, if negative, they are moving backwards.
     *
     * @return Forward Value
     */
    public float getForwardValue() {
        return readFloat(1);
    }

    /**
     * Is a Jump
     *
     * @return Is Jump
     */
    public boolean isJump() {
        return readBoolean(0);
    }

    /**
     * Is an unmount
     *
     * @return Is Unmounting
     */
    public boolean isUnmount() {
        return readBoolean(1);
    }
}
