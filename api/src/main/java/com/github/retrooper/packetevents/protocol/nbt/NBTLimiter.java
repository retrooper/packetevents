package com.github.retrooper.packetevents.protocol.nbt;

import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;

public class NBTLimiter {

    private final Object byteBuf;
    private final int max;
    private int bytes;

    public NBTLimiter(Object byteBuf, int max) {
        this.byteBuf = byteBuf;
        this.max = max;
    }

    public void increment(int amount) {
        bytes += amount;

        if (bytes > max) throw new IllegalArgumentException("NBT size limit reached (" + bytes + "/" + max + ")");
    }

    public void checkReadability(int length) {
        if(length > ByteBufHelper.readableBytes(byteBuf)) throw new IllegalArgumentException("Length is too large: " + length + ", readable: " + ByteBufHelper.readableBytes(byteBuf));
    }
}
