package io.github.retrooper.packetevents.mc1201.registry;

import com.github.retrooper.packetevents.manager.registry.ItemRegistry;
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import io.github.retrooper.packetevents.FabricItemType;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class Fabric1201ItemRegistry implements ItemRegistry {

    @Override
    public @Nullable ItemType getByName(String name) {
        Optional<Item> item = Registries.ITEM.getOrEmpty(Identifier.tryParse(name));
        return item.isPresent() ? new FabricItemType(item.get()) : null;
    }

    public @Nullable ItemType getByName(String name, boolean test) {
        Item item = Registries.ITEM.get(Identifier.tryParse(name));
        return new FabricItemType(item);
    }
}
