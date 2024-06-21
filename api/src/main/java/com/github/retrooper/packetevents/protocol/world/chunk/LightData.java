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

    public LightData() {
    }

    public LightData(boolean trustEdges, BitSet blockLightMask, BitSet skyLightMask, BitSet emptyBlockLightMask, BitSet emptySkyLightMask, int skyLightCount, int blockLightCount, byte[][] skyLightArray, byte[][] blockLightArray) {
        this.trustEdges = trustEdges;
        this.blockLightMask = blockLightMask;
        this.skyLightMask = skyLightMask;
        this.emptyBlockLightMask = emptyBlockLightMask;
        this.emptySkyLightMask = emptySkyLightMask;
        this.skyLightCount = skyLightCount;
        this.blockLightCount = blockLightCount;
        this.skyLightArray = skyLightArray;
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
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
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

    public static LightData read(PacketWrapper<?> packet) {
        LightData lightData = new LightData();
        ServerVersion serverVersion = packet.getServerVersion();
        if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_19_4)) {
            lightData.trustEdges = packet.readBoolean();
        }

        lightData.skyLightMask = ChunkBitMask.readChunkMask(packet);
        lightData.blockLightMask = ChunkBitMask.readChunkMask(packet);
        lightData.emptySkyLightMask = ChunkBitMask.readChunkMask(packet);
        lightData.emptyBlockLightMask = ChunkBitMask.readChunkMask(packet);

        lightData.skyLightCount = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17) ? packet.readVarInt() : 18;
        lightData.skyLightArray = new byte[lightData.skyLightCount][];
        for (int x = 0; x < lightData.skyLightCount; x++) {
            if (lightData.skyLightMask.get(x)) {
                lightData.skyLightArray[x] = packet.readByteArray();
            }
        }

        lightData.blockLightCount = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17) ? packet.readVarInt() : 18;
        lightData.blockLightArray = new byte[lightData.blockLightCount][];
        for (int x = 0; x < lightData.blockLightCount; x++) {
            if (lightData.blockLightMask.get(x)) {
                lightData.blockLightArray[x] = packet.readByteArray();
            }
        }

        return lightData;
    }

    public static void write(PacketWrapper<?> packet, LightData lightData) {
        ServerVersion serverVersion = packet.getServerVersion();
        if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_19_4)) {
            packet.writeBoolean(lightData.trustEdges);
        }
        ChunkBitMask.writeChunkMask(packet, lightData.skyLightMask);
        ChunkBitMask.writeChunkMask(packet, lightData.blockLightMask);
        ChunkBitMask.writeChunkMask(packet, lightData.emptySkyLightMask);
        ChunkBitMask.writeChunkMask(packet, lightData.emptyBlockLightMask);

        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17))
            packet.writeVarInt(lightData.skyLightCount);
        for (int x = 0; x < lightData.skyLightCount; x++) {
            if (lightData.skyLightMask.get(x)) {
                packet.writeByteArray(lightData.skyLightArray[x]);
            }
        }

        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17))
            packet.writeVarInt(lightData.blockLightCount);
        for (int x = 0; x < lightData.blockLightCount; x++) {
            if (lightData.blockLightMask.get(x)) {
                packet.writeByteArray(lightData.blockLightArray[x]);
            }
        }
    }
}