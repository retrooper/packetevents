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

package io.github.retrooper.packetevents.packetwrappers.play.out.removeentityeffect;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.entity.Entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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
    public Object asNMSPacket() {
        try {
            Object packetPlayOutRemoveEntityEffectInstance = packetDefaultConstructor.newInstance();
            WrappedPacketOutRemoveEntityEffect wrappedPacketOutRemoveEntityEffect =
                    new WrappedPacketOutRemoveEntityEffect(new NMSPacket(packetPlayOutRemoveEntityEffectInstance));
            wrappedPacketOutRemoveEntityEffect.setEntityId(getEntityId());
            wrappedPacketOutRemoveEntityEffect.setEffectId(getEffectId());
            return packetPlayOutRemoveEntityEffectInstance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
