package io.github.retrooper.packetevents.mc1201;

import io.github.retrooper.packetevents.util.LazyHolder;
import io.github.retrooper.packetevents.loader.ChainLoadData;
import io.github.retrooper.packetevents.mc1201.factory.fabric.Fabric1201ClientPlayerManager;

public class Fabric1201ClientChainLoadEntrypoint extends Fabric1201ChainLoadEntrypoint {

    @Override
    public void initialize(ChainLoadData chainLoadData) {
        super.playerManagerAbstractLazyHolder = LazyHolder.simple(Fabric1201ClientPlayerManager::new);
        super.initialize(chainLoadData);
    }
}
