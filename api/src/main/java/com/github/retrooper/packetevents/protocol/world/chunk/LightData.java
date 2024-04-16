package com.github.retrooper.packetevents.protocol.world.chunk;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.BitSet;

public class LightData implements Cloneable {
    private boolean trustEdges;
    private BitSet blockLightMask;
    private BitSet skyLightMask;
    private BitSet emptyBlockLightMask;
    private BitSet emptySkyLightMask;
    private int skyLightCount;
    private int blockLightCount;
    private byte[][] skyLightArray;
    private byte[][] blockLightArray;

    public void read(PacketWrapper<?> packet) {
        ServerVersion serverVersion = packet.getServerVersion();
        if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_19_4)) {
            trustEdges = packet.readBoolean();
        }

        skyLightMask = ChunkBitMask.readChunkMask(packet);
        blockLightMask = ChunkBitMask.readChunkMask(packet);
        emptySkyLightMask = ChunkBitMask.readChunkMask(packet);
        emptyBlockLightMask = ChunkBitMask.readChunkMask(packet);

        skyLightCount = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17) ? packet.readVarInt() : 18;
        this.skyLightArray = new byte[skyLightCount][];
        for (int x = 0; x < skyLightCount; x++) {
            skyLightArray[x] = packet.readByteArray();
        }

        blockLightCount = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17) ? packet.readVarInt() : 18;
        this.blockLightArray = new byte[blockLightCount][];
        for (int x = 0; x < blockLightCount; x++) {
            blockLightArray[x] = packet.readByteArray();
        }
    }

    public void write(PacketWrapper<?> packet) {
        ServerVersion serverVersion = packet.getServerVersion();
        if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_19_4)) {
            packet.writeBoolean(trustEdges);
        }
        ChunkBitMask.writeChunkMask(packet, skyLightMask);
        ChunkBitMask.writeChunkMask(packet, blockLightMask);
        ChunkBitMask.writeChunkMask(packet, emptySkyLightMask);
        ChunkBitMask.writeChunkMask(packet, emptyBlockLightMask);

        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17))
            packet.writeVarInt(skyLightCount);
        for (int x = 0; x < skyLightCount; x++) {
            packet.writeByteArray(skyLightArray[x]);
        }

        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17))
            packet.writeVarInt(blockLightCount);
        for (int x = 0; x < blockLightCount; x++) {
            packet.writeByteArray(blockLightArray[x]);
        }
    }

    public boolean isTrustEdges() {
        return trustEdges;
    }

    public void setTrustEdges(boolean trustEdges) {
        this.trustEdges = trustEdges;
    }

    public BitSet getBlockLightMask() {
        return blockLightMask;
    }

    public void setBlockLightMask(BitSet blockLightMask) {
        this.blockLightMask = blockLightMask;
    }

    public BitSet getSkyLightMask() {
        return skyLightMask;
    }

    public void setSkyLightMask(BitSet skyLightMask) {
        this.skyLightMask = skyLightMask;
    }

    public BitSet getEmptyBlockLightMask() {
        return emptyBlockLightMask;
    }

    public void setEmptyBlockLightMask(BitSet emptyBlockLightMask) {
        this.emptyBlockLightMask = emptyBlockLightMask;
    }

    public BitSet getEmptySkyLightMask() {
        return emptySkyLightMask;
    }

    public void setEmptySkyLightMask(BitSet emptySkyLightMask) {
        this.emptySkyLightMask = emptySkyLightMask;
    }

    public int getSkyLightCount() {
        return skyLightCount;
    }

    public void setSkyLightCount(int skyLightCount) {
        this.skyLightCount = skyLightCount;
    }

    public int getBlockLightCount() {
        return blockLightCount;
    }

    public void setBlockLightCount(int blockLightCount) {
        this.blockLightCount = blockLightCount;
    }

    public byte[][] getSkyLightArray() {
        return skyLightArray;
    }

    public void setSkyLightArray(byte[][] skyLightArray) {
        this.skyLightArray = skyLightArray;
    }

    public byte[][] getBlockLightArray() {
        return blockLightArray;
    }

    public void setBlockLightArray(byte[][] blockLightArray) {
        this.blockLightArray = blockLightArray;
    }

    @Override
    public LightData clone() {
        try {
            LightData clone = (LightData) super.clone();
            clone.blockLightMask = (BitSet) blockLightMask.clone();
            clone.skyLightMask = (BitSet) skyLightMask.clone();
            clone.emptyBlockLightMask = (BitSet) emptyBlockLightMask.clone();
            clone.emptySkyLightMask = (BitSet) emptySkyLightMask.clone();
            clone.skyLightArray = skyLightArray.clone();
            clone.blockLightArray = blockLightArray.clone();
            clone.skyLightArray = skyLightArray.clone();
            clone.blockLightArray = blockLightArray.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}