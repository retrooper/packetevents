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

package io.github.retrooper.packetevents.packetwrappers.play.out.spawnentityliving;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import io.github.retrooper.packetevents.utils.vector.Vector3d;
import org.bukkit.entity.EntityType;

import java.util.Optional;
import java.util.UUID;

//TODO finish wrapper
class WrappedPacketOutSpawnEntityLiving extends WrappedPacketEntityAbstraction {
    public WrappedPacketOutSpawnEntityLiving(NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntityLiving a0;
        net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving a1;
        net.minecraft.server.v1_9_R1.PacketPlayOutSpawnEntityLiving a2;
        net.minecraft.server.v1_9_R2.PacketPlayOutSpawnEntityLiving a3;
        net.minecraft.server.v1_12_R1.PacketPlayOutSpawnEntityLiving a4;
        net.minecraft.server.v1_13_R1.PacketPlayOutSpawnEntityLiving a5;
        net.minecraft.server.v1_13_R2.PacketPlayOutSpawnEntityLiving a6;
        net.minecraft.server.v1_16_R2.PacketPlayOutSpawnEntityLiving a7;
    }

    private int floor(double a) {
        int result = (int) a;
        return a < (double) result ? result - 1 : result;
    }

    public Vector3d getPosition() {
        double x;
        double y;
        double z;
        //On 1.8.x and 1.7.10 the y is factored by 32
        if (version.isOlderThan(ServerVersion.v_1_9)) {
            x = readInt(2) / 32.0;
            y = readInt(3) / 32.0;
            z = readInt(4) / 32.0;
        } else {
            x = readDouble(0);
            y = readDouble(1);
            z = readDouble(2);
        }
        return new Vector3d(x, y, z);
    }

    public void setPosition(Vector3d position) {
        //On 1.8.x and 1.7.10 the y must be factored by 32
        if (version.isOlderThan(ServerVersion.v_1_9)) {
            writeInt(2, floor(position.x * 32.0));
            writeInt(3, floor(position.y * 32.0));
            writeInt(4, floor(position.z * 32.0));
        } else {
            writeDouble(0, position.x);
            writeDouble(1, position.y);
            writeDouble(2, position.z);
        }
    }

    public EntityType getType() {
        int entityTypeID = readInt(1);
        return EntityType.fromId(entityTypeID);
    }

    public void setEntityType(EntityType type) {
        writeInt(1, type.getTypeId());
    }

    public Optional<UUID> getUUID() {
        if (version.isNewerThan(ServerVersion.v_1_8_8)) {
            return Optional.of(readObject(0, UUID.class));
        }
        else {
            return Optional.empty();
        }
    }

    public void setUUID(UUID uuid) {
        if (version.isNewerThan(ServerVersion.v_1_8_8)) {
            writeObject(0, uuid);
        }
    }
}
