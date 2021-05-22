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

package io.github.retrooper.packetevents.packetwrappers.play.out.entitystatus;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import org.bukkit.entity.Entity;

import java.lang.reflect.Constructor;

public class WrappedPacketOutEntityStatus extends WrappedPacketEntityAbstraction implements SendableWrapper {
    private static Constructor<?> packetConstructor;
    private byte status;

    public WrappedPacketOutEntityStatus(NMSPacket packet) {
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

    @Override
    protected void load() {
        try {
            packetConstructor =
                    PacketTypeClasses.Play.Server.ENTITY_STATUS.getConstructor(NMSUtils.nmsEntityClass, byte.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public byte getEntityStatus() {
        if (packet != null) {
            return readByte(0);
        } else {
            return status;
        }
    }

    public void setEntityStatus(byte status) {
        if (packet != null) {
            writeByte(0, status);
        } else {
            this.status = status;
        }
    }

    @Override
    public Object asNMSPacket() throws Exception {
        Object nmsEntity = NMSUtils.getNMSEntity(getEntity());
        return packetConstructor.newInstance(nmsEntity, getEntityStatus());
    }
}
