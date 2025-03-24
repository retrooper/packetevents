package io.github.retrooper.packetevents.mc1211;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import io.github.retrooper.packetevents.impl.netty.manager.player.PlayerManagerAbstract;
import io.github.retrooper.packetevents.loader.ChainLoadEntryPoint;
import io.github.retrooper.packetevents.util.LazyHolder;
import io.github.retrooper.packetevents.loader.ChainLoadData;
import io.github.retrooper.packetevents.mc1211.factory.fabric.Fabric1211ClientPlayerManager;

public class Fabric1211ClientChainLoadEntrypoint implements ChainLoadEntryPoint {

    private final LazyHolder<PlayerManagerAbstract> playerManagerAbstractLazyHolder = LazyHolder.simple(Fabric1211ClientPlayerManager::new);

    @Override
    public void initialize(ChainLoadData chainLoadData) {
        chainLoadData.setPlayerManagerIfNull(playerManagerAbstractLazyHolder);
    }

    @Override
    public ServerVersion getNativeVersion() {
        return ServerVersion.V_1_21_1;
    }
}
