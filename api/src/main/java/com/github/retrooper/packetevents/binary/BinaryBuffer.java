package com.github.retrooper.packetevents.binary;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;

public final class BinaryBuffer {

    public static final BinaryBufferType<Byte> BYTE = new BinaryBufferTypes.Byte();
    public static final BinaryBufferType<Boolean> BOOLEAN = new BinaryBufferTypes.Bool();
    public static final BinaryBufferType<Integer> INT = new BinaryBufferTypes.Int();
    public static final BinaryBufferType<Float> FLOAT = new BinaryBufferTypes.Float();
    public static final BinaryBufferType<Double> DOUBLE = new BinaryBufferTypes.Double();
    public static final BinaryBufferType<Long> LONG = new BinaryBufferTypes.Long();
    public static final BinaryBufferType<Short> SHORT = new BinaryBufferTypes.Short();
    public static final BinaryBufferType<Integer> VAR_INT = new BinaryBufferTypes.VarInt();

    private Object buffer;

    public BinaryBuffer(Object buffer) {
        this.buffer = buffer;
    }

    public BinaryBuffer(int capacity, boolean io) {
        if (io) {
            this.buffer = PacketEvents.getAPI().getNettyManager().getByteBufAllocationOperator().buffer(capacity);
        }
        else {
            this.buffer = PacketEvents.getAPI().getNettyManager().getByteBufAllocationOperator().directBuffer(capacity);
        }
    }

    public BinaryBuffer(int capacity) {
        this(capacity, true);
    }

    public BinaryBuffer(byte[] wrappedData) {
        this.buffer = PacketEvents.getAPI().getNettyManager().getByteBufAllocationOperator().wrappedBuffer(wrappedData);
    }

    public <T> T read(BinaryBufferType<T> type, ServerVersion serverVersion, ClientVersion clientVersion) {
        return type.read(this, serverVersion, clientVersion);
    }

    public <T> T read(BinaryBufferType<T> type, ClientVersion clientVersion) {
        return type.read(this, clientVersion);
    }

    public <T> T read(BinaryBufferType<T> type) {
        return type.read(this);
    }

    public <T> void write(BinaryBufferType<T> type, T value, ServerVersion serverVersion, ClientVersion clientVersion) {
        type.write(this, value, serverVersion, clientVersion);
    }

    public <T> void write(BinaryBufferType<T> type, T value, ClientVersion clientVersion) {
        type.write(this, value, clientVersion);
    }

    public <T> void write(BinaryBufferType<T> type, T value) {
        type.write(this, value);
    }



}
