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

package io.github.retrooper.packetevents.packetwrappers.play.out.mount;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.entity.Entity;

import java.lang.reflect.Constructor;
//TODO Test sending this wrapper on 1.17
public class WrappedPacketOutMount extends WrappedPacketEntityAbstraction implements SendableWrapper {
    private static boolean v_1_17;
    private static Constructor<?> packetConstructor;
    private int[] passengerIDs;

    public WrappedPacketOutMount(NMSPacket packet) {
        super(packet);
        net.minecraft.server.v1_16_R2.PacketPlayOutMount m;
        net.minecraft.network.protocol.game.PacketPlayOutMount m2;
    }

    public WrappedPacketOutMount(int entityID, int[] passengerIDs) {
        setEntityId(entityID);
        this.passengerIDs = passengerIDs;
    }

    public WrappedPacketOutMount(Entity entity, int[] passengerIDs) {
        setEntity(entity);
        this.passengerIDs = passengerIDs;
    }

    @Override
    protected void load() {
        v_1_17 = version.isNewerThanOrEquals(ServerVersion.v_1_17);
        try {
            if (v_1_17) {
                packetConstructor = PacketTypeClasses.Play.Server.MOUNT.getConstructor(NMSUtils.packetDataSerializerClass);
            }
            else {
                packetConstructor = PacketTypeClasses.Play.Server.MOUNT.getConstructor();
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public int[] getPassengerIds() {
        if (packet != null) {
            return readIntArray(0);
        } else {
            return passengerIDs;
        }
    }

    public void setPassengerIds(int[] passengerIDs) {
        if (packet != null) {
            writeIntArray(0, passengerIDs);
        } else {
            this.passengerIDs = passengerIDs;
        }
    }

    @Override
    public boolean isSupported() {
        return version.isNewerThan(ServerVersion.v_1_8_8);
    }

    @Override
    public Object asNMSPacket() throws Exception {
        Object packetInstance;
        if (v_1_17) {
            Object byteBuf = PacketEvents.get().getByteBufUtil().newByteBuf(new byte[] {0, 0, 0, 0, 0, 0, 0, 0});
            Object packetDataSerializer = NMSUtils.generatePacketDataSerializer(byteBuf);
         packetInstance = packetConstructor.newInstance(packetDataSerializer);
        }
        else {
            packetInstance = packetConstructor.newInstance();
        }
        WrappedPacketOutMount wrappedPacketOutMount = new WrappedPacketOutMount(new NMSPacket(packetInstance));
        wrappedPacketOutMount.setEntityId(getEntityId());
        wrappedPacketOutMount.setPassengerIds(getPassengerIds());
        return packetInstance;
    }
}
