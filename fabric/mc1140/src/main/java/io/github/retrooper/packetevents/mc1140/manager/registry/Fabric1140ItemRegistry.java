package io.github.retrooper.packetevents.mc1140.manager.registry;

import com.github.retrooper.packetevents.manager.registry.ItemRegistry;
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
//import io.github.retrooper.packetevents.FabricItemType;
//import net.minecraft.item.Item;
//import net.minecraft.registry.Registries;
//import net.minecraft.util.Identifier;
import io.github.retrooper.packetevents.FabricItemType;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class Fabric1140ItemRegistry implements ItemRegistry {

    @Override
    public @Nullable ItemType getByName(String name) {
        Optional<Item> item = Registry.ITEM.getOrEmpty(Identifier.createSplit(name, ':'));
        return item.isPresent() ? new FabricItemType(item.get()) : null;
    }

    @Override
    public @Nullable ItemType getById(int id) {
        return null;
    }
}
