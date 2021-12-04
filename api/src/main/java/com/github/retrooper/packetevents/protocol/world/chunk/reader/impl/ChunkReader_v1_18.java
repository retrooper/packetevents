package com.github.retrooper.packetevents.protocol.world.chunk.reader.impl;

import com.github.retrooper.packetevents.protocol.stream.NetStreamInput;
import com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import com.github.retrooper.packetevents.protocol.world.chunk.impl.v1_16.Chunk_v1_16;
import com.github.retrooper.packetevents.protocol.world.chunk.reader.ChunkReader;

import java.io.ByteArrayInputStream;
import java.util.BitSet;

public class ChunkReader_v1_18 implements ChunkReader {
    @Override
    public BaseChunk[] read(BitSet set, BitSet sevenExtendedMask, boolean fullChunk, boolean hasSkyLight, boolean checkForSky, int chunkSize, byte[] data) {
        NetStreamInput dataIn = new NetStreamInput(new ByteArrayInputStream(data));
        BaseChunk[] chunks = new BaseChunk[chunkSize];

        for (int index = 0; index < chunkSize; ++index) {
            if (set.get(index)) {
                chunks[index] = new Chunk_v1_16(dataIn);

                // TODO: We need to store this biome data somewhere!

                // Skip past the biome data
                int length = dataIn.readUnsignedByte();

                // Simulate reading past the palette for biomes
                if (length > 3) { // Writes nothing
                    // do nothing
                } else if (length == 0) { // Writes the single member of the palette
                    dataIn.readVarInt(); // Read single member of palette
                } else { // Writes size, then var ints for each size
                    int paletteLength = dataIn.readVarInt();
                    for (int i = 0; i < paletteLength; i++) {
                        dataIn.readVarInt();
                    }
                }

                dataIn.readLongs(dataIn.readVarInt());
            }
        }

        return chunks;
    }
}
