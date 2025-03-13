package io.github.retrooper.packetevents.mc1211;

import io.github.retrooper.packetevents.util.LazyHolder;
import io.github.retrooper.packetevents.loader.ChainLoadData;
import io.github.retrooper.packetevents.mc1211.factory.fabric.Fabric1211ClientPlayerManager;

public class Fabric1211ClientChainLoadEntrypoint extends Fabric1211ChainLoadEntrypoint {
    @Override
    public void initialize(ChainLoadData chainLoadData) {
        super.playerManagerAbstractLazyHolder = LazyHolder.simple(Fabric1211ClientPlayerManager::new);
        super.initialize(chainLoadData);
    }
}
