package io.github.retrooper.packetevents.mc1914;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import io.github.retrooper.packetevents.loader.ChainLoadData;
import io.github.retrooper.packetevents.loader.ChainLoadEntryPoint;
import io.github.retrooper.packetevents.manager.registry.FabricRegistryManager;
import io.github.retrooper.packetevents.mc1914.manager.registry.Fabric1193ItemRegistry;
import io.github.retrooper.packetevents.util.LazyHolder;

public class Fabric1913ChainLoadEntrypoint implements ChainLoadEntryPoint {

    @Override
    public void initialize(ChainLoadData chainLoadData) {
        chainLoadData.setRegistryManagerIfNull(LazyHolder.simple(() ->
                new FabricRegistryManager(new Fabric1193ItemRegistry())
        ));
    }

    @Override
    public ServerVersion getNativeVersion() {
        return ServerVersion.V_1_19_4;
    }
}
