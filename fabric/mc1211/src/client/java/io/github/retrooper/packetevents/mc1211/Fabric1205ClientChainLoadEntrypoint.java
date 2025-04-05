package io.github.retrooper.packetevents.mc1211;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import io.github.retrooper.packetevents.factory.fabric.FabricPacketEventsAPI;
import io.github.retrooper.packetevents.loader.ChainLoadEntryPoint;
import io.github.retrooper.packetevents.util.LazyHolder;
import io.github.retrooper.packetevents.loader.ChainLoadData;
import io.github.retrooper.packetevents.mc1211.factory.fabric.Fabric1205ClientPlayerManager;

public class Fabric1205ClientChainLoadEntrypoint implements ChainLoadEntryPoint {

    @Override
    public void initialize(ChainLoadData chainLoadData) {
        chainLoadData.setClientPlayerManagerIfNull(LazyHolder.simple(() -> new Fabric1205ClientPlayerManager(FabricPacketEventsAPI.getClientAPI())));
    }

    @Override
    public ServerVersion getNativeVersion() {
        return ServerVersion.V_1_21_1;
    }
}
