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
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

// Inspired heavily by MCProtocolLib
public class WrapperPlayServerMultiBlockChange extends PacketWrapper<WrapperPlayServerMultiBlockChange> {
    private Vector3i chunkPosition;
    //Suppress light
    private Boolean trustEdges;
    private EncodedBlock[] blockData;

    public WrapperPlayServerMultiBlockChange(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerMultiBlockChange(Vector3i chunkPosition, @Nullable Boolean trustEdges, EncodedBlock[] blockData) {
        super(PacketType.Play.Server.MULTI_BLOCK_CHANGE);
        this.chunkPosition = chunkPosition;
        this.trustEdges = trustEdges;
        this.blockData = blockData;
    }

    @Override
    public void read() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
            long encodedPosition = readLong();

            int sectionX = (int) (encodedPosition >> 42);
            int sectionY = (int) (encodedPosition << 44 >> 44);
            int sectionZ = (int) (encodedPosition << 22 >> 42);
            chunkPosition = new Vector3i(sectionX, sectionY, sectionZ);

            if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_19_4)) {
                trustEdges = readBoolean();
            }

            blockData = new EncodedBlock[readVarInt()];
            for (int i = 0; i < blockData.length; i++) {
                blockData[i] = new EncodedBlock(chunkPosition, readVarLong());
            }
        } else { // Copied from MCProtocolLib
            int chunkX = readInt();
            int chunkZ = readInt();
            chunkPosition = new Vector3i(chunkX, 0, chunkZ);
            int len = readVarInt();
            blockData = new EncodedBlock[len];
            for (int i = 0; i < len; i++) {
                short pos = readShort();
                //chunkX << 4 = chunkX * 16
                int x = (chunkX << 4) + (pos >> 12 & 15);
                int y = pos & 255;
                int z = (chunkZ << 4) + (pos >> 8 & 15);
                int blockId = readVarInt();
                this.blockData[i] = new EncodedBlock(blockId, x, y, z);
            }
        }
    }

    @Override
    public void write() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
            long encodedPos = 0;
            encodedPos |= (chunkPosition.getX() & 0x3FFFFFL) << 42;
            encodedPos |= (chunkPosition.getZ() & 0x3FFFFFL) << 20;
            writeLong(encodedPos | (chunkPosition.getY() & 0xFFFFFL));

            if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_19_4)) {
                writeBoolean(Boolean.TRUE.equals(trustEdges));
            }

            writeVarInt(blockData.length);
            for (EncodedBlock blockDatum : blockData) {
                writeVarLong(blockDatum.toLong());
            }
        } else { // Copied from MCProtocolLib
            writeInt(chunkPosition.getX());
            writeInt(chunkPosition.getZ());
            writeVarInt(this.blockData.length);
            for (EncodedBlock record : blockData) {
                int x = record.getX() & 0xF;
                int z = record.getZ() & 0xF;
                short pos = (short) (x << 12 | z << 8 | record.getY());
                writeShort(pos);
                writeVarInt(record.getBlockId());
            }
        }
    }

    @Override
    public void copy(WrapperPlayServerMultiBlockChange wrapper) {
        chunkPosition = wrapper.chunkPosition;
        trustEdges = wrapper.trustEdges;
        blockData = wrapper.blockData;
    }

    public Vector3i getChunkPosition() {
        return chunkPosition;
    }

    public void setChunkPosition(Vector3i chunkPosition) {
        this.chunkPosition = chunkPosition;
    }

    public boolean getTrustEdges() {
        return Boolean.TRUE.equals(trustEdges);
    }

    public void setTrustEdges(Boolean trustEdges) {
        this.trustEdges = trustEdges;
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

        public int getBlockId() {
            return blockID;
        }

        public void setBlockId(int blockID) {
            this.blockID = blockID;
        }

        public WrappedBlockState getBlockState(ClientVersion version) {
            return WrappedBlockState.getByGlobalId(version, blockID);
        }

        public void setBlockState(WrappedBlockState blockState) {
            blockID = blockState.getGlobalId();
        }

        /**
         * @return Global X position of block
         */
        public int getX() {
            return x;
        }

        /**
         * @return Global Y position of block
         */
        public int getY() {
            return y;
        }

        /**
         * @return Global Z position of block
         */
        public int getZ() {
            return z;
        }

        /**
         * @param x Global X position of block
         */
        public void setX(int x) {
            this.x = x;
        }

        /**
         * @param y Global Y position of block
         */
        public void setY(int y) {
            this.y = y;
        }

        /**
         * @param z Global Z position of block
         */
        public void setZ(int z) {
            this.z = z;
        }
    }
}
