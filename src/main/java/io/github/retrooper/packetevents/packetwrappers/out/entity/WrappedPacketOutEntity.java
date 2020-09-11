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

import io.github.retrooper.packetevents.packet.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.github.retrooper.packetevents.utils.NMSUtils;
import org.bukkit.entity.Entity;

import java.lang.reflect.Field;

public class WrappedPacketOutEntity extends WrappedPacket {
    private static Class<?> packetClass;
    //Byte = 1.7.10->1.8.8, Int = 1.9->1.15.x, Short = 1.16.x
    private static byte mode = 0; //byte = 0, int = 1, short = 2
    private static double dXYZDivisor = 0.0;
    private Entity entity;
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
        this.entityID = readInt(0);
        this.onGround = readBoolean(0);
        int dX = 1, dY = 1, dZ = 1;
        switch (mode) {
            case 0:
                dX = readByte(0);
                dY = readByte(1);
                dZ = readByte(2);
                this.yaw = readByte(3);
                this.pitch = readByte(4);
                break;
            case 1:
                dX = readInt(1);
                dY = readInt(2);
                dZ = readInt(3);
                this.yaw = readByte(0);
                this.pitch = readByte(1);
                break;
            case 2:
                dX = readShort(0);
                dY = readShort(1);
                dZ = readShort(2);
                this.yaw = readByte(0);
                this.pitch = readByte(1);
                break;
        }
        this.deltaX = dX / dXYZDivisor;
        this.deltaY = dY / dXYZDivisor;
        this.deltaZ = dZ / dXYZDivisor;
    }

    /**
     * Get the pitch.
     *
     * @return Get Byte Pitch
     */
    public byte getPitch() {
        return pitch;
    }

    /**
     * Get the Yaw.
     *
     * @return Get Byte Yaw
     */
    public byte getYaw() {
        return yaw;
    }

    /**
     * Get the Delta X
     *
     * @return Delta X
     */
    public double getDeltaX() {
        return deltaX;
    }

    /**
     * Get the Delta Y
     *
     * @return Delta Y
     */
    public double getDeltaY() {
        return deltaY;
    }

    /**
     * Get the Delta Z
     *
     * @return Delta Z
     */
    public double getDeltaZ() {
        return deltaZ;
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
        return entity = NMSUtils.getEntityById(this.entityID);
    }

    /**
     * Get the ID of the entity.
     * If you do not want to use {@link #getEntity()},
     * you lookup the entity by yourself with this entity ID.
     *
     * @return Entity ID
     */
    public int getEntityId() {
        return entityID;
    }

    /**
     * Is the entity on the ground?
     *
     * @return On Ground
     */
    public boolean isOnGround() {
        return onGround;
    }

    public static class WrappedPacketOutRelEntityMove extends WrappedPacket {
        private int entityID;
        private Entity entity;
        private double deltaX, deltaY, deltaZ;
        private boolean onGround;

        public WrappedPacketOutRelEntityMove(Object packet) {
            super(packet);
        }

        @Override
        protected void setup() {
            int dX = 1, dY = 1, dZ = 1;
            switch (mode) {
                case 0:
                    dX = readByte(0);
                    dY = readByte(1);
                    dZ = readByte(2);
                    break;
                case 1:
                    dX = readInt(1);
                    dY = readInt(2);
                    dZ = readInt(3);
                    break;
                case 2:
                    dX = readShort(0);
                    dY = readShort(1);
                    dZ = readShort(2);
                    break;
            }
            deltaX = dX / dXYZDivisor;
            deltaY = dY / dXYZDivisor;
            deltaZ = dZ / dXYZDivisor;
        }

        /**
         * Get the Delta X
         *
         * @return Delta X
         */
        public double getDeltaX() {
            return deltaX;
        }

        /**
         * Get the Delta Y
         *
         * @return Delta Y
         */
        public double getDeltaY() {
            return deltaY;
        }

        /**
         * Get the Delta Z
         *
         * @return Delta Z
         */
        public double getDeltaZ() {
            return deltaZ;
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
            return entity = NMSUtils.getEntityById(this.entityID);
        }

        /**
         * Get the ID of the entity.
         * If you do not want to use {@link #getEntity()},
         * you lookup the entity by yourself with this entity ID.
         *
         * @return Entity ID
         */
        public int getEntityId() {
            return entityID;
        }

        /**
         * Is the entity on the ground?
         *
         * @return On Ground
         */
        public boolean isOnGround() {
            return onGround;
        }
    }

    public static class WrappedPacketOutRelEntityMoveLook extends WrappedPacket {
        private int entityID;
        private Entity entity;
        private double deltaX, deltaY, deltaZ;
        private byte yaw, pitch;
        private boolean onGround;

        public WrappedPacketOutRelEntityMoveLook(Object packet) {
            super(packet);
        }

        @Override
        protected void setup() {
            entityID = readInt(0);
            onGround = readBoolean(0);
            int dX = 1, dY = 1, dZ = 1;
            switch (mode) {
                case 0:
                    dX = readByte(0);
                    dY = readByte(1);
                    dZ = readByte(2);
                    yaw = readByte(3);
                    pitch = readByte(4);
                    break;
                case 1:
                    dX = readInt(1);
                    dY = readInt(2);
                    dZ = readInt(3);
                    yaw = readByte(0);
                    pitch = readByte(1);
                    break;
                case 2:
                    dX = readShort(0);
                    dY = readShort(1);
                    dZ = readShort(2);
                    yaw = readByte(0);
                    pitch = readByte(1);
                    break;
            }
            deltaX = dX / dXYZDivisor;
            deltaY = dY / dXYZDivisor;
            deltaZ = dZ / dXYZDivisor;
        }

        /**
         * Get the pitch.
         *
         * @return Get Byte Pitch
         */
        public byte getPitch() {
            return pitch;
        }

        /**
         * Get the Yaw.
         *
         * @return Get Byte Yaw
         */
        public byte getYaw() {
            return yaw;
        }

        /**
         * Get the Delta X
         *
         * @return Delta X
         */
        public double getDeltaX() {
            return deltaX;
        }

        /**
         * Get the Delta Y
         *
         * @return Delta Y
         */
        public double getDeltaY() {
            return deltaY;
        }

        /**
         * Get the Delta Z
         *
         * @return Delta Z
         */
        public double getDeltaZ() {
            return deltaZ;
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
            return entity = NMSUtils.getEntityById(this.entityID);
        }

        /**
         * Get the ID of the entity.
         * If you do not want to use {@link #getEntity()},
         * you lookup the entity by yourself with this entity ID.
         *
         * @return Entity ID
         */
        public int getEntityId() {
            return entityID;
        }

        /**
         * Is the entity on the ground?
         *
         * @return On Ground
         */
        public boolean isOnGround() {
            return onGround;
        }
    }

    public static class WrappedPacketOutRelEntityLook extends WrappedPacket {
        private int entityID;
        private Entity entity;
        private byte yaw, pitch;
        private boolean onGround;

        public WrappedPacketOutRelEntityLook(Object packet) {
            super(packet);
        }

        @Override
        protected void setup() {
            entityID = readInt(0);
            onGround = readBoolean(0);
            switch (mode) {
                case 0:
                    yaw = readByte(3);
                    pitch = readByte(4);
                    break;
                case 1:
                case 2:
                    yaw = readByte(0);
                    pitch = readByte(1);
                    break;
            }
        }

        /**
         * Get the pitch.
         *
         * @return Get Byte Pitch
         */
        public byte getPitch() {
            return pitch;
        }

        /**
         * Get the Yaw.
         *
         * @return Get Byte Yaw
         */
        public byte getYaw() {
            return yaw;
        }

        /**
         * Is the entity on the ground?
         *
         * @return On Ground
         */
        public boolean isOnGround() {
            return onGround;
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
            return entity = NMSUtils.getEntityById(this.entityID);
        }

        /**
         * Get the ID of the entity.
         * If you do not want to use {@link #getEntity()},
         * you lookup the entity by yourself with this entity ID.
         *
         * @return Entity ID
         */
        public int getEntityId() {
            return entityID;
        }
    }
}
