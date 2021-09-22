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

package io.github.retrooper.packetevents.wrapper.play.server;

import io.github.retrooper.packetevents.event.impl.PacketSendEvent;
import io.github.retrooper.packetevents.manager.server.ServerVersion;
import io.github.retrooper.packetevents.protocol.PacketType;
import io.github.retrooper.packetevents.utils.nbt.NBTCompound;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.BitSet;
import java.util.Optional;
//TODO Finish
public class WrapperPlayServerChunkData extends PacketWrapper<WrapperPlayServerChunkData> {
    private int chunkX;
    private int chunkZ;
    private BitSet primaryBitSet;
    private Optional<Boolean> groundUpContinuous;
    private Optional<Boolean> fullChunk;
    private Optional<Boolean> ignoreOldData;
    private Optional<NBTCompound> heightMaps;
    public WrapperPlayServerChunkData(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerChunkData() {
        super(PacketType.Play.Server.CHUNK_DATA.getID());
    }

    @Override
    public void readData() {
        chunkX = readInt();
        chunkZ = readInt();
        if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_13_2)) {
            fullChunk = Optional.of(readBoolean());
        }
        else {
            groundUpContinuous = Optional.of(readBoolean());
        }

        if (serverVersion == ServerVersion.v_1_16 ||
        serverVersion == ServerVersion.v_1_16_1) {
            ignoreOldData = Optional.of(readBoolean());
        }

        if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_17)) {
            primaryBitSet = BitSet.valueOf(readLongArray());
        }
        else if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_9)) {
            primaryBitSet = BitSet.valueOf(new long[] {readVarInt()});
        }
        else {
            primaryBitSet = BitSet.valueOf(new long[] {readUnsignedShort()});
        }

        if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_14)) {
            heightMaps = Optional.of(readTag());
        }

        //Biom thingy here on 1.17 and 1.16.x ig? (todo look further)

        //newer versions have some extra data

        //then we can finally go to size and the real byte array with data

        net.minecraft.server.v1_7_R4.PacketPlayOutMapChunk mc0;
        net.minecraft.server.v1_8_R3.PacketPlayOutMapChunk mc1;
        net.minecraft.server.v1_9_R1.PacketPlayOutMapChunk mc2;
        net.minecraft.server.v1_10_R1.PacketPlayOutMapChunk mc3;
        net.minecraft.server.v1_11_R1.PacketPlayOutMapChunk mc4;
        net.minecraft.server.v1_12_R1.PacketPlayOutMapChunk mc5;
        net.minecraft.server.v1_13_R1.PacketPlayOutMapChunk mc6;
        net.minecraft.server.v1_14_R1.PacketPlayOutMapChunk mc7;
        net.minecraft.server.v1_15_R1.PacketPlayOutMapChunk mc8;
        net.minecraft.server.v1_16_R1.PacketPlayOutMapChunk mc9;
        net.minecraft.network.protocol.game.PacketPlayOutMapChunk mc10;
    }

    @Override
    public void readData(WrapperPlayServerChunkData wrapper) {

    }

    @Override
    public void writeData() {

    }
}
