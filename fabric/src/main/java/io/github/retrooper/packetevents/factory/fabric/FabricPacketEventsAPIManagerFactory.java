package io.github.retrooper.packetevents.factory.fabric;

import com.github.retrooper.packetevents.manager.registry.RegistryManager;
import io.github.retrooper.packetevents.manager.AbstractFabricPlayerManager;
import io.github.retrooper.packetevents.util.LazyHolder;
import io.github.retrooper.packetevents.impl.netty.manager.player.PlayerManagerAbstract;
import io.github.retrooper.packetevents.loader.ChainLoadData;

public class FabricPacketEventsAPIManagerFactory {
    // TODO, refactor if booky and retrooper approve, bad design having settable static field
    // exists to maintain 100% backward compatability
    private static LazyHolder<AbstractFabricPlayerManager> lazyPlayerManagerHolder = () -> null;
    private static LazyHolder<AbstractFabricPlayerManager> lazyClientPlayerManagerHolder = () -> null;
    private static LazyHolder<RegistryManager> registryManagerLazyHolder = () -> null;

    public static LazyHolder<AbstractFabricPlayerManager> getLazyPlayerManagerHolder() {
        return lazyPlayerManagerHolder;
    }

    public static LazyHolder<AbstractFabricPlayerManager> getClientLazyPlayerManagerHolder() {
        return lazyClientPlayerManagerHolder;
    }

    public static LazyHolder<RegistryManager> getLazyRegistryManagerHolder() {
        return registryManagerLazyHolder;
    }

    public static void init(ChainLoadData chainLoadData) {
        FabricPacketEventsAPIManagerFactory.lazyPlayerManagerHolder = chainLoadData.getPlayerManagerAbstractLazyHolder();
        FabricPacketEventsAPIManagerFactory.lazyClientPlayerManagerHolder = chainLoadData.getClientPlayerManagerAbstractLazyHolder();
        FabricPacketEventsAPIManagerFactory.registryManagerLazyHolder = chainLoadData.getRegistryManagerLazyHolder();
    }
}
