package com.github.retrooper.packetevents.test;

import com.github.retrooper.packetevents.protocol.nbt.NBTLimiter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class NBTLimiterTest {

    @Test
    public void testSizeAccountingDoesNotOverflow() {
        NBTLimiter limiter = NBTLimiter.forBuffer(new Object(), Integer.MAX_VALUE - 1);
        limiter.increment(Integer.MAX_VALUE - 2);

        // The sum exceeds the configured limit and also overflows a signed int.
        assertThrows(IllegalArgumentException.class, () -> limiter.increment(4));
    }

    @Test
    public void testExactLimitAllowed() {
        NBTLimiter limiter = NBTLimiter.forBuffer(new Object(), 64);
        limiter.increment(32);
        limiter.increment(32);

        assertThrows(IllegalArgumentException.class, () -> limiter.increment(1));
    }

    @Test
    public void testNegativeIncrementsAreRejected() {
        NBTLimiter limiter = NBTLimiter.forBuffer(new Object(), 64);
        assertThrows(IllegalArgumentException.class, () -> limiter.increment(-1));
    }
}
