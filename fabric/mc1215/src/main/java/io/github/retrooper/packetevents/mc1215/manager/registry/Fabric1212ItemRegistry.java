package io.github.retrooper.packetevents.mc1215.manager.registry;

import com.github.retrooper.packetevents.manager.registry.ItemRegistry;
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import io.github.retrooper.packetevents.FabricItemType;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class Fabric1212ItemRegistry implements ItemRegistry {

    @Override
    public @Nullable ItemType getByName(String name) {
        Optional<RegistryEntry.Reference<Item>> item = Registries.ITEM.getEntry(Identifier.tryParse(name)); // returns default entry if item doesn't exist
        return item.map(itemReference -> new FabricItemType(itemReference.value())).orElse(null);
    }

    @Override
    public @Nullable ItemType getById(int id) {
        Optional<RegistryEntry.Reference<Item>> item = Registries.ITEM.getEntry(id); // returns default entry if item doesn't exist
        return item.map(itemReference -> new FabricItemType(itemReference.value())).orElse(null);
    }
}