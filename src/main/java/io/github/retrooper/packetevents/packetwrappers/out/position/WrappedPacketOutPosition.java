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

import io.github.retrooper.packetevents.packet.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.reflectionutils.Reflection;

public final class WrappedPacketOutPosition extends WrappedPacket {
    private static Class<?> packetClass;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    public WrappedPacketOutPosition(Object packet) {
        super(packet);
    }

    public static void load() {
        packetClass = PacketTypeClasses.Server.POSITION;
    }

    @Override
    protected void setup() {
        //TODO boolean onGround and flags
        try {
            this.x = Reflection.getField(packetClass, double.class, 0).getDouble(packet);
            this.y = Reflection.getField(packetClass, double.class, 1).getDouble(packet);
            this.z = Reflection.getField(packetClass, double.class, 2).getDouble(packet);

            this.yaw = Reflection.getField(packetClass, float.class, 0).getFloat(packet);
            this.pitch = Reflection.getField(packetClass, float.class, 1).getFloat(packet);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }
}
