package com.github.retrooper.packetevents.protocol.component.builtin.item;

import com.github.retrooper.packetevents.protocol.world.numbers.ResolvableInt;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * @versions 26.3+
 */
@NullMarked
public class ItemCompostable {

    private final ResolvableInt layers;

    public ItemCompostable(ResolvableInt layers) {
        this.layers = layers;
    }

    public static ItemCompostable read(PacketWrapper<?> wrapper) {
        ResolvableInt layers = ResolvableInt.read(wrapper);
        return new ItemCompostable(layers);
    }

    public static void write(PacketWrapper<?> wrapper, ItemCompostable compostable) {
        ResolvableInt.write(wrapper, compostable.layers);
    }

    public ResolvableInt getLayers() {
        return this.layers;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) return false;
        ItemCompostable that = (ItemCompostable) obj;
        return this.layers.equals(that.layers);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.layers);
    }
}
