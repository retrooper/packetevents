package com.github.retrooper.packetevents.protocol.nbt;

import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import org.jetbrains.annotations.NotNull;

public interface NBTLimiter {

    int DEFAULT_MAX_SIZE = 2097152;

    static NBTLimiter noop() {
        return new NBTLimiter() {
            @Override
            public void increment(int amount) {
                // no-op
            }

            @Override
            public void checkReadability(int length) {
                // no-op
            }
        };
    }

    static NBTLimiter forBuffer(final @NotNull Object byteBuf) {
        return forBuffer(byteBuf, DEFAULT_MAX_SIZE);
    }

    static NBTLimiter forBuffer(final @NotNull Object byteBuf, final int max) {
        return new NBTLimiter() {
            private int bytes;

            @Override
            public void increment(int amount) {
                bytes += amount;

                if (bytes > max) throw new IllegalArgumentException("NBT size limit reached (" + bytes + "/" + max + ")");
            }

            @Override
            public void checkReadability(int length) {
                if (length > ByteBufHelper.readableBytes(byteBuf)) throw new IllegalArgumentException("Length is too large: " + length + ", readable: " + ByteBufHelper.readableBytes(byteBuf));
            }
        };
    }

    void increment(int amount);

    void checkReadability(int length);

}
