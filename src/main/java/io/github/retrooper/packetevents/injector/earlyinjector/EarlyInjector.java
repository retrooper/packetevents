package io.github.retrooper.packetevents.injector.earlyinjector;

import io.github.retrooper.packetevents.injector.ChannelInjector;

public interface EarlyInjector extends ChannelInjector {
    void prepare();

    void cleanup();

    Object injectChannel(Object channel);

    void ejectChannel(Object channel);
}
