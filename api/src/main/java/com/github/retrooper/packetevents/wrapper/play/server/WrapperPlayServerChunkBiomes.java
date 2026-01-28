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
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.world.chunk.*;
import com.github.retrooper.packetevents.protocol.world.chunk.palette.DataPalette;
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

    public WrapperPlayServerChunkBiomes(long[] chunkKeys, DataPalette biomePalette, int worldHeight) {
        super(PacketType.Play.Server.CHUNK_BIOMES);
        this.chunkPalettesMap = new HashMap<>();
        for (long chunkKey : chunkKeys) {
            this.chunkPalettesMap.put(chunkKey, new ChunkData(biomePalette, worldHeight / 16));
        }
    }

    public WrapperPlayServerChunkBiomes(long[] chunkKeys, DataPalette biomePalette) {
        this(chunkKeys, biomePalette, 384);
    }

    public WrapperPlayServerChunkBiomes(long[] chunkKeys, int biomeID, int worldHeight) {
        super(PacketType.Play.Server.CHUNK_BIOMES);
        this.chunkPalettesMap = new HashMap<>();
        for (long chunkKey : chunkKeys) {
            this.chunkPalettesMap.put(chunkKey, new ChunkData(biomeID, worldHeight / 16));
        }
    }

    public WrapperPlayServerChunkBiomes(long[] chunkKeys, int biomeID) {
        this(chunkKeys, biomeID, 384);
    }

    @Override
    public void read() {
        chunkPalettesMap = new HashMap<>();
        int chunkAmount = readVarInt();
        for (int chunkIndex = 0; chunkIndex < chunkAmount; chunkIndex++) {
            int chunkZ = readInt();
            int chunkX = readInt();
            int bufferSize = readVarInt();
            List<DataPalette> chunkPalettes = new ArrayList<>();
            while (ByteBufHelper.readableBytes(buffer) > 0 && ByteBufHelper.readerIndex(buffer) < ByteBufHelper.writerIndex(buffer)) {
                DataPalette biomePalette = PaletteType.BIOME.read(this);
                chunkPalettes.add(biomePalette);
            }
            ChunkData chunkData = new ChunkData(chunkPalettes, bufferSize);
            chunkPalettesMap.put(getChunkKey(chunkX, chunkZ), chunkData);
        }
    }

    @Override
    public void write() {
        writeVarInt(chunkPalettesMap.size());
        for (Map.Entry<Long, ChunkData> entry : chunkPalettesMap.entrySet()) {
            long chunkKey = entry.getKey();
            int chunkX = getChunkX(chunkKey);
            int chunkZ = getChunkZ(chunkKey);
            writeInt(chunkZ);
            writeInt(chunkX);
            List<DataPalette> chunkPalettes = entry.getValue().palettes;

            int calculatedBufferSize = 0;
            int bufferIndex = ByteBufHelper.writerIndex(buffer);
            int sizeBefore = ByteBufHelper.readableBytes(buffer);

            for (DataPalette biomePalette : chunkPalettes) {
                PaletteType.write(this, biomePalette);
                int sizeAfter = ByteBufHelper.readableBytes(buffer);
                calculatedBufferSize += (sizeAfter - sizeBefore);
                ByteBufHelper.writerIndex(buffer, bufferIndex);
            }

            writeVarInt(calculatedBufferSize);

            for (DataPalette biomePalette : chunkPalettes) {
                PaletteType.write(this, biomePalette);
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
        /// Size in bytes of the serialized palettes (Only != 0 if read from packet)
        final int bufferSize;

        private ChunkData(List<DataPalette> palettes, int bufferSize) {
            this.bufferSize = bufferSize;
            this.palettes = palettes;
        }

        public ChunkData(List<DataPalette> palettes) {
            this.palettes = palettes;
            this.bufferSize = 0;
        }

        public ChunkData(DataPalette palette, int sectionAmount) {
            this.palettes = new ArrayList<>();
            for (int i = 0; i < sectionAmount; i++) {
                this.palettes.add(palette);
            }
            this.bufferSize = 0;
        }

        public ChunkData(int biomeID, int sectionAmount) {
            this.palettes = new ArrayList<>();
            DataPalette biomePalette = new DataPalette(
                    new SingletonPalette(biomeID),
                    new BitStorage(1, 64),
                    PaletteType.BIOME
            );
            for (int i = 0; i < sectionAmount; i++) {
                palettes.add(biomePalette);
            }
            this.bufferSize = 0;
        }

    }

}
