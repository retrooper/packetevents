package com.github.retrooper.packetevents.protocol.nbt;

import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NBTLimiter {

    private final int max;
    @Nullable
    private Object byteBuf;
    private int bytes;

    public NBTLimiter() {
        this.max = Integer.MAX_VALUE;
    }

    public NBTLimiter(@NotNull Object byteBuf) {
        this.byteBuf = byteBuf;
        this.max = 2097152;
    }

    public void increment(int amount) {
        bytes += amount;

        if (bytes > max) throw new IllegalArgumentException("NBT size limit reached (" + bytes + "/" + max + ")");
    }

    public void checkReadability(int length) {
        if(byteBuf != null && length > ByteBufHelper.readableBytes(byteBuf)) throw new IllegalArgumentException("Length is too large: " + length + ", readable: " + ByteBufHelper.readableBytes(byteBuf));
    }
}
