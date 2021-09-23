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
import io.github.retrooper.packetevents.protocol.data.world.chunk.Chunk;
import io.github.retrooper.packetevents.protocol.data.world.chunk.Column;
import io.github.retrooper.packetevents.protocol.data.nbt.NBTCompound;
import io.github.retrooper.packetevents.utils.NetStreamOutput;
import io.github.retrooper.packetevents.utils.netty.buffer.ByteBufAbstract;
import io.github.retrooper.packetevents.utils.netty.buffer.ByteBufUtil;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.io.ByteArrayOutputStream;
import java.util.BitSet;

//TODO Finish
public class WrapperPlayServerChunkData extends PacketWrapper<WrapperPlayServerChunkData> {
    private Column column;

    public WrapperPlayServerChunkData(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerChunkData(Column column) {
        super(PacketType.Play.Server.CHUNK_DATA.getID());
        this.column = column;
    }

    @Override
    public void readData() {
        //Currently only 1.17 support
        //Chunk X and Z
        int chunkX = readInt();
        int chunkZ = readInt();

        boolean fullChunk = true;
        if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_13_2) && serverVersion.isOlderThan(ServerVersion.v_1_17)) {
            fullChunk = readBoolean();
        }

        BitSet chunkMask;
        if (serverVersion.isOlderThan(ServerVersion.v_1_17)) {
            chunkMask = BitSet.valueOf(new long[] {readVarInt()});
        }
        else {
            int bitMaskLength = readVarInt();
            chunkMask = BitSet.valueOf(readLongArray(bitMaskLength));
        }

        NBTCompound heightMaps = readTag();
        int[] biomeData = new int[0];
        if (fullChunk) {
            biomeData = new int[readVarInt()];
            for (int index = 0; index < biomeData.length; index++) {
                biomeData[index] = readVarInt();
            }
        }
        byte[] data = readByteArray(readVarInt());
        ByteBufAbstract bb = ByteBufUtil.copiedBuffer(data);
        PacketWrapper<?> dataWrapper = PacketWrapper.createUniversalPacketWrapper(bb);
        Chunk[] chunks = new Chunk[chunkMask.size()];
        for (int index = 0; index < chunks.length; index++) {
            if (chunkMask.get(index)) {
                chunks[index] = Chunk.read(dataWrapper);
            }
        }

        NBTCompound[] tileEntities = new NBTCompound[readVarInt()];
        for (int i = 0; i < tileEntities.length; i++) {
            tileEntities[i] = readTag();
        }

        if (fullChunk) {
            column = new Column(chunkX, chunkZ, chunks, tileEntities, heightMaps, biomeData);
        }
        else {
            column = new Column(chunkX, chunkZ, chunks, tileEntities, heightMaps);
        }
    }

    @Override
    public void readData(WrapperPlayServerChunkData wrapper) {
        column = wrapper.column;
    }

    @Override
    public void writeData() {
        ByteArrayOutputStream dataBytes = new ByteArrayOutputStream();
        NetStreamOutput dataOut = new NetStreamOutput(dataBytes);

        BitSet bitSet = new BitSet();
        Chunk[] chunks = column.getChunks();
        for (int index = 0; index < chunks.length; index++) {
            Chunk chunk = chunks[index];
            if (chunk != null && !chunk.isEmpty()) {
                bitSet.set(index);
                Chunk.write(dataOut, chunk);
            }
        }

        writeInt(column.getX());
        writeInt(column.getZ());

        boolean fullChunk = column.isFullChunk();
        if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_13_2) && serverVersion.isOlderThan(ServerVersion.v_1_17)) {
            writeBoolean(fullChunk);
        }

        if (serverVersion.isOlderThan(ServerVersion.v_1_17)) {
            writeVarInt((int) bitSet.toLongArray()[0]);
        }
        else {
            long[] longArray = bitSet.toLongArray();
            writeVarInt(longArray.length);
            for (long content : longArray) {
                writeLong(content);
            }
        }
        writeTag(column.getHeightMaps());
        if (fullChunk) {
            writeVarInt(column.getBiomeData().length);
            for (int biomeData : column.getBiomeData()) {
                writeVarInt(biomeData);
            }
        }
        writeVarInt(dataBytes.size());
        writeByteArray(dataBytes.toByteArray());
        writeVarInt(column.getTileEntities().length);
        for(NBTCompound tag : column.getTileEntities()) {
            writeTag(tag);
        }
    }

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }
}
