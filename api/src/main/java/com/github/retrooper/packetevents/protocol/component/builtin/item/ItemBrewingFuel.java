package com.github.retrooper.packetevents.protocol.component.builtin.item;

import com.github.retrooper.packetevents.protocol.world.numbers.ResolvableFloat;
import com.github.retrooper.packetevents.protocol.world.numbers.ResolvableInt;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * @versions 26.3+
 */
@NullMarked
public class ItemBrewingFuel {

    private final ResolvableInt uses;
    private final ResolvableFloat speedMultiplier;

    public ItemBrewingFuel(ResolvableInt uses, ResolvableFloat speedMultiplier) {
        this.uses = uses;
        this.speedMultiplier = speedMultiplier;
    }

    public static ItemBrewingFuel read(PacketWrapper<?> wrapper) {
        ResolvableInt uses = ResolvableInt.read(wrapper);
        ResolvableFloat speedMultiplier = ResolvableFloat.read(wrapper);
        return new ItemBrewingFuel(uses, speedMultiplier);
    }

    public static void write(PacketWrapper<?> wrapper, ItemBrewingFuel fuel) {
        ResolvableInt.write(wrapper, fuel.uses);
        ResolvableFloat.write(wrapper, fuel.speedMultiplier);
    }

    public ResolvableInt getUses() {
        return this.uses;
    }

    public ResolvableFloat getSpeedMultiplier() {
        return this.speedMultiplier;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) return false;
        ItemBrewingFuel that = (ItemBrewingFuel) obj;
        if (!this.uses.equals(that.uses)) return false;
        return this.speedMultiplier.equals(that.speedMultiplier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.uses, this.speedMultiplier);
    }
}
