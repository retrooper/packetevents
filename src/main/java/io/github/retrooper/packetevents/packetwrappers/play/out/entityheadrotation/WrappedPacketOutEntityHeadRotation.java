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

package io.github.retrooper.packetevents.packetwrappers.play.out.entityheadrotation;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;
import org.bukkit.entity.Entity;

import java.lang.reflect.Constructor;

public class WrappedPacketOutEntityHeadRotation extends WrappedPacketEntityAbstraction implements SendableWrapper {
    private static Constructor<?> packetDefaultConstructor;
    private byte yaw;

    public WrappedPacketOutEntityHeadRotation(NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutEntityHeadRotation(int entityID, byte yaw) {
        this.entityID = entityID;
        this.yaw = yaw;
    }

    public WrappedPacketOutEntityHeadRotation(Entity entity, byte yaw) {
        this.entityID = entity.getEntityId();
        this.entity = entity;
        this.yaw = yaw;
    }

    @Override
    protected void load() {
        try {
            packetDefaultConstructor = PacketTypeClasses.Play.Server.ENTITY_HEAD_ROTATION.getConstructor();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public byte getYaw() {
        if (packet != null) {
            return readByte(0);
        } else {
            return yaw;
        }
    }

    public void setYaw(byte yaw) {
        if (packet != null) {
            writeByte(0, yaw);
        } else {
            this.yaw = yaw;
        }
    }

    @Override
    public Object asNMSPacket() throws Exception {
        Object packetInstance = packetDefaultConstructor.newInstance();
        WrappedPacketOutEntityHeadRotation wrappedPacketOutEntityHeadRotation = new WrappedPacketOutEntityHeadRotation(new NMSPacket(packetInstance));
        wrappedPacketOutEntityHeadRotation.setYaw(getYaw());
        wrappedPacketOutEntityHeadRotation.setEntityId(getEntityId());
        return packetInstance;
    }
}
