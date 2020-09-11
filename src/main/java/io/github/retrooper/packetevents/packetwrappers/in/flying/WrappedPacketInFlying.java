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


import io.github.retrooper.packetevents.packet.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketInFlying extends WrappedPacket {
    private static Class<?> flyingClass;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private boolean onGround;
    private boolean isPosition;
    private boolean isLook;
    public WrappedPacketInFlying(Object packet) {
        super(packet);
    }

    public static void load() {
        flyingClass = PacketTypeClasses.Client.FLYING;
    }

    @Override
    protected void setup() {
        x = readDouble(0);
        y = readDouble(1);
        z = readDouble(2);

        yaw = readFloat(0);
        pitch = readFloat(1);

        onGround = readBoolean(0);

        isPosition = readBoolean(1);
        isLook = readBoolean(2);
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

    public boolean isOnGround() {
        return onGround;
    }

    public final boolean isPosition() {
        return isPosition;
    }

    public final boolean isLook() {
        return isLook;
    }

    public static class WrappedPacketInPosition extends WrappedPacket {
        private double x;
        private double y;
        private double z;
        private boolean onGround;
        public WrappedPacketInPosition(Object packet) {
            super(packet);
        }

        @Override
        protected void setup() {
            x = readDouble(0);
            y = readDouble(1);
            z = readDouble(2);
            onGround = readBoolean(0);
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

        public boolean isOnGround() {
            return onGround;
        }
    }

    public static class WrappedPacketInPosition_Look extends WrappedPacket {
        private double x;
        private double y;
        private double z;
        private float yaw;
        private float pitch;
        private boolean onGround;
        public WrappedPacketInPosition_Look(Object packet) {
            super(packet);
        }

        @Override
        protected void setup() {
            x = readDouble(0);
            y = readDouble(1);
            z = readDouble(2);
            yaw = readFloat(0);
            pitch = readFloat(1);
            onGround = readBoolean(0);
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

        public boolean isOnGround() {
            return onGround;
        }
    }

    public static class WrappedPacketInLook extends WrappedPacketInFlying {
        private float yaw;
        private float pitch;
        private boolean onGround;
        public WrappedPacketInLook(Object packet) {
            super(packet);
        }

        @Override
        protected void setup() {
            yaw = readFloat(0);
            pitch = readFloat(1);
            onGround = readBoolean(0);
        }

        public float getYaw() {
            return yaw;
        }

        public float getPitch() {
            return pitch;
        }

        public boolean isOnGround() {
            return onGround;
        }
    }

}
