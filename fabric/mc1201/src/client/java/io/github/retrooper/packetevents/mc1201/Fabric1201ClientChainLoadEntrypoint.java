package io.github.retrooper.packetevents.mc1201;

import io.github.retrooper.packetevents.factory.fabric.FabricPacketEventsAPI;
import io.github.retrooper.packetevents.util.LazyHolder;
import io.github.retrooper.packetevents.loader.ChainLoadData;
import io.github.retrooper.packetevents.mc1201.factory.fabric.Fabric1201ClientPlayerManager;

public class Fabric1201ClientChainLoadEntrypoint extends Fabric1201ChainLoadEntrypoint {

    @Override
    public void initialize(ChainLoadData chainLoadData) {
        chainLoadData.setClientPlayerManagerIfNull(LazyHolder.simple(() -> new Fabric1201ClientPlayerManager(FabricPacketEventsAPI.getClientAPI())));
    }
}
