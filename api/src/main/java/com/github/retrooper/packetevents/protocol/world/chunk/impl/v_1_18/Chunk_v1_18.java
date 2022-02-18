package com.github.retrooper.packetevents.protocol.world.chunk.impl.v_1_18;

import com.github.retrooper.packetevents.protocol.stream.NetStreamInput;
import com.github.retrooper.packetevents.protocol.stream.NetStreamOutput;
import com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import com.github.retrooper.packetevents.protocol.world.chunk.palette.DataPalette;
import com.github.retrooper.packetevents.protocol.world.chunk.palette.PaletteType;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import org.jetbrains.annotations.NotNull;

public class Chunk_v1_18 implements BaseChunk {
    private static final int AIR = 0;

    private int blockCount;
    private @NotNull
    final DataPalette chunkData;
    private @NotNull
    final DataPalette biomeData;

    public Chunk_v1_18() {
        this(0, DataPalette.createForChunk(), DataPalette.createForBiome());
    }

    public Chunk_v1_18(final int blockCount, final @NotNull DataPalette chunkData, final @NotNull DataPalette biomeData) {
        this.blockCount = blockCount;
        this.chunkData = chunkData;
        this.biomeData = biomeData;
    }

    public static Chunk_v1_18 read(NetStreamInput in)  {
        int blockCount = in.readShort();

        DataPalette chunkPalette = DataPalette.read(in, PaletteType.CHUNK);
        DataPalette biomePalette = DataPalette.read(in, PaletteType.BIOME);
        return new Chunk_v1_18(blockCount, chunkPalette, biomePalette);
    }

    public static void write(NetStreamOutput out, Chunk_v1_18 section)  {
        out.writeShort(section.blockCount);
        DataPalette.write(out, section.chunkData);
        DataPalette.write(out, section.biomeData);
    }

    @Override
    public WrappedBlockState get(int x, int y, int z) {
        return WrappedBlockState.getByGlobalId(this.chunkData.get(x, y, z));
    }

    @Override
    public void set(int x, int y, int z, int state) {
        int curr = this.chunkData.set(x, y, z, state);
        if (state != AIR && curr == AIR) {
            this.blockCount++;
        } else if (state == AIR && curr != AIR) {
            this.blockCount--;
        }
    }

    @Override
    public boolean isEmpty() {
        return this.blockCount == 0;
    }

    public int getBlockCount() {
        return blockCount;
    }

    public void setBlockCount(int blockCount) {
        this.blockCount = blockCount;
    }

    public @NotNull DataPalette getChunkData() {
        return chunkData;
    }

    public @NotNull DataPalette getBiomeData() {
        return biomeData;
    }
}