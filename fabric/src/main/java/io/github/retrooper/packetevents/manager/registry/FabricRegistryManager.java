package io.github.retrooper.packetevents.manager.registry;

import com.github.retrooper.packetevents.manager.registry.ItemRegistry;
import com.github.retrooper.packetevents.manager.registry.RegistryManager;

public class FabricRegistryManager implements RegistryManager {

    private final ItemRegistry fabricItemRegistry;

    public FabricRegistryManager(
            ItemRegistry fabricItemRegistry
    ) {
        this.fabricItemRegistry = fabricItemRegistry;
    }

    @Override
    public ItemRegistry getItemRegistry() {
        return fabricItemRegistry;
    }
}
