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

import io.github.retrooper.packetevents.packet.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.reflectionutils.Reflection;

public class WrappedPacketInSteerVehicle extends WrappedPacket {
    private static Class<?> packetClass;
    private float side, forward;
    private boolean jump, unmount;
    public WrappedPacketInSteerVehicle(Object packet) {
        super(packet);
    }

    public static void load() {
        packetClass = PacketTypeClasses.Client.STEER_VEHICLE;
    }

    @Override
    protected void setup() {
        try {
            this.side = Reflection.getField(packetClass, float.class, 0).getFloat(packet);
            this.forward = Reflection.getField(packetClass, float.class, 1).getFloat(packet);

            this.jump = Reflection.getField(packetClass, boolean.class, 0).getBoolean(packet);
            this.unmount = Reflection.getField(packetClass, boolean.class, 1).getBoolean(packet);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    //Positive side value means left, negative means right
    public float getSideValue() {
        return side;
    }

    //Positive forward value means forward, negative is backwards
    public float getForwardValue() {
        return forward;
    }

    public boolean isJump() {
        return jump;
    }

    public boolean isUnmount() {
        return unmount;
    }
}
