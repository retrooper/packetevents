package com.github.retrooper.packetevents.protocol.world.chunk;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;

import java.util.BitSet;

@ApiStatus.Internal
public class ChunkBitMask {

    private ChunkBitMask() {
    }

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
        long[] longArray = chunkMask.toLongArray();

        if (packet.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_17)) {
            //Write primary bit mask
            packet.writeLongArray(longArray);
        } else if (packet.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
            if (longArray.length > 0) {
                packet.writeVarInt((int) longArray[0]);
            } else {
                packet.writeVarInt(0);
            }
        } else {
            if (longArray.length > 0) {
                packet.writeShort((int) longArray[0]);
            } else {
                packet.writeShort(0);
            }
        }
    }

}