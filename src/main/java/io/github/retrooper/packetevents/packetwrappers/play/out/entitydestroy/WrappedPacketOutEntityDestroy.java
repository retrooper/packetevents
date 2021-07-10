/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
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
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import it.unimi.dsi.fastutil.ints.IntList;
import org.bukkit.entity.Entity;

import java.lang.reflect.Constructor;
import java.util.Optional;

/**
 * @author yanjulang
 * @since 1.8
 */
public class WrappedPacketOutEntityDestroy extends WrappedPacketEntityAbstraction implements SendableWrapper {
    private static boolean v_1_17;
    private static boolean v_1_17_1;
    private static Constructor<?> packetConstructor;
    private int[] entityIds;

    public WrappedPacketOutEntityDestroy(NMSPacket packet) {
        super(packet);
    }

    @Deprecated
    public WrappedPacketOutEntityDestroy(int... entityIds) {
        this.entityIds = entityIds;
    }

    public WrappedPacketOutEntityDestroy(int entityID) {
        setEntityId(entityID);
    }

    public WrappedPacketOutEntityDestroy(Entity entity) {
        setEntity(entity);
    }

    @Override
    protected void load() {
        v_1_17 = version.isNewerThanOrEquals(ServerVersion.v_1_17);
        v_1_17_1 = version.isNewerThanOrEquals(ServerVersion.v_1_17_1);
        try {
            if (v_1_17) {
                packetConstructor =
                        PacketTypeClasses.Play.Server.ENTITY_DESTROY.getConstructor(int.class);
            }
            else {
                packetConstructor =
                        PacketTypeClasses.Play.Server.ENTITY_DESTROY.getConstructor(int[].class);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getEntityId() {
        if (entityID != -1 || packet == null) {
            if (v_1_17) {
                return entityID;
            }
            else {
                return entityIds[0];
            }
        }
        if (v_1_17_1) {
            IntList list = readObject(0, IntList.class);
            entityID = list.get(0);
        }
        else if (v_1_17) {
            entityID = readInt(0);
        }
        else {
            entityID = readIntArray(0)[0];
        }
        return entityID;
    }

    @Override
    public void setEntityId(int entityID) {
        if (packet != null) {
            if (v_1_17) {
                writeInt(0, this.entityID = entityID);
            }
            else {
                this.entityIds = new int[] {entityID};
                writeIntArray(0, new int[]{this.entityIds[0]});
            }
        } else {
            if (v_1_17) {
                this.entityID = entityID;
            }
            else {
                this.entityIds = new int[] {entityID};
            }
        }
        this.entity = null;
    }

    public Optional<int[]> getEntityIds() {
        if (packet != null) {
            if (v_1_17) {
                return Optional.of(new int[] {getEntityId()});
            }
            else {
                return Optional.of(readIntArray(0));
            }
        } else {
            if (v_1_17_1) {
                IntList list = readObject(0, IntList.class);
                return Optional.of(list.toIntArray());
            }
            else if (v_1_17) {
                return Optional.of(new int[] {entityID});
            }
            else {
                return Optional.of(entityIds);
            }
        }
    }

    public void setEntityIds(int... entityIds) {
        if (packet != null) {
            if (v_1_17) {
                setEntityId(entityIds[0]);
            }
            else {
                writeIntArray(0, entityIds);
            }
        }
        else {
            if (v_1_17) {
                this.entityID = entityIds[0];
            }
            else {
                this.entityIds = entityIds;
            }
        }
    }


    @Override
    public Object asNMSPacket() throws Exception {
        if (v_1_17) {
            return packetConstructor.newInstance(getEntityId());
        } else {
            return packetConstructor.newInstance(getEntityIds().get());
        }
    }
}