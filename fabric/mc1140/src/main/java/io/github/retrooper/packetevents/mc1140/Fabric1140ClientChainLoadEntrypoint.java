package io.github.retrooper.packetevents.mc1140;

import io.github.retrooper.packetevents.factory.fabric.FabricPacketEventsAPI;
import io.github.retrooper.packetevents.loader.ChainLoadData;
import io.github.retrooper.packetevents.mc1140.factory.fabric.Fabric1140ClientPlayerManager;
import io.github.retrooper.packetevents.util.LazyHolder;

public class Fabric1140ClientChainLoadEntrypoint extends Fabric1140ChainLoadEntrypoint {

    @Override
    public void initialize(ChainLoadData chainLoadData) {
        chainLoadData.setClientPlayerManagerIfNull(LazyHolder.simple(() -> new Fabric1140ClientPlayerManager(FabricPacketEventsAPI.getClientAPI())));
    }
}
