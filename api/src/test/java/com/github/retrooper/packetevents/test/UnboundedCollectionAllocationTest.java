package com.github.retrooper.packetevents.test;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.netty.buffer.UnpooledByteBufAllocationHelper;
import com.github.retrooper.packetevents.util.KnownPack;
import com.github.retrooper.packetevents.test.base.BaseDummyAPITest;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * A collection length read off the wire must not size an allocation before the elements are read.
 *
 * <p>The unbounded readers take the count straight from the buffer and hand it to the collection
 * constructor, so {@code readList} builds an {@code ArrayList} with that capacity and
 * {@code readArray} calls {@code Array.newInstance} with that length. Both allocate before a
 * single element is touched. A count of {@code Integer.MAX_VALUE} fits in five bytes, so a packet
 * with nothing after the count asks for an array of two billion references.</p>
 *
 * <p>Three serverbound wrappers reach the unbounded path, which is what makes this remotely
 * triggerable rather than theoretical: {@code WrapperConfigClientSelectKnownPacks},
 * {@code WrapperPlayClientSetGameRule} and {@code WrapperPlayClientDebugSubscriptionRequest}.</p>
 *
 * <p>{@code readByteArray()} in the same class already gets this right — it bounds the length by
 * the readable bytes left in the buffer. These tests hold the collection readers to that same
 * rule: an element costs at least one byte on the wire, so a count larger than the bytes that
 * remain cannot be honest.</p>
 */
public class UnboundedCollectionAllocationTest extends BaseDummyAPITest {

    /** A wrapper over a real buffer; createDummyWrapper alone leaves the buffer null. */
    private static PacketWrapper<?> emptyWrapper() {
        PacketWrapper<?> wrapper = PacketWrapper.createDummyWrapper(ClientVersion.V_1_21_6);
        wrapper.setBuffer(UnpooledByteBufAllocationHelper.buffer());
        return wrapper;
    }

    /** Nothing follows the count, so every one of these claimed elements is a lie. */
    private static PacketWrapper<?> wrapperClaiming(int count) {
        PacketWrapper<?> wrapper = emptyWrapper();
        wrapper.writeVarInt(count);
        return wrapper;
    }

    /**
     * Fails on an unpatched build: the {@code ArrayList} capacity is allocated first, so the JVM
     * is asked for two billion references and answers with {@link OutOfMemoryError} instead of a
     * decode error. The allocation is refused outright rather than filling the heap, so catching
     * it here does not leave the test JVM in a bad state.
     */
    @Test
    @DisplayName("readList refuses a count larger than the buffer could possibly hold")
    public void readListRejectsImpossibleCount() {
        PacketWrapper<?> wrapper = wrapperClaiming(Integer.MAX_VALUE);
        try {
            wrapper.readList(PacketWrapper::readKnownPack);
            fail("a count of Integer.MAX_VALUE with no elements behind it must not be accepted");
        } catch (OutOfMemoryError oom) {
            fail("the count was used to size an allocation before any element was read: " + oom);
        } catch (RuntimeException expected) {
            // Refused while decoding, which is the point.
        }
    }

    @Test
    @DisplayName("readArray refuses a count larger than the buffer could possibly hold")
    public void readArrayRejectsImpossibleCount() {
        PacketWrapper<?> wrapper = wrapperClaiming(Integer.MAX_VALUE);
        try {
            wrapper.readArray(PacketWrapper::readKnownPack,
                    KnownPack.class);
            fail("a count of Integer.MAX_VALUE with no elements behind it must not be accepted");
        } catch (OutOfMemoryError oom) {
            fail("the length was used to size an allocation before any element was read: " + oom);
        } catch (RuntimeException expected) {
            // Refused while decoding, which is the point.
        }
    }

    @Test
    @DisplayName("readMap refuses a count larger than the buffer could possibly hold")
    public void readMapRejectsImpossibleCount() {
        PacketWrapper<?> wrapper = wrapperClaiming(Integer.MAX_VALUE);
        try {
            wrapper.readMap(PacketWrapper::readString, PacketWrapper::readString);
            fail("a count of Integer.MAX_VALUE with no entries behind it must not be accepted");
        } catch (OutOfMemoryError oom) {
            fail("the count was used to size the map before any entry was read: " + oom);
        } catch (RuntimeException expected) {
            // Refused while decoding, which is the point.
        }
    }

    /**
     * Measured, not assumed: a negative count was already refused before this change, because
     * the collection constructor rejects it. It is kept here so that stays true.
     */
    @Test
    @DisplayName("a negative count is refused rather than reaching the allocator")
    public void negativeCountIsRefused() {
        PacketWrapper<?> wrapper = wrapperClaiming(-1);
        assertThrows(RuntimeException.class,
                () -> wrapper.readList(PacketWrapper::readKnownPack),
                "a negative element count must not reach the collection constructor");
    }

    /** The bound must not break packets that are telling the truth. */
    @Test
    @DisplayName("an honest list still round-trips")
    public void honestListStillWorks() {
        PacketWrapper<?> wrapper = emptyWrapper();
        wrapper.writeList(java.util.Arrays.asList(
                new KnownPack("minecraft", "core", "1.21.6"),
                new KnownPack("example", "pack", "1.0")
        ), PacketWrapper::writeKnownPack);
        assertDoesNotThrow(() -> {
            java.util.List<?> read = wrapper.readList(PacketWrapper::readKnownPack);
            if (read.size() != 2) {
                fail("expected the two packs back, got " + read.size());
            }
        });
    }
}
