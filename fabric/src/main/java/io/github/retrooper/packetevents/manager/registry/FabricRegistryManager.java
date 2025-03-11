package io.github.retrooper.packetevents.manager.registry;

import com.github.retrooper.packetevents.manager.registry.RegistryManager;

public class FabricRegistryManager implements RegistryManager {

    private final FabricItemRegistry fabricItemRegistry = new FabricItemRegistry();

    @Override
    public FabricItemRegistry getItemRegistry() {
        return fabricItemRegistry;
    }
}
