package com.github.retrooper.packetevents.protocol.world.chunk;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.BitSet;

public class ChunkBitMask {

    public static long[] readBitSetLongs(PacketWrapper<?> packet) {
        if (packet.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_17)) {
            //Read primary bit mask
            return packet.readLongArray();
        } else if (packet.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
            //Read primary bit mask
            return new long[]{packet.readVarInt()};
        } else {
            //Read primary bit mask
            return new long[]{packet.readUnsignedShort()};
        }
    }

    public static BitSet readChunkMask(PacketWrapper<?> packet) {
        return BitSet.valueOf(readBitSetLongs(packet));
    }

    public static void writeChunkMask(PacketWrapper<?> packet, BitSet chunkMask) {
        if (packet.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_17)) {
            //Write primary bit mask
            long[] longArray = chunkMask.toLongArray();
            packet.writeLongArray(longArray);
        } else if (packet.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
            //Write primary bit mask
            packet.writeVarInt((int) chunkMask.toLongArray()[0]);
        } else {
            packet.writeShort((int) chunkMask.toLongArray()[0]);
        }
    }

}
