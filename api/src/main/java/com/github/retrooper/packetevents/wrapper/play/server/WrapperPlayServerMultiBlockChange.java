package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

// Inspired heavily by MCProtocolLib
public class WrapperPlayServerMultiBlockChange extends PacketWrapper<WrapperPlayServerMultiBlockChange> {
    Vector3i chunkPosition;
    boolean trustEdges;
    EncodedBlock[] blockData;

    public WrapperPlayServerMultiBlockChange(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerMultiBlockChange(Vector3i chunkPosition, boolean trustEdges, EncodedBlock[] blockData) {
        super(PacketType.Play.Server.MULTI_BLOCK_CHANGE);
        this.chunkPosition = chunkPosition;
        this.trustEdges = trustEdges;
        this.blockData = blockData;
    }

    @Override
    public void readData() {
        long encodedPosition = readLong();

        int sectionX = (int) (encodedPosition >> 42);
        int sectionY = (int) (encodedPosition << 44 >> 44);
        int sectionZ = (int) (encodedPosition << 22 >> 42);
        chunkPosition = new Vector3i(sectionX, sectionY, sectionZ);

        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
            trustEdges = readBoolean();
        }

        blockData = new EncodedBlock[readVarInt()];
        for (int i = 0; i < blockData.length; i++) {
            blockData[i] = new EncodedBlock(chunkPosition, readVarLong());
        }
    }

    @Override
    public void readData(WrapperPlayServerMultiBlockChange wrapper) {
        chunkPosition = wrapper.chunkPosition;
        trustEdges = wrapper.trustEdges;
        blockData = wrapper.blockData;
    }

    @Override
    public void writeData() {
        long encodedPos = 0;
        encodedPos |= (chunkPosition.getX() & 0x3FFFFFL) << 42;
        encodedPos |= (chunkPosition.getZ() & 0x3FFFFFL) << 20;
        writeLong(encodedPos | (chunkPosition.getY() & 0xFFFFFL));

        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
            writeBoolean(trustEdges);
        }

        writeVarInt(blockData.length);
        for (EncodedBlock blockDatum : blockData) {
            writeVarLong(blockDatum.toLong());
        }
    }

    public Vector3i getChunkPosition() {
        return chunkPosition;
    }

    public boolean getTrustEdges() {
        return trustEdges;
    }

    public EncodedBlock[] getBlocks() {
        return blockData;
    }

    public void setBlocks(EncodedBlock[] blocks) {
        this.blockData = blocks;
    }

    public static class EncodedBlock {
        private int blockID;
        private int x;
        private int y;
        private int z;

        public EncodedBlock(int blockID, int x, int y, int z) {
            this.blockID = blockID;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public EncodedBlock(WrappedBlockState blockState, int x, int y, int z) {
            this(blockState.getGlobalId(), x, y, z);
        }

        public EncodedBlock(Vector3i chunk, long data) {
            // Reverse blockStateId << 12 | (blockLocalX << 8 | blockLocalZ << 4 | blockLocalY)
            short position = (short) (data & 0xFFFL);

            x = (chunk.getX() << 4) + (position >>> 8 & 0xF);
            y = (chunk.getY() << 4) + (position & 0xF);
            z = (chunk.getZ() << 4) + (position >>> 4 & 0xF);

            this.blockID = (int) (data >>> 12);
        }

        public long toLong() {
            return (long) blockID << 12 | (x & 0xF) << 8 | (z & 0xF) << 4 | (y & 0xF);
        }

        public int getBlockID() {
            return blockID;
        }

        public void setBlockID(int blockID) {
            this.blockID = blockID;
        }

        public WrappedBlockState getBlockState() {
            return WrappedBlockState.getByGlobalId(blockID);
        }

        public void setBlockState(WrappedBlockState blockState) {
            blockID = blockState.getGlobalId();
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getZ() {
            return z;
        }

        public int setX(int x) {
            return this.x = x;
        }

        public int setY(int y) {
            return this.y = y;
        }

        public int setZ(int z) {
            return this.z = z;
        }
    }
}
