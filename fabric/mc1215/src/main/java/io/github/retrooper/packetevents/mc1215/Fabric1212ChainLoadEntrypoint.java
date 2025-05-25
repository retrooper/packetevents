package io.github.retrooper.packetevents.mc1215;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import io.github.retrooper.packetevents.util.LazyHolder;
import io.github.retrooper.packetevents.loader.ChainLoadData;
import io.github.retrooper.packetevents.loader.ChainLoadEntryPoint;
import io.github.retrooper.packetevents.manager.registry.FabricRegistryManager;
import io.github.retrooper.packetevents.mc1215.manager.registry.Fabric1212ItemRegistry;

public class Fabric1212ChainLoadEntrypoint implements ChainLoadEntryPoint {

    @Override
    public void initialize(ChainLoadData chainLoadData) {
        chainLoadData.setRegistryManagerIfNull(LazyHolder.simple(() ->
                new FabricRegistryManager(new Fabric1212ItemRegistry())
        ));
    }

    @Override
    public ServerVersion getNativeVersion() {
        return ServerVersion.V_1_21_5;
    }
}
