package com.github.retrooper.packetevents.protocol.world.chunk.impl.v1_8;

import com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import com.github.retrooper.packetevents.protocol.world.chunk.NibbleArray3d;
import com.github.retrooper.packetevents.protocol.world.chunk.ShortArray3d;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;

public class Chunk_v1_8 implements BaseChunk {
    private ShortArray3d blocks;
    private NibbleArray3d blocklight;
    private NibbleArray3d skylight;

    public Chunk_v1_8(boolean skylight) {
        this(new ShortArray3d(4096), new NibbleArray3d(4096), skylight ? new NibbleArray3d(4096) : null);
    }

    public Chunk_v1_8(ShortArray3d blocks, NibbleArray3d blocklight, NibbleArray3d skylight) {
        this.blocks = blocks;
        this.blocklight = blocklight;
        this.skylight = skylight;
    }

    public ShortArray3d getBlocks() {
        return this.blocks;
    }

    public NibbleArray3d getBlockLight() {
        return this.blocklight;
    }

    public NibbleArray3d getSkyLight() {
        return this.skylight;
    }

    @Override
    public WrappedBlockState get(int x, int y, int z) {
        int combinedID = this.blocks.get(x, y, z);
        return WrappedBlockState.getByGlobalId(combinedID);
    }

    @Override
    public void set(int x, int y, int z, int combinedID) {
        this.blocks.set(x, y, z, combinedID);
    }

    @Override
    public boolean isEmpty() {
        for(short block : this.blocks.getData()) {
            if(block != 0) {
                return false;
            }
        }

        return true;
    }
}
