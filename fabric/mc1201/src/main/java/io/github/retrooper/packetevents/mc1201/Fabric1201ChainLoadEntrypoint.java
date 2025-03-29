package io.github.retrooper.packetevents.mc1201;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import io.github.retrooper.packetevents.manager.registry.FabricRegistryManager;
import io.github.retrooper.packetevents.mc1201.registry.Fabric1201ItemRegistry;
import io.github.retrooper.packetevents.util.LazyHolder;
import io.github.retrooper.packetevents.impl.netty.manager.player.PlayerManagerAbstract;
import io.github.retrooper.packetevents.loader.ChainLoadData;
import io.github.retrooper.packetevents.loader.ChainLoadEntryPoint;
import io.github.retrooper.packetevents.mc1201.factory.fabric.Fabric1201ServerPlayerManager;

public class Fabric1201ChainLoadEntrypoint implements ChainLoadEntryPoint {

    protected LazyHolder<PlayerManagerAbstract> playerManagerAbstractLazyHolder = LazyHolder.simple(Fabric1201ServerPlayerManager::new);

    @Override
    public void initialize(ChainLoadData chainLoadData) {
        chainLoadData.setPlayerManagerIfNull(playerManagerAbstractLazyHolder);
        // Set default registry manager if not already set by any entrypoint
        chainLoadData.setRegistryManagerIfNull(LazyHolder.simple(() -> new FabricRegistryManager(
                new Fabric1201ItemRegistry()
        )));
    }

    @Override
    public ServerVersion getNativeVersion() {
        return ServerVersion.V_1_20_1;
    }
}
