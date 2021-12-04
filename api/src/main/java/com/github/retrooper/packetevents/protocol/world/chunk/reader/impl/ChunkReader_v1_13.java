package com.github.retrooper.packetevents.protocol.world.chunk.reader.impl;

import com.github.retrooper.packetevents.protocol.stream.NetStreamInput;
import com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import com.github.retrooper.packetevents.protocol.world.chunk.impl.v1_13.Chunk_v1_13;
import com.github.retrooper.packetevents.protocol.world.chunk.reader.ChunkReader;

import java.io.ByteArrayInputStream;
import java.util.BitSet;

public class ChunkReader_v1_13 implements ChunkReader {
    @Override
    public BaseChunk[] read(BitSet set, BitSet sevenExtendedMask, boolean fullChunk, boolean hasSkyLight, boolean checkForSky, int chunkSize, byte[] data) {
        NetStreamInput dataIn = new NetStreamInput(new ByteArrayInputStream(data));
        BaseChunk[] chunks = new BaseChunk[chunkSize];

        for (int index = 0; index < chunks.length; ++index) {
            if (set.get(index)) {
                chunks[index] = new Chunk_v1_13(dataIn);
            }
        }

        return chunks;
    }
}
