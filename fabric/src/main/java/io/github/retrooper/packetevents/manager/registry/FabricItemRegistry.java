package io.github.retrooper.packetevents.manager.registry;

import com.github.retrooper.packetevents.manager.registry.ItemRegistry;
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import io.github.retrooper.packetevents.FabricItemType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

public class FabricItemRegistry implements ItemRegistry {

    @Override
    public @Nullable ItemType getByName(String name) {
        Item item = BuiltInRegistries.ITEM.get(ResourceLocation.of(name, ':'));
        return new FabricItemType(item);
    }
}
