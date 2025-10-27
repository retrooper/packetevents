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

package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.netty.buffer.ByteBufInputStream;
import com.github.retrooper.packetevents.netty.buffer.ByteBufOutputStream;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.stream.NetStreamInput;
import com.github.retrooper.packetevents.protocol.stream.NetStreamInputWrapper;
import com.github.retrooper.packetevents.protocol.stream.NetStreamOutput;
import com.github.retrooper.packetevents.protocol.stream.NetStreamOutputWrapper;
import com.github.retrooper.packetevents.protocol.world.chunk.*;
import com.github.retrooper.packetevents.protocol.world.chunk.palette.DataPalette;
import com.github.retrooper.packetevents.protocol.world.chunk.palette.Palette;
import com.github.retrooper.packetevents.protocol.world.chunk.palette.PaletteType;
import com.github.retrooper.packetevents.protocol.world.chunk.palette.SingletonPalette;
import com.github.retrooper.packetevents.protocol.world.chunk.reader.impl.*;
import com.github.retrooper.packetevents.protocol.world.chunk.storage.BitStorage;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.*;

public class WrapperPlayServerChunkBiomes extends PacketWrapper<WrapperPlayServerChunkBiomes> {

    private Map<Long, ChunkData> chunkPalettesMap;

    public WrapperPlayServerChunkBiomes(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerChunkBiomes(Map<Long, ChunkData> chunkPalettesMap) {
        super(PacketType.Play.Server.CHUNK_BIOMES);
        this.chunkPalettesMap = chunkPalettesMap;
    }

    public WrapperPlayServerChunkBiomes(long[] chunkKeys, DataPalette biomePalette) {
        super(PacketType.Play.Server.CHUNK_BIOMES);
        this.chunkPalettesMap = new HashMap<>();
        for (long chunkKey : chunkKeys) {
            this.chunkPalettesMap.put(chunkKey, new ChunkData(biomePalette));
        }
    }

    public WrapperPlayServerChunkBiomes(long[] chunkKeys, int biomeID) {
        super(PacketType.Play.Server.CHUNK_BIOMES);
        this.chunkPalettesMap = new HashMap<>();
        for (long chunkKey : chunkKeys) {
            this.chunkPalettesMap.put(chunkKey, new ChunkData(biomeID));
        }
    }

    @Override
    public void read() {
        chunkPalettesMap = new HashMap<>();
        int chunkSize = 24;
        byte chunkAmount = readByte();
        for (int chunkIndex = 0; chunkIndex < chunkAmount; chunkIndex++) {
            int chunkZ = readInt();
            int chunkX = readInt();
            int bufferSize = readVarInt();
            List<DataPalette> chunkPalettes = new ArrayList<>();
            NetStreamInput stream = new NetStreamInput(new ByteBufInputStream(this.buffer));
            for (int i = 0; i < chunkSize; i++) {
                DataPalette biomePalette = DataPalette.read(
                        stream,
                        PaletteType.BIOME,
                        true,
                        false);
                chunkPalettes.add(biomePalette);
            }
            ChunkData chunkData = new ChunkData(chunkPalettes, bufferSize);
            chunkPalettesMap.put(getChunkKey(chunkX, chunkZ), chunkData);
        }
    }

    @Override
    public void write() {
        writeByte(chunkPalettesMap.size());
        for (Map.Entry<Long, ChunkData> entry : chunkPalettesMap.entrySet()) {
            long chunkKey = entry.getKey();
            int chunkX = getChunkX(chunkKey);
            int chunkZ = getChunkZ(chunkKey);
            writeInt(chunkZ);
            writeInt(chunkX);
            int calculatedBufferSize = 0;
            List<DataPalette> chunkPalettes = entry.getValue().palettes;
            for (DataPalette biomePalette : chunkPalettes) {
                Object tempBuffer = ByteBufHelper.allocateNewBuffer(buffer);

                int sizeBefore = ByteBufHelper.readableBytes(tempBuffer);
                DataPalette.write(new NetStreamOutput(new ByteBufOutputStream(tempBuffer)), biomePalette, false);
                int sizeAfter = ByteBufHelper.readableBytes(tempBuffer);

                calculatedBufferSize += (sizeAfter - sizeBefore);
            }
            writeVarInt(calculatedBufferSize);
            for (DataPalette chunkPalette : chunkPalettes) {
                DataPalette.write(new NetStreamOutputWrapper(this), chunkPalette, false);
            }
        }


    }

    @Override
    public void copy(WrapperPlayServerChunkBiomes wrapper) {
        this.chunkPalettesMap = wrapper.chunkPalettesMap;
    }

    public Map<Long, ChunkData> getChunkPalettesMap() {
        return chunkPalettesMap;
    }

    public List<Long> getChunkKeys() {
        return new ArrayList<>(chunkPalettesMap.keySet());
    }

    public ChunkData getPalette(int chunkX, int chunkZ) {
        return chunkPalettesMap.get(getChunkKey(chunkX, chunkZ));
    }

    public static class ChunkData {
        List<DataPalette> palettes;
        final int bufferSize; // size in bytes of the serialized palettes (Only != 0 if read from packet)

        private ChunkData(List<DataPalette> palettes, int bufferSize) {
            this.bufferSize = bufferSize;
            this.palettes = palettes;
        }

        public ChunkData(List<DataPalette> palettes) {
            this.palettes = palettes;
            this.bufferSize = 0;
        }

        public ChunkData(DataPalette palette) {
            this.palettes = new ArrayList<>();
            for (int i = 0; i < 24; i++) {
                this.palettes.add(palette);
            }
            this.bufferSize = 0;
        }

        public ChunkData(int biomeID) {
            this.palettes = new ArrayList<>();
            DataPalette biomePalette = new DataPalette(
                    new SingletonPalette(biomeID),
                    new BitStorage(1, 64),
                    PaletteType.BIOME
            );
            for (int i = 0; i < 24; i++) {
                palettes.add(biomePalette);
            }
            this.bufferSize = 0;
        }

    }

}
