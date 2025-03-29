package io.github.retrooper.packetevents.loader;

import com.github.retrooper.packetevents.manager.registry.RegistryManager;
import io.github.retrooper.packetevents.util.LazyHolder;
import io.github.retrooper.packetevents.impl.netty.manager.player.PlayerManagerAbstract;

public class ChainLoadData {

    private LazyHolder<RegistryManager> registryManagerLazyHolder = null;
    private LazyHolder<PlayerManagerAbstract> playerManagerAbstractLazyHolder = null;
    private LazyHolder<PlayerManagerAbstract> clientPlayerManagerAbstractLazyHolder = null;

    public void setRegistryManagerIfNull(LazyHolder<RegistryManager> registryManagerLazyHolder) {
        if (this.registryManagerLazyHolder == null) {
            this.registryManagerLazyHolder = registryManagerLazyHolder;
        }
    }

    public LazyHolder<RegistryManager> getRegistryManagerLazyHolder() {
        return this.registryManagerLazyHolder;
    }

    public void setPlayerManagerIfNull(LazyHolder<PlayerManagerAbstract> playerManagerAbstractLazyHolder) {
        if (this.playerManagerAbstractLazyHolder == null) {
            this.playerManagerAbstractLazyHolder = playerManagerAbstractLazyHolder;
        }
    }

    public LazyHolder<PlayerManagerAbstract> getPlayerManagerAbstractLazyHolder() {
        return this.playerManagerAbstractLazyHolder;
    }

    public void setClientPlayerManagerIfNull(LazyHolder<PlayerManagerAbstract> clientPlayerManagerAbstractLazyHolder) {
        if (this.clientPlayerManagerAbstractLazyHolder == null) {
            this.clientPlayerManagerAbstractLazyHolder = clientPlayerManagerAbstractLazyHolder;
        }
    }

    public LazyHolder<PlayerManagerAbstract> getClientPlayerManagerAbstractLazyHolder() {
        return this.clientPlayerManagerAbstractLazyHolder;
    }
}
