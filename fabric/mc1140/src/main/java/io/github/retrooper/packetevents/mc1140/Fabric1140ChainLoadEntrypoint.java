package io.github.retrooper.packetevents.mc1140;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import io.github.retrooper.packetevents.factory.fabric.FabricPacketEventsAPI;
import io.github.retrooper.packetevents.manager.AbstractFabricPlayerManager;
import io.github.retrooper.packetevents.manager.registry.FabricRegistryManager;
import io.github.retrooper.packetevents.mc1140.manager.registry.Fabric1140ItemRegistry;
import io.github.retrooper.packetevents.util.LazyHolder;
import io.github.retrooper.packetevents.loader.ChainLoadData;
import io.github.retrooper.packetevents.loader.ChainLoadEntryPoint;
import io.github.retrooper.packetevents.mc1140.factory.fabric.Fabric1140ServerPlayerManager;

public class Fabric1140ChainLoadEntrypoint implements ChainLoadEntryPoint {

    protected LazyHolder<AbstractFabricPlayerManager> playerManagerAbstractLazyHolder = LazyHolder.simple(() -> new Fabric1140ServerPlayerManager(FabricPacketEventsAPI.getServerAPI()));

    @Override
    public void initialize(ChainLoadData chainLoadData) {
        chainLoadData.setPlayerManagerIfNull(playerManagerAbstractLazyHolder);
        // Set default registry manager if not already set by any entrypoint
        chainLoadData.setRegistryManagerIfNull(LazyHolder.simple(() -> new FabricRegistryManager(
                new Fabric1140ItemRegistry()
        )));
    }

    @Override
    public ServerVersion getNativeVersion() {
        return ServerVersion.V_1_14;
    }
}
