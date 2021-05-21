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

package io.github.retrooper.packetevents.packetwrappers.play.out.removeentityeffect;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.entity.Entity;

import java.lang.reflect.Constructor;

public class WrappedPacketOutRemoveEntityEffect extends WrappedPacketEntityAbstraction implements SendableWrapper {
    private static Constructor<?> packetDefaultConstructor;
    private int effectID = -1;

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
        try {
            packetDefaultConstructor = PacketTypeClasses.Play.Server.REMOVE_ENTITY_EFFECT.getConstructor();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public int getEffectId() {
        if (effectID != -1 || packet == null) {
            return effectID;
        } else if (version.isOlderThan(ServerVersion.v_1_9)) {
            return effectID = readInt(1);
        } else {
            Object nmsMobEffectList = read(0, NMSUtils.mobEffectListClass);
            return NMSUtils.getEffectId(nmsMobEffectList);
        }
    }

    public void setEffectId(int effectID) {
        this.effectID = effectID;
        if (packet != null) {
            if (version.isOlderThan(ServerVersion.v_1_9)) {
                writeInt(1, effectID);
            } else {
                Object nmsMobEffectList = NMSUtils.getMobEffectListById(effectID);
                write(NMSUtils.mobEffectListClass, 0, nmsMobEffectList);
            }
        }
    }

    @Override
    public Object asNMSPacket() throws Exception {
        Object packetPlayOutRemoveEntityEffectInstance = packetDefaultConstructor.newInstance();
        WrappedPacketOutRemoveEntityEffect wrappedPacketOutRemoveEntityEffect =
                new WrappedPacketOutRemoveEntityEffect(new NMSPacket(packetPlayOutRemoveEntityEffectInstance));
        wrappedPacketOutRemoveEntityEffect.setEntityId(getEntityId());
        wrappedPacketOutRemoveEntityEffect.setEffectId(getEffectId());
        return packetPlayOutRemoveEntityEffectInstance;
    }
}
