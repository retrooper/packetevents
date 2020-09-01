/**
MIT License

Copyright (c) 2020 retrooper

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package io.github.retrooper.packetevents.packetwrappers.out.entity;

import io.github.retrooper.packetevents.packet.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.reflectionutils.Reflection;
import io.github.retrooper.packetevents.utils.NMSUtils;
import org.bukkit.entity.Entity;

import java.lang.reflect.Field;

public class WrappedPacketOutEntity extends WrappedPacket {
    private static Class<?> packetClass;
    //Byte = 1.7.10->1.8.8, Int = 1.9->1.15.x, Short = 1.16.x
    private static byte mode = 0; //byte = 0, int = 1, short = 2
    private static double dXYZDivisor = 0.0;
    private int entityID;
    private double deltaX, deltaY, deltaZ;
    private byte yaw, pitch;
    private boolean onGround;
    public WrappedPacketOutEntity(Object packet) {
        super(packet);
    }

    public static void load() {
        packetClass = PacketTypeClasses.Server.ENTITY;

        Field dxField = Reflection.getField(packetClass, 1);
        assert dxField != null;
        if (dxField.equals(Reflection.getField(packetClass, byte.class, 0))) {
            mode = 0;
        } else if (dxField.equals(Reflection.getField(packetClass, int.class, 1))) {
            mode = 1;
        } else if (dxField.equals(Reflection.getField(packetClass, short.class, 0))) {
            mode = 2;
        }

        if (mode == 0) {
            dXYZDivisor = 32.0;
        } else {
            dXYZDivisor = 4096.0;
        }

    }

    @Override
    protected void setup() {
        try {
            this.entityID = Reflection.getField(packetClass, int.class, 0).getInt(packet);
            this.onGround = Reflection.getField(packetClass, boolean.class, 0).getBoolean(packet);
            int dX = 1, dY = 1, dZ = 1;
            switch (mode) {
                case 0:
                    dX = Reflection.getField(packetClass, byte.class, 0).getByte(packet);
                    dY = Reflection.getField(packetClass, byte.class, 1).getByte(packet);
                    dZ = Reflection.getField(packetClass, byte.class, 2).getByte(packet);
                    this.yaw = Reflection.getField(packetClass, byte.class, 3).getByte(packet);
                    this.pitch = Reflection.getField(packetClass, byte.class, 4).getByte(packet);
                    break;
                case 1:
                    dX = Reflection.getField(packetClass, int.class, 1).getInt(packet);
                    dY = Reflection.getField(packetClass, int.class, 2).getInt(packet);
                    dZ = Reflection.getField(packetClass, int.class, 3).getInt(packet);
                    this.yaw = Reflection.getField(packetClass, byte.class, 0).getByte(packet);
                    this.pitch = Reflection.getField(packetClass, byte.class, 1).getByte(packet);
                    break;
                case 2:
                    dX = Reflection.getField(packetClass, short.class, 0).getShort(packet);
                    dY = Reflection.getField(packetClass, short.class, 1).getShort(packet);
                    dZ = Reflection.getField(packetClass, short.class, 2).getShort(packet);
                    this.yaw = Reflection.getField(packetClass, byte.class, 0).getByte(packet);
                    this.pitch = Reflection.getField(packetClass, byte.class, 1).getByte(packet);
                    break;
            }
            this.deltaX = dX / dXYZDivisor;
            this.deltaY = dY / dXYZDivisor;
            this.deltaZ = dZ / dXYZDivisor;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public byte getPitch() {
        return pitch;
    }

    public byte getYaw() {
        return yaw;
    }

    public double getDeltaZ() {
        return deltaZ;
    }

    public double getDeltaY() {
        return deltaY;
    }

    public double getDeltaX() {
        return deltaX;
    }

    public Entity getEntity() {
        return NMSUtils.getEntityById(entityID);
    }

    public int getEntityId() {
        return entityID;
    }

    public boolean isOnGround() {
        return onGround;
    }
}
