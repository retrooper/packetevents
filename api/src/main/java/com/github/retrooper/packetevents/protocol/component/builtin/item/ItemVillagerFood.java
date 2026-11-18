package com.github.retrooper.packetevents.protocol.component.builtin.item;

import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * @versions 26.3+
 */
@NullMarked
public class ItemVillagerFood {

    private final int nutrition;

    public ItemVillagerFood(int nutrition) {
        this.nutrition = nutrition;
    }

    public static ItemVillagerFood read(PacketWrapper<?> wrapper) {
        int nutrition = wrapper.readVarInt();
        return new ItemVillagerFood(nutrition);
    }

    public static void write(PacketWrapper<?> wrapper, ItemVillagerFood food) {
        wrapper.writeVarInt(food.nutrition);
    }

    public int getNutrition() {
        return this.nutrition;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) return false;
        ItemVillagerFood that = (ItemVillagerFood) obj;
        return this.nutrition == that.nutrition;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.nutrition);
    }
}
