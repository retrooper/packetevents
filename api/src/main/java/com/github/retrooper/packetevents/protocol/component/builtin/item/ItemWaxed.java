package com.github.retrooper.packetevents.protocol.component.builtin.item;

import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

/**
 * @versions 26.3+
 */
@NullMarked
public final class ItemWaxed {

    public static final ItemWaxed INSTANCE = new ItemWaxed();

    private ItemWaxed() {
    }

    public static ItemWaxed read(PacketWrapper<?> wrapper) {
        return INSTANCE;
    }

    public static void write(PacketWrapper<?> wrapper, ItemWaxed waxed) {
        // no-op
    }
}
