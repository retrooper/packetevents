package io.github.retrooper.packetevents.mc1914;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import io.github.retrooper.packetevents.factory.fabric.FabricPacketEventsAPI;
import io.github.retrooper.packetevents.loader.ChainLoadData;
import io.github.retrooper.packetevents.loader.ChainLoadEntryPoint;
import io.github.retrooper.packetevents.manager.FabricServerManager;
import io.github.retrooper.packetevents.manager.registry.FabricRegistryManager;
import io.github.retrooper.packetevents.mc1140.manager.registry.Fabric1140ItemRegistry;
import io.github.retrooper.packetevents.mc1914.factory.fabric.Fabric1190ServerPlayerManager;
import io.github.retrooper.packetevents.mc1914.manager.registry.Fabric1193ItemRegistry;
import io.github.retrooper.packetevents.util.LazyHolder;

public class Fabric1913ChainLoadEntrypoint implements ChainLoadEntryPoint {

    @Override
    public void initialize(ChainLoadData chainLoadData) {
        chainLoadData.setPlayerManagerIfNull(LazyHolder.simple(() -> new Fabric1190ServerPlayerManager(FabricPacketEventsAPI.getServerAPI())));
        chainLoadData.setRegistryManagerIfNull(LazyHolder.simple(() ->
                new FabricRegistryManager(FabricServerManager.getVersionStatically().isNewerThan(ServerVersion.V_1_19_2) ? new Fabric1193ItemRegistry() : new Fabric1140ItemRegistry())
        ));
    }

    @Override
    public ServerVersion getNativeVersion() {
        return ServerVersion.V_1_19_4;
    }
}
