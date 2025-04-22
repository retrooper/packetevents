package io.github.retrooper.packetevents.mc1202;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import io.github.retrooper.packetevents.factory.fabric.FabricPacketEventsAPI;
import io.github.retrooper.packetevents.manager.AbstractFabricPlayerManager;
import io.github.retrooper.packetevents.util.LazyHolder;
import io.github.retrooper.packetevents.loader.ChainLoadData;
import io.github.retrooper.packetevents.loader.ChainLoadEntryPoint;
import io.github.retrooper.packetevents.mc1202.factory.fabric.Fabric1202ServerPlayerManager;

public class Fabric1202ChainLoadEntrypoint implements ChainLoadEntryPoint {

    protected LazyHolder<AbstractFabricPlayerManager> playerManagerAbstractLazyHolder = LazyHolder.simple(() -> new Fabric1202ServerPlayerManager(FabricPacketEventsAPI.getServerAPI()));

    @Override
    public void initialize(ChainLoadData chainLoadData) {
        chainLoadData.setPlayerManagerIfNull(playerManagerAbstractLazyHolder);
    }

    @Override
    public ServerVersion getNativeVersion() {
        return ServerVersion.V_1_20_2;
    }
}
