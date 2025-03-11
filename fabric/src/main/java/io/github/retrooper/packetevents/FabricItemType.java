package io.github.retrooper.packetevents;

import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import java.util.Set;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

public class FabricItemType implements ItemType {

    private final Item item;

    public FabricItemType(Item item) {
        this.item = item;
    }

    @Override
    public int getMaxAmount() {
        return item.getMaxStackSize();
    }

    @Override
    public int getMaxDurability() {
        return item.getMaxDamage();
    }

    @Override
    public ItemType getCraftRemainder() {
        return new FabricItemType(item.getCraftingRemainingItem());
    }

    @Override
    public @Nullable StateType getPlacedType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<ItemTypes.ItemAttribute> getAttributes() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResourceLocation getName() {
        net.minecraft.resources.ResourceLocation resourceLocation = BuiltInRegistries.ITEM.getKey(item);
        return new ResourceLocation(resourceLocation.getNamespace(), resourceLocation.getPath());
    }

    @Override
    public int getId(ClientVersion version) {
        return BuiltInRegistries.ITEM.getId(item);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof FabricItemType) {
            FabricItemType fabricItemType = (FabricItemType) o;
            return this.item == fabricItemType.item;
        }
        return false;
    }
}
