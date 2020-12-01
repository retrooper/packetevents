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

package io.github.retrooper.packetevents.packetwrappers.play.out.entitystatus;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import org.bukkit.entity.Entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class WrappedPacketOutEntityStatus extends WrappedPacket implements SendableWrapper {
    private static Constructor<?> packetConstructor;
    private Entity entity;
    private byte status;
    private int entityID = -1;
    public WrappedPacketOutEntityStatus(Object packet) {
        super(packet);
    }

    public WrappedPacketOutEntityStatus(Entity entity, byte status) {
        this.entityID = entity.getEntityId();
        this.entity = entity;
        this.status = status;
    }

    public WrappedPacketOutEntityStatus(int entityID, byte status) {
        this.entityID = entityID;
        this.status = status;
    }

    public static void load() {
        try {
            packetConstructor =
                    PacketTypeClasses.Server.ENTITY_STATUS.getConstructor(NMSUtils.nmsEntityClass, byte.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public int getEntityId() {
        if (packet == null) {
            if (entityID != -1) {
                return entityID;
            }
            return entityID = readInt(0);
        } else {
            return entityID;
        }
    }

    public Entity getEntity() {
        if (entity == null) {
            return entity = NMSUtils.getEntityById(getEntityId());
        }
        return entity;
    }

    public byte getEntityStatus() {
        if (packet == null) {
            return status;
        } else {
            return readByte(0);
        }
    }

    @Override
    public Object asNMSPacket() {
        try {
            return packetConstructor.newInstance(NMSUtils.getNMSEntity(getEntity()), status);
        } catch (InstantiationException | InvocationTargetException
                | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
