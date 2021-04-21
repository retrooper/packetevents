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

package io.github.retrooper.packetevents.packetwrappers.play.out.entitydestroy;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author yanjulang
 * @since 1.8
 */
public class WrappedPacketOutEntityDestroy extends WrappedPacket implements SendableWrapper {

    private static Constructor<?> packetConstructor;
    private int[] entityIds;
    private Entity[] entities;
    public WrappedPacketOutEntityDestroy(NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutEntityDestroy(int... entityIds) {
        this.entityIds = entityIds;
    }

    public WrappedPacketOutEntityDestroy(Entity... entities) {
        setEntities(entities);
    }

    @Override
    protected void load() {
        try {
            packetConstructor =
                    PacketTypeClasses.Play.Server.ENTITY_DESTROY.getConstructor(int[].class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public int[] getEntityIds() {
        if (packet != null && entityIds == null) {
            return entityIds = readIntArray(0);
        } else {
            return entityIds;
        }
    }

    public void setEntityIds(int... entityIds) {
        this.entityIds = entityIds;
        if (packet != null) {
            writeIntArray(0, entityIds);
        }
    }

    public Entity[] getEntities(@Nullable World world) {
        if (packet != null && entities == null) {
            int[] entityIDs = getEntityIds();
            entities = new Entity[entityIDs.length];
            for (int i = 0; i < entityIDs.length; i++) {
                entities[i] = NMSUtils.getEntityById(world, entityIDs[i]);
            }
        }
        return entities;
    }

    public Entity[] getEntities() {
        return getEntities(null);
    }

    public void setEntities(Entity... entities) {
        this.entityIds = new int[entities.length];
        this.entities = entities;
        for (int i = 0; i < entities.length; i++) {
            Entity entity = entities[i];
            entityIds[i] = entity.getEntityId();
        }
        if (packet != null) {
            writeIntArray(0, entityIds);
        }
    }

    @Override
    public Object asNMSPacket() {
        try {
            return packetConstructor.newInstance((Object) entityIds);
        } catch (InstantiationException | InvocationTargetException
                | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}