/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2026 retrooper and contributors
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
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.world.biome.Biome;
import com.github.retrooper.packetevents.protocol.world.chunk.palette.DataPalette;
import com.github.retrooper.packetevents.protocol.world.chunk.palette.PaletteType;
import com.github.retrooper.packetevents.protocol.world.chunk.palette.SingletonPalette;
import com.github.retrooper.packetevents.protocol.world.chunk.storage.BitStorage;
import com.github.retrooper.packetevents.util.Vector2i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Mojang name: ClientboundChunksBiomesPacket
 *
 * @version 1.19.4+
 */
public class WrapperPlayServerChunkBiomes extends PacketWrapper<WrapperPlayServerChunkBiomes> {

    private Map<Vector2i, ChunkBiomeData> chunks;

    public WrapperPlayServerChunkBiomes(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerChunkBiomes(Map<Vector2i, ChunkBiomeData> chunks) {
        super(PacketType.Play.Server.CHUNK_BIOMES);
        this.chunks = chunks;
    }

    @Override
    public void read() {
        this.chunks = this.readMap(Vector2i::read, ChunkBiomeData::read);
    }

    @Override
    public void write() {
        this.writeMap(this.chunks, Vector2i::write, ChunkBiomeData::write);
    }

    @Override
    public void copy(WrapperPlayServerChunkBiomes wrapper) {
        this.chunks = wrapper.chunks;
    }

    public Map<Vector2i, ChunkBiomeData> getChunks() {
        return this.chunks;
    }

    public ChunkBiomeData getChunk(Vector2i chunkPos) {
        return this.chunks.get(chunkPos);
    }

    public static class ChunkBiomeData {

        private final List<DataPalette> sections;

        private ChunkBiomeData(List<DataPalette> sections) {
            this.sections = sections;
        }

        public static ChunkBiomeData createWithRepeatingPalette(DataPalette palette, int sectionCount) {
            ChunkBiomeData data = new ChunkBiomeData(new ArrayList<>());
            for (int i = 0; i < sectionCount; i++) {
                data.sections.add(palette);
            }
            return data;
        }

        public static ChunkBiomeData createWithSingleBiome(Biome biome, ClientVersion version, int sectionCount) {
            return createWithSingleBiome(biome.getId(version), sectionCount);
        }

        public static ChunkBiomeData createWithSingleBiome(int biomeId, int sectionCount) {
            DataPalette palette = new DataPalette(
                    new SingletonPalette(biomeId),
                    new BitStorage(0, PaletteType.BIOME.getStorageSize()),
                    PaletteType.BIOME
            );
            return createWithRepeatingPalette(palette, sectionCount);
        }

        public static ChunkBiomeData read(PacketWrapper<?> wrapper) {
            int endIndex = ByteBufHelper.readerIndex(wrapper.buffer) + wrapper.readVarInt();
            List<DataPalette> sections = new ArrayList<>();
            while (ByteBufHelper.readerIndex(wrapper.buffer) < endIndex) {
                sections.add(PaletteType.BIOME.read(wrapper));
            }
            // ensure the length prefix matches
            if (ByteBufHelper.readerIndex(wrapper.buffer) != endIndex) {
                throw new IllegalStateException("Unexpected buffer position, expected " + endIndex + ", received " + wrapper.buffer);
            }
            return new ChunkBiomeData(sections);
        }

        public static void write(PacketWrapper<?> wrapper, ChunkBiomeData biomeData) {
            // fake write, we need to figure out how large this is
            // TODO use extra calculation instead of writing twice
            int startIndex = ByteBufHelper.writerIndex(wrapper.buffer);
            for (DataPalette biomePalette : biomeData.sections) {
                PaletteType.write(wrapper, biomePalette);
            }
            int dataLength = ByteBufHelper.writerIndex(wrapper.buffer) - startIndex;
            ByteBufHelper.writerIndex(wrapper.buffer, startIndex);

            // real write
            wrapper.writeVarInt(dataLength);
            for (DataPalette biomePalette : biomeData.sections) {
                PaletteType.write(wrapper, biomePalette);
            }
        }
    }
}
