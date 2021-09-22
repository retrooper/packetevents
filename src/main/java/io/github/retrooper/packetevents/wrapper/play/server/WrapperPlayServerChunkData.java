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
import net.minecraft.server.v1_7_R4.PacketPlayOutMapChunk;

import java.io.IOException;
import java.util.BitSet;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

//TODO Finish
public class WrapperPlayServerChunkData extends PacketWrapper<WrapperPlayServerChunkData> {
    private int chunkX;
    private int chunkZ;
    private BitSet primaryBitSet;
    private Optional<Boolean> groundUpContinuous;
    private Optional<Boolean> fullChunk;
    private Optional<Boolean> ignoreOldData;
    private Optional<NBTCompound> heightMaps;
    //TODO Abstract the biome ids
    private Optional<int[]> biomeIDs;
    private byte[] data;

    public WrapperPlayServerChunkData(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerChunkData() {
        super(PacketType.Play.Server.CHUNK_DATA.getID());
    }

    @Override
    public void readData() {
        //Chunk X and Z
        chunkX = readInt();
        chunkZ = readInt();

        //Booleans
        if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_13_2)) {
            fullChunk = Optional.of(readBoolean());
        } else {
            groundUpContinuous = Optional.of(readBoolean());
        }

        if (serverVersion == ServerVersion.v_1_16 ||
                serverVersion == ServerVersion.v_1_16_1) {
            ignoreOldData = Optional.of(readBoolean());
        }

        //Bit set
        long[] bitSetLongs = new long[0];
        if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_17)) {
            primaryBitSet = BitSet.valueOf(readLongArray());
        } else if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_9)) {
            primaryBitSet = BitSet.valueOf(new long[]{readVarInt()});
        } else {

            if (serverVersion == ServerVersion.v_1_7_10) {
                //Primary bit mask, add bit mask
                bitSetLongs = new long[] {readUnsignedShort(), readUnsignedShort()};
            }
            else {
                //Primary bit mask
                bitSetLongs = new long[] {readUnsignedShort()};
            }
            primaryBitSet = BitSet.valueOf(bitSetLongs);
        }

        //Height maps
        if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_14)) {
            heightMaps = Optional.of(readTag());
        }

        //Biome IDs
        if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_15)) {
            int biomesLength;
            if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_16_2)) {
                biomesLength = readVarInt();
            } else {
                biomesLength = 1024;//Always 1024
            }
            boolean v1_17 = serverVersion.isNewerThanOrEquals(ServerVersion.v_1_17);
            int[] biomeIDs = new int[biomesLength];
            for (int i = 0; i < biomesLength; i++) {
                if (v1_17) {
                    biomeIDs[i] = readVarInt();
                } else {
                    biomeIDs[i] = readInt();
                }
            }
            this.biomeIDs = Optional.of(biomeIDs);
        }
        else {
            this.biomeIDs = Optional.empty();
        }


        //Chunk data
        if (serverVersion == ServerVersion.v_1_7_10) {
            //1.7.10 compresses the chunk data
            int dataLength = readInt();
            byte[] compressedData = readByteArray(dataLength);
            int i = 0;

            int j;
            for(j = 0; j < 16; ++j) {
                i += bitSetLongs[0] >> j & 1;
            }

            j = 12288 * i;
            if (groundUpContinuous.orElse(false)) {
                j += 256;
            }

            data = new byte[j];
            Inflater inflater = new Inflater();
            inflater.setInput(compressedData, 0, compressedData.length);

            try {
                inflater.inflate(data);
            } catch (DataFormatException var9) {
                throw new IllegalStateException("Badly compressed chunk data!");
            } finally {
                inflater.end();
            }

        }
        else {
            int dataLength = readVarInt();
            data = readByteArray(dataLength);
        }

        //TODO Support 1.9 - 1.11 with cursed biomes order
        //TODO Support block of entities or whatever that is
    }

    @Override
    public void readData(WrapperPlayServerChunkData wrapper) {

    }

    @Override
    public void writeData() {

    }
}
