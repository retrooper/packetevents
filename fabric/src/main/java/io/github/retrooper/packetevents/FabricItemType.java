package io.github.retrooper.packetevents;

import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import java.util.Set;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

public class FabricItemType implements ItemType {

    private final Item item;

    public FabricItemType(Item item) {
        this.item = item;
    }

    @Override
    public int getMaxAmount() {
        return item.getMaxAmount();
    }

    @Override
    public int getMaxDurability() {
        return item.getDurability();
    }

    @Override
    public ItemType getCraftRemainder() {
        return new FabricItemType(item.getRecipeRemainder());
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
        Identifier resourceLocation = Registry.ITEM.getId(item);
        return new ResourceLocation(resourceLocation.getNamespace(), resourceLocation.getPath());
    }

    @Override
    public int getId(ClientVersion version) {
        return Registry.ITEM.getRawId(item);
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
