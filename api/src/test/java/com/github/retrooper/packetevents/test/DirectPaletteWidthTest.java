package com.github.retrooper.packetevents.test;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.world.chunk.impl.v1_16.Chunk_v1_9;
import com.github.retrooper.packetevents.protocol.world.biome.Biome;
import com.github.retrooper.packetevents.protocol.world.biome.Biomes;
import com.github.retrooper.packetevents.protocol.world.chunk.impl.v_1_18.Chunk_v1_18;
import com.github.retrooper.packetevents.protocol.world.chunk.palette.DataPalette;
import com.github.retrooper.packetevents.protocol.world.chunk.palette.GlobalPalette;
import com.github.retrooper.packetevents.protocol.world.chunk.palette.PaletteType;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.test.base.BaseDummyAPITest;
import com.github.retrooper.packetevents.util.mappings.SimpleRegistry;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class DirectPaletteWidthTest extends BaseDummyAPITest {

    @ParameterizedTest
    @CsvSource({"V_26_2, 15, 32365", "V_26_3, 16, 35722"})
    void blockPaletteUsesTheSectionVersion(ClientVersion version, int bits, int highestState) {
        DataPalette palette = new Chunk_v1_18(version).getChunkData();
        for (int i = 0; i < 256; i++) {
            palette.set(i & 15, 0, i >> 4, i);
        }
        palette.set(0, 1, 0, highestState);

        assertSame(GlobalPalette.INSTANCE, palette.palette);
        assertEquals(highestState + 1, WrappedBlockState.getRegistrySize(version));
        assertEquals(highestState, palette.get(0, 1, 0));
        for (int i = 0; i < 256; i++) {
            assertEquals(i, palette.get(i & 15, 0, i >> 4));
        }
        assertEncoding(version, palette, bits, 1024);
    }

    @ParameterizedTest
    @CsvSource({"V_1_18, 6, 7", "V_26_2, 7, 8", "V_26_3, 7, 8"})
    void biomePaletteUsesTheVanillaRegistry(ClientVersion version, int bits, int longs) {
        DataPalette palette = new Chunk_v1_18(version).getBiomeData();
        fillBiomePalette(palette);

        assertSame(GlobalPalette.INSTANCE, palette.palette);
        assertEncoding(version, palette, bits, longs);
    }

    @Test
    @SuppressWarnings("deprecation")
    void biomePaletteCreatedWithoutVersionUsesTheServerRegistry() {
        ClientVersion version = PacketEvents.getAPI().getServerManager().getVersion().toClientVersion();
        DataPalette palette = DataPalette.createForBiome();
        fillBiomePalette(palette);

        assertEncoding(version, palette, 7, 8);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void biomePaletteUsesThePacketRegistry(boolean readFromPacket) {
        ClientVersion version = ClientVersion.V_26_3;
        ByteBuf buffer = Unpooled.buffer();
        PacketWrapper<?> wrapper = PacketWrapper.createUniversalPacketWrapper(buffer, version.toServerVersion());
        try {
            SimpleRegistry<Biome> registry = new SimpleRegistry<>(Biomes.getRegistry().getRegistryKey());
            for (int i = 0; i < 129; i++) {
                registry.define("test:biome_" + i, i, Biomes.PLAINS);
            }
            wrapper.setRegistryHolder((key, ignored) -> key.equals(registry.getRegistryKey()) ? registry : null);

            DataPalette palette;
            if (readFromPacket) {
                wrapper.writeByte(0);
                wrapper.writeVarInt(0);
                palette = PaletteType.BIOME.read(wrapper);
            } else {
                palette = PaletteType.BIOME.create(wrapper);
            }
            fillBiomePalette(palette);
            palette.set(0, 0, 0, 128);

            assertSame(GlobalPalette.INSTANCE, palette.palette);
            assertEquals(128, palette.get(0, 0, 0));
            assertEncoding(version, palette, 8, 8);
        } finally {
            buffer.release();
        }
    }

    @Test
    void olderChunkRetainsContiguousPackingWhenPromoted() {
        ClientVersion version = ClientVersion.V_1_13;
        ByteBuf buffer = Unpooled.buffer();
        PacketWrapper<?> wrapper = PacketWrapper.createUniversalPacketWrapper(buffer, version.toServerVersion());
        try {
            wrapper.writeByte(8);
            wrapper.writeVarInt(256);
            for (int i = 0; i < 256; i++) {
                wrapper.writeVarInt(i);
            }
            wrapper.writeVarInt(512);
            for (int i = 0; i < 512; i++) {
                wrapper.writeLong(0);
            }

            DataPalette palette = Chunk_v1_9.read(wrapper, false, false).getDataPalette();
            palette.set(4, 0, 0, 8598);

            assertEquals(8598, palette.get(4, 0, 0));
            assertEncoding(version, palette, 14, 896);
        } finally {
            buffer.release();
        }
    }

    @Test
    void directPaletteReadFromPacketKeepsTheWireWidth() {
        ClientVersion version = ClientVersion.V_26_2;
        ByteBuf buffer = Unpooled.buffer();
        PacketWrapper<?> wrapper = PacketWrapper.createUniversalPacketWrapper(buffer, version.toServerVersion());
        try {
            wrapper.writeByte(8);
            for (int i = 0; i < 8; i++) {
                wrapper.writeLong(0);
            }
            wrapper.writeByte(42);

            DataPalette palette = PaletteType.BIOME.read(wrapper);
            assertSame(GlobalPalette.INSTANCE, palette.palette);
            assertEquals(8, palette.storage.getBitsPerEntry());
            assertEquals(42, wrapper.readByte());
        } finally {
            buffer.release();
        }
    }

    private static void fillBiomePalette(DataPalette palette) {
        for (int i = 0; i < 64; i++) {
            palette.set(i & 3, i >> 4, (i >> 2) & 3, i % 9);
        }
        for (int i = 0; i < 64; i++) {
            assertEquals(i % 9, palette.get(i & 3, i >> 4, (i >> 2) & 3));
        }
    }

    private static void assertEncoding(ClientVersion version, DataPalette palette, int bits, int longs) {
        ByteBuf buffer = Unpooled.buffer();
        PacketWrapper<?> wrapper = PacketWrapper.createUniversalPacketWrapper(buffer, version.toServerVersion());
        try {
            PaletteType.write(wrapper, palette);

            assertEquals(bits, palette.storage.getBitsPerEntry());
            assertEquals(longs, palette.storage.getData().length);
            assertEquals(bits, wrapper.readByte());
            if (version.isOlderThan(ClientVersion.V_1_21_5)) {
                assertEquals(longs, wrapper.readVarInt());
            }
            assertEquals(longs * Long.BYTES, buffer.readableBytes());
        } finally {
            buffer.release();
        }
    }
}
