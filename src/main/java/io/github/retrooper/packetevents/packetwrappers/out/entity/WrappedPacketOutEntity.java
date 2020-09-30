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

package io.github.retrooper.packetevents.packetwrappers.out.entity;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import org.bukkit.entity.Entity;

import java.lang.reflect.Field;

public class WrappedPacketOutEntity extends WrappedPacket {
    private static Class<?> packetClass;
    private int entityID = -1;
    //Byte = 1.7.10->1.8.8, Int = 1.9->1.15.x, Short = 1.16.x
    private static byte mode = 0; //byte = 0, int = 1, short = 2
    private static double dXYZDivisor = 0.0;
    private static int yawByteIndex = 0;
    private static int pitchByteIndex = 1;
    private Entity entity;
    private boolean isListening = false;

    public WrappedPacketOutEntity(Object packet) {
        super(packet);
        isListening = true;
    }

    public static void load() {
        packetClass = PacketTypeClasses.Server.ENTITY;

        Field dxField = Reflection.getField(packetClass, 1);
        assert dxField != null;
        if (dxField.equals(Reflection.getField(packetClass, byte.class, 0))) {
            mode = 0;
            yawByteIndex = 3;
            pitchByteIndex = 4;
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

    /**
     * Get the pitch.
     *
     * @return Get Byte Pitch
     */
    public byte getPitch() {
        if (isListening) {
            return readByte(pitchByteIndex);
        } else {
            return 0;
        }
    }

    /**
     * Get the Yaw.
     *
     * @return Get Byte Yaw
     */
    public byte getYaw() {
        if (isListening) {
            return readByte(yawByteIndex);
        } else {
            return 0;
        }
    }

    /**
     * Get the Delta X
     *
     * @return Delta X
     */
    public double getDeltaX() {
        if (isListening) {
            switch (mode) {
                case 0:
                    return readByte(0) / dXYZDivisor;
                case 1:
                    return readInt(1) / dXYZDivisor;
                case 2:
                    return readShort(0) / dXYZDivisor;
            }
        } else {

        }
        return 0.0;
    }

    /**
     * Get the Delta Y
     *
     * @return Delta Y
     */
    public double getDeltaY() {
        if (isListening) {
            switch (mode) {
                case 0:
                    return readByte(1) / dXYZDivisor;
                case 1:
                    return readInt(2) / dXYZDivisor;
                case 2:
                    return readShort(1) / dXYZDivisor;
            }
        } else {

        }
        return 0.0;
    }

    /**
     * Get the Delta Z
     *
     * @return Delta Z
     */
    public double getDeltaZ() {
        if (isListening) {
            switch (mode) {
                case 0:
                    return readByte(2) / dXYZDivisor;
                case 1:
                    return readInt(3) / dXYZDivisor;
                case 2:
                    return readShort(2) / dXYZDivisor;
            }
        } else {

        }
        return 0.0;
    }

    /**
     * Lookup the associated entity by the ID that was sent in the packet.
     *
     * @return Entity
     */
    public Entity getEntity() {
        if (entity != null) {
            return entity;
        }
        return entity = NMSUtils.getEntityById(getEntityID());
    }

    /**
     * Get the ID of the entity.
     * If you do not want to use {@link #getEntity()},
     * you lookup the entity by yourself with this entity ID.
     *
     * @return Entity ID
     */
    public int getEntityID() {
        if (entityID != -1) {
            return entityID;
        } else {
            return entityID = readInt(0);
        }
    }

    /**
     * Is the entity on the ground?
     *
     * @return On Ground
     */
    public boolean isOnGround() {
        if (isListening) {
            return readBoolean(0);
        } else {

        }
        return false;
    }
}
