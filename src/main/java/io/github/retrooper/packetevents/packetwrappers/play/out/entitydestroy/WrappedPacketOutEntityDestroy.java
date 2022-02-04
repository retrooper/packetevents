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
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.entity.Entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author yanjulang, MWHunter
 * @since 1.8
 */
public class WrappedPacketOutEntityDestroy extends WrappedPacketEntityAbstraction implements SendableWrapper {
    private static Class<?> INT_COLLECTION_CLASS;
    private static Class<?> INT_LIST_CLASS;
    private static Class<?> INT_ARRAY_LIST_CLASS;
    private static Constructor<?> INT_ARRAY_LIST_CONSTRUCTOR;
    private static Method TO_INT_ARRAY_METHOD;
    private static boolean v_1_17;
    private static boolean v_1_17_1;
    private static Constructor<?> packetConstructor;
    private int[] entityIds = new int[0];

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
        v_1_17_1 = version.isNewerThanOrEquals(ServerVersion.v_1_17_1);
        v_1_17 = version.equals(ServerVersion.v_1_17);

        if (v_1_17_1) {
            if (INT_COLLECTION_CLASS == null) {
                INT_COLLECTION_CLASS = Reflection.getClassByNameWithoutException("it.unimi.dsi.fastutil.ints.IntCollection");
                INT_LIST_CLASS = Reflection.getClassByNameWithoutException("it.unimi.dsi.fastutil.ints.IntList");
                INT_ARRAY_LIST_CLASS = Reflection.getClassByNameWithoutException("it.unimi.dsi.fastutil.ints.IntArrayList");
                try {
                    INT_ARRAY_LIST_CONSTRUCTOR = INT_ARRAY_LIST_CLASS.getConstructor(int[].class);
                    INT_ARRAY_LIST_CONSTRUCTOR.setAccessible(true);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                try {
                    TO_INT_ARRAY_METHOD = INT_COLLECTION_CLASS.getDeclaredMethod("toIntArray");
                    TO_INT_ARRAY_METHOD.setAccessible(true);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            if (v_1_17) {
                packetConstructor =
                        PacketTypeClasses.Play.Server.ENTITY_DESTROY.getConstructor(int.class);
            } else {
                packetConstructor =
                        PacketTypeClasses.Play.Server.ENTITY_DESTROY.getConstructor(int[].class);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getEntityId() {
        if (entityID != -1 || entityIds.length > 0 || packet == null) {
            if (v_1_17) {
                return entityID;
            } else {
                return entityIds[0];
            }
        } else {
            if (v_1_17_1) {
                Object list = readObject(0, INT_LIST_CLASS);
                try {
                    entityIds = (int[]) TO_INT_ARRAY_METHOD.invoke(list);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                return entityIds[0];
            } else if (v_1_17) {
                return entityID = readInt(0);
            } else {
                entityIds = readIntArray(0);
                return entityIds[0];
            }
        }

    }

    @Override
    public void setEntityId(int entityID) {
        if (packet != null) {
            if (v_1_17) {
                writeInt(0, this.entityID = entityID);
            } else {
                this.entityIds = new int[]{entityID};
                if (v_1_17_1) {
                    Object intArrayList = null;
                    try {
                        intArrayList = INT_ARRAY_LIST_CONSTRUCTOR.newInstance(this.entityIds);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    write(INT_LIST_CLASS, 0, intArrayList);
                } else {
                    writeIntArray(0, new int[]{this.entityIds[0]});
                }
            }
        } else {
            if (v_1_17) {
                this.entityID = entityID;
            } else {
                this.entityIds = new int[]{entityID};
            }
        }
        this.entity = null;
    }

    public int[] getEntityIds() {
        if (packet != null) {
            if (v_1_17) {
                return new int[]{getEntityId()};
            } else if (v_1_17_1) {
                Object list = readObject(0, INT_LIST_CLASS);
                try {
                    return (int[]) TO_INT_ARRAY_METHOD.invoke(list);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                return null;
            } else {
                return readIntArray(0);
            }
        } else {
            if (v_1_17) {
                return new int[]{entityID};
            } else {
                return entityIds;
            }
        }
    }

    public void setEntityIds(int... entityIds) {
        if (packet != null) {
            if (v_1_17) {
                setEntityId(entityIds[0]);
            } else if (v_1_17_1) {
                Object intArrayList = null;
                try {
                    intArrayList = INT_ARRAY_LIST_CONSTRUCTOR.newInstance(entityIds);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                write(INT_LIST_CLASS, 0, intArrayList);
            } else {
                writeIntArray(0, entityIds);
            }
        } else {
            if (v_1_17) {
                this.entityID = entityIds[0];
            } else {
                this.entityIds = entityIds;
            }
        }
    }


    @Override
    public Object asNMSPacket() throws Exception {
        if (v_1_17) {
            return packetConstructor.newInstance(getEntityId());
        } else {
            return packetConstructor.newInstance(getEntityIds());
        }
    }
}