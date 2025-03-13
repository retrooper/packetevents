package io.github.retrooper.packetevents.mc1214.manager.registry;

import com.github.retrooper.packetevents.manager.registry.ItemRegistry;
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import io.github.retrooper.packetevents.FabricItemType;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class Fabric1214ItemRegistry implements ItemRegistry {

    @Override
    public @Nullable ItemType getByName(String name) {
        Optional<Holder.Reference<Item>> optionalItemReference = BuiltInRegistries.ITEM.get(ResourceLocation.parse(name));
        return optionalItemReference.isPresent() ?  new FabricItemType(optionalItemReference.get().value()) : null;
    }
}