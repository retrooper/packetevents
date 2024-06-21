/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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

package com.github.retrooper.packetevents.util;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.player.Equipment;
import com.github.retrooper.packetevents.protocol.world.chunk.LightData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChunkData;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityEquipment;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateLight;

import java.util.Collections;

public class PacketTransformationUtil {
    public static PacketWrapper<?>[] transform(PacketWrapper<?> wrapper) {
        if (wrapper instanceof WrapperPlayServerDestroyEntities) {
            WrapperPlayServerDestroyEntities destroyEntities = (WrapperPlayServerDestroyEntities) wrapper;
            int len = destroyEntities.getEntityIds().length;
            if (wrapper.getServerVersion() == ServerVersion.V_1_17 && len > 1) {
                //Transform into multiple packets
                PacketWrapper<?>[] output = new PacketWrapper[len];
                for (int i = 0; i < len; i++) {
                    int entityId = destroyEntities.getEntityIds()[i];
                    output[i] = new WrapperPlayServerDestroyEntities(entityId);
                }
                return output;
            }
        } else if (wrapper instanceof WrapperPlayServerEntityEquipment) {
            WrapperPlayServerEntityEquipment entityEquipment = (WrapperPlayServerEntityEquipment) wrapper;
            int len = entityEquipment.getEquipment().size();
            if (entityEquipment.getServerVersion().isOlderThan(ServerVersion.V_1_16) && len > 1) {
                //Transform into multiple packets. Actually a mistake on the user's end.
                PacketWrapper<?>[] output = new PacketWrapper[len];
                for (int i = 0; i < len; i++) {
                    Equipment equipment = entityEquipment.getEquipment().get(i);
                    output[i] = new WrapperPlayServerEntityEquipment(entityEquipment.getEntityId(), Collections.singletonList(equipment));
                }
                return output;
            }
        } else if (wrapper instanceof WrapperPlayServerChunkData) {
            WrapperPlayServerChunkData chunkData = (WrapperPlayServerChunkData) wrapper;
            LightData lightData = chunkData.getLightData();

            if (chunkData.getServerVersion().isOlderThan(ServerVersion.V_1_18) && lightData != null) {
                //Transform into chunk data & light data packets
                PacketWrapper<?>[] output = new PacketWrapper[2];
                output[0] = new WrapperPlayServerUpdateLight(
                        chunkData.getColumn().getX(),
                        chunkData.getColumn().getZ(),
                        lightData
                );
                // Light packet must be sent first
                output[1] = chunkData;

                return output;
            }
        }
        return new PacketWrapper<?>[]{wrapper};
    }
}
