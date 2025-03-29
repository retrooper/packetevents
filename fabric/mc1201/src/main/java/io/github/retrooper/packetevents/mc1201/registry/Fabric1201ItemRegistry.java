package io.github.retrooper.packetevents.mc1201.registry;

import com.github.retrooper.packetevents.manager.registry.ItemRegistry;
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import io.github.retrooper.packetevents.FabricItemType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class Fabric1201ItemRegistry implements ItemRegistry {

    @Override
    public @Nullable ItemType getByName(String name) {
        Optional<Item> item = BuiltInRegistries.ITEM.getOptional(ResourceLocation.tryParse(name));
        return item.isPresent() ? new FabricItemType(item.get()) : null;
    }
}
