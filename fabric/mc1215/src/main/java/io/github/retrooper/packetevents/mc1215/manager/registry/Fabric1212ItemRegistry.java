package io.github.retrooper.packetevents.mc1215.manager.registry;

import com.github.retrooper.packetevents.manager.registry.ItemRegistry;
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import io.github.retrooper.packetevents.FabricItemType;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class Fabric1212ItemRegistry implements ItemRegistry {

    @Override
    public @NotNull ItemType getByName(String name) {
        Item item = Registries.ITEM.get(Identifier.tryParse(name)); // returns default entry if item doesn't exist
        return new FabricItemType(item);
    }
}