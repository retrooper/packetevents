/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2016-2021 retrooper and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.retrooper.packetevents.packetwrappers.play.out.entitydestroy;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;

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
    public Object asNMSPacket() throws Exception {
        return packetConstructor.newInstance(entityIds);
    }
}