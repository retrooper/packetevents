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
public class ItemCookingFuel {

    private final ResolvableInt burnTime;
    private final ResolvableFloat speedMultiplier;

    public ItemCookingFuel(ResolvableInt burnTime, ResolvableFloat speedMultiplier) {
        this.burnTime = burnTime;
        this.speedMultiplier = speedMultiplier;
    }

    public static ItemCookingFuel read(PacketWrapper<?> wrapper) {
        ResolvableInt uses = ResolvableInt.read(wrapper);
        ResolvableFloat speedMultiplier = ResolvableFloat.read(wrapper);
        return new ItemCookingFuel(uses, speedMultiplier);
    }

    public static void write(PacketWrapper<?> wrapper, ItemCookingFuel fuel) {
        ResolvableInt.write(wrapper, fuel.burnTime);
        ResolvableFloat.write(wrapper, fuel.speedMultiplier);
    }

    public ResolvableInt getBurnTime() {
        return this.burnTime;
    }

    public ResolvableFloat getSpeedMultiplier() {
        return this.speedMultiplier;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) return false;
        ItemCookingFuel that = (ItemCookingFuel) obj;
        if (!this.burnTime.equals(that.burnTime)) return false;
        return this.speedMultiplier.equals(that.speedMultiplier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.burnTime, this.speedMultiplier);
    }
}
