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

package io.github.retrooper.packetevents.packetwrappers.play.out.removeentityeffect;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.entity.Entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class WrappedPacketOutRemoveEntityEffect extends WrappedPacketEntityAbstraction implements SendableWrapper {
    private static boolean v_1_8_x, v_1_17;
    private static Constructor<?> packetConstructor;
    private static Class<?> holderClass;
    private static Method accessHolderValueMethod;
    private static boolean hasHolder = false;
    private int effectID;

    public WrappedPacketOutRemoveEntityEffect(NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutRemoveEntityEffect(Entity entity, int effectID) {
        this.entityID = entity.getEntityId();
        this.entity = entity;
        this.effectID = effectID;
    }

    public WrappedPacketOutRemoveEntityEffect(int entityID, int effectID) {
        this.entityID = entityID;
        this.effectID = effectID;
    }

    @Override
    protected void load() {
        v_1_8_x = version.isOlderThan(ServerVersion.v_1_9);
        v_1_17 = version.isNewerThanOrEquals(ServerVersion.v_1_17);
        try {
            if (v_1_17) {
                //packetConstructor = PacketTypeClasses.Play.Server.REMOVE_ENTITY_EFFECT.getConstructor(int.class, NMSUtils.mobEffectListClass);
            }
            else {
                packetConstructor = PacketTypeClasses.Play.Server.REMOVE_ENTITY_EFFECT.getConstructor();
            }


            holderClass = NMSUtils.getNMClassWithoutException("core.Holder");
            if (holderClass != null) {
                hasHolder = Reflection.getField(PacketTypeClasses.Play.Server.ENTITY_EFFECT, holderClass, 0) != null;
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public int getEffectId() {
        if (packet != null) {
            //1.7 and 1.8
            if (v_1_8_x) {
                return readInt(1);
            }
            //1.9+
            else {
                Object mobEffectList = null;
                if (hasHolder) {
                    Object holder = readObject(0, holderClass);
                    if (accessHolderValueMethod == null) {
                        accessHolderValueMethod = Reflection.getMethod(holderClass, 0);
                    }
                    try {
                        mobEffectList = accessHolderValueMethod.invoke(holder);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    mobEffectList = readObject(0, NMSUtils.mobEffectListClass);
                }
                return NMSUtils.getEffectId(mobEffectList);
            }
        }
        else {
            return effectID;
        }
    }

    public void setEffectId(int effectID) {
        if (packet != null) {
            //1.7 and 1.8
            if (v_1_8_x) {
                writeInt(1, effectID);
            }
            //1.9+
            else {
                Object nmsMobEffectList = NMSUtils.getMobEffectListById(effectID);
                write(NMSUtils.mobEffectListClass, 0, nmsMobEffectList);
            }
        }
        else {
            this.effectID = effectID;
        }
    }

    @Override
    public Object asNMSPacket() throws Exception {
        Object packetInstance;
        if (v_1_17) {
            Object nmsMobEffectList = NMSUtils.getMobEffectListById(getEffectId());
            packetInstance = packetConstructor.newInstance(getEntityId(), nmsMobEffectList);
        }
        else {
            packetInstance = packetConstructor.newInstance();
            WrappedPacketOutRemoveEntityEffect wrappedPacketOutRemoveEntityEffect =
                    new WrappedPacketOutRemoveEntityEffect(new NMSPacket(packetInstance));
            wrappedPacketOutRemoveEntityEffect.setEntityId(getEntityId());
            wrappedPacketOutRemoveEntityEffect.setEffectId(getEffectId());
        }
        return packetInstance;
    }
}
