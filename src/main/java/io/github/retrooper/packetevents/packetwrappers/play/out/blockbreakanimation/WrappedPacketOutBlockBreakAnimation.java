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

package io.github.retrooper.packetevents.packetwrappers.play.out.blockbreakanimation;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import io.github.retrooper.packetevents.utils.vector.Vector3i;
import org.bukkit.entity.Entity;

import java.lang.reflect.Constructor;

public class WrappedPacketOutBlockBreakAnimation extends WrappedPacketEntityAbstraction implements SendableWrapper {
    private static Constructor<?> packetConstructor;
    private Vector3i blockPosition;
    private int destroyStage;

    public WrappedPacketOutBlockBreakAnimation(NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutBlockBreakAnimation(int entityID, Vector3i blockPosition, int destroyStage) {
        this.entityID = entityID;
        this.blockPosition = blockPosition;
        this.destroyStage = destroyStage;
    }

    public WrappedPacketOutBlockBreakAnimation(Entity entity, Vector3i blockPosition, int destroyStage) {
        this.entityID = entity.getEntityId();
        this.entity = entity;
        this.blockPosition = blockPosition;
        this.destroyStage = destroyStage;
    }

    @Override
    protected void load() {
        try {
            packetConstructor = PacketTypeClasses.Play.Server.BLOCK_BREAK_ANIMATION.getConstructor(int.class, int.class, int.class, int.class, int.class);
        } catch (NoSuchMethodException e) {
            try {
                packetConstructor = PacketTypeClasses.Play.Server.BLOCK_BREAK_ANIMATION.getConstructor(int.class, NMSUtils.blockPosClass, int.class);
            } catch (NoSuchMethodException e2) {
                e2.printStackTrace();
            }
        }
    }

    public Vector3i getBlockPosition() {
        if (packet != null) {
            Vector3i blockPosition = new Vector3i();
            if (version.isOlderThan(ServerVersion.v_1_8)) {
                this.blockPosition.x = readInt(1);
                this.blockPosition.y = readInt(2);
                this.blockPosition.z = readInt(3);
            } else {
                Object blockPos = readObject(0, NMSUtils.blockPosClass);
                this.blockPosition = NMSUtils.readBlockPos(blockPos);
            }
            return blockPosition;
        } else {
            return this.blockPosition;
        }
    }

    public void setBlockPosition(Vector3i blockPosition) {
        if (packet != null) {
            if (version.isOlderThan(ServerVersion.v_1_8)) {
                writeInt(1, blockPosition.x);
                writeInt(2, blockPosition.y);
                writeInt(3, blockPosition.z);
            } else {
                Object blockPos = NMSUtils.generateNMSBlockPos(blockPosition.x, blockPosition.y, blockPosition.z);
                write(NMSUtils.blockPosClass, 0, blockPos);
            }
        } else {
            this.blockPosition = blockPosition;
        }
    }

    public int getDestroyStage() {
        if (packet != null) {
            int index = version.isOlderThan(ServerVersion.v_1_8) ? 4 : 1;
            return readInt(index);
        } else {
            return this.destroyStage;
        }
    }

    public void setDestroyStage(int destroyStage) {
        if (packet != null) {
            int index = version.isOlderThan(ServerVersion.v_1_8) ? 4 : 1;
            writeInt(index, destroyStage);
        } else {
            this.destroyStage = destroyStage;
        }
    }

    @Override
    public Object asNMSPacket() throws Exception {
        Vector3i blockPosition = getBlockPosition();
        if (version.isOlderThan(ServerVersion.v_1_8)) {
            return packetConstructor.newInstance(getEntityId(), blockPosition.x, blockPosition.y, blockPosition.z, getDestroyStage());
        } else {
            Object nmsBlockPos = NMSUtils.generateNMSBlockPos(blockPosition.x, blockPosition.y, blockPosition.z);
            return packetConstructor.newInstance(getEntityId(), nmsBlockPos, getDestroyStage());
        }
    }
}
