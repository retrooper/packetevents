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
import io.github.retrooper.packetevents.utils.chunk.Chunk;
import io.github.retrooper.packetevents.utils.chunk.Column;
import io.github.retrooper.packetevents.utils.nbt.NBTCompound;
import io.github.retrooper.packetevents.utils.netty.buffer.ByteBufAbstract;
import io.github.retrooper.packetevents.utils.netty.buffer.ByteBufAbstractInputStream;
import io.github.retrooper.packetevents.utils.netty.buffer.ByteBufUtil;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.io.IOException;
import java.util.BitSet;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

//TODO Finish
public class WrapperPlayServerChunkData extends PacketWrapper<WrapperPlayServerChunkData> {

    private Optional<Boolean> groundUpContinuous;
    private Optional<Boolean> fullChunk;
    private Optional<Boolean> ignoreOldData;
    private Optional<NBTCompound> heightMaps;
    //TODO Abstract the biome ids
    private Optional<int[]> biomeIDs;
    private int chunkX;
    private int chunkZ;
    private BitSet primaryBitSet;
    private byte[] data;

    private Column column;

    public WrapperPlayServerChunkData(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerChunkData() {
        super(PacketType.Play.Server.CHUNK_DATA.getID());
    }

    private void oldReadData() {
        //TODO Lots of cleaning up and parse the chunks correctly and make abstractions
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
        long[] bitSetLongs;
        if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_17)) {
            bitSetLongs = readLongArray();
            primaryBitSet = BitSet.valueOf(bitSetLongs);
        } else if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_9)) {
            bitSetLongs = new long[]{readVarInt()};
            primaryBitSet = BitSet.valueOf(bitSetLongs);
        } else {

            if (serverVersion == ServerVersion.v_1_7_10) {
                //Primary bit mask, add bit mask
                bitSetLongs = new long[]{readUnsignedShort(), readUnsignedShort()};
            } else {
                //Primary bit mask
                bitSetLongs = new long[]{readUnsignedShort()};
            }
            primaryBitSet = BitSet.valueOf(bitSetLongs);
        }

        //Height maps
        if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_14)) {
            heightMaps = Optional.of(readTag());
        }

        //Biome IDs on newer versions
        boolean handledBiomes = false;
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
            handledBiomes = true;
        }


        //Chunk data
        if (serverVersion == ServerVersion.v_1_7_10) {
            //1.7.10 compresses the chunk data
            int dataLength = readInt();
            byte[] compressedData = readByteArray(dataLength);
            int i = 0;

            int j;
            for (j = 0; j < 16; ++j) {
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

        } else {
            int dataLength = readVarInt();
            data = readByteArray((int) bitSetLongs[0]);
            if (!handledBiomes && groundUpContinuous.orElse(false)) {
                byte[] bytes = readByteArray(byteBuf.readableBytes());
                int[] output = new int[bytes.length];
                for (int i = 0; i < bytes.length; i++) {
                    output[i] = bytes[i];
                }
                biomeIDs = Optional.of(output);
                handledBiomes = true;
            }
        }

        if (!handledBiomes) {
            biomeIDs = Optional.empty();
        }
    }

    @Override
    public void readData() {
        //Currently only 1.17 support
        //Chunk X and Z
        chunkX = readInt();
        chunkZ = readInt();

        int bitMaskLength = readVarInt();
        BitSet chunkMask = BitSet.valueOf(readLongArray(bitMaskLength));
        NBTCompound heightMaps = readTag();
        int[] biomeData = new int[readVarInt()];
        for (int index = 0; index < biomeData.length; index++) {
            biomeData[index] = readVarInt();
        }
        byte[] data = readByteArray(readVarInt());
        ByteBufAbstract bb = ByteBufUtil.copiedBuffer(data);
        PacketWrapper<?> dataWrapper = PacketWrapper.createUniversalPacketWrapper(bb);
        Chunk[] chunks = new Chunk[chunkMask.size()];
        for(int index = 0; index < chunks.length; index++) {
            if(chunkMask.get(index)) {
                try {
                    chunks[index] = Chunk.read(dataWrapper);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        NBTCompound[] tileEntities = new NBTCompound[readVarInt()];
        for(int i = 0; i < tileEntities.length; i++) {
            tileEntities[i] = readTag();
        }

        this.column = new Column(chunkX, chunkZ, chunks, tileEntities, heightMaps, biomeData);
    }

    @Override
    public void readData(WrapperPlayServerChunkData wrapper) {

    }

    @Override
    public void writeData() {

    }
}
