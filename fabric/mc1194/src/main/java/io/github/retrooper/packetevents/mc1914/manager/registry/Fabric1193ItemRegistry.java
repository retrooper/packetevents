package io.github.retrooper.packetevents.mc1914.manager.registry;

import com.github.retrooper.packetevents.manager.registry.ItemRegistry;
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import io.github.retrooper.packetevents.FabricItemType;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class Fabric1193ItemRegistry implements ItemRegistry {

    @Override
    public @Nullable ItemType getByName(String name) {
        Optional<Item> item = Registries.ITEM.getOrEmpty(Identifier.splitOn(name, ':'));
        return item.isPresent() ? new FabricItemType(item.get()) : null;
    }

    @Override
    public @Nullable ItemType getById(int id) {
        return null;
    }
}
