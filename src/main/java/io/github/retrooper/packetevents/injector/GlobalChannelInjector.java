package io.github.retrooper.packetevents.injector;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.injector.earlyinjector.EarlyChannelInjector7;
import io.github.retrooper.packetevents.injector.earlyinjector.EarlyChannelInjector8;
import io.github.retrooper.packetevents.injector.lateinjector.LateChannelInjector7;
import io.github.retrooper.packetevents.injector.lateinjector.LateChannelInjector8;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import org.bukkit.entity.Player;

public class GlobalChannelInjector implements ChannelInjector {
    private final ChannelInjector injector;

    public GlobalChannelInjector() {
        boolean legacy = NMSUtils.legacyNettyImportMode;
        //Early injector
        if (PacketEvents.get().getSettings().shouldInjectEarly()) {
            injector = legacy ? new EarlyChannelInjector7() : new EarlyChannelInjector8();
        }
        //Late injector
        else {
            injector = legacy ? new LateChannelInjector7() : new LateChannelInjector8();
        }
    }

    @Override
    public void prepare() {
        injector.prepare();
    }

    @Override
    public void cleanup() {
        injector.cleanup();
    }

    @Override
    public void cleanupAsync() {
        injector.cleanupAsync();
    }

    @Override
    public void injectPlayerSync(Player player) {
        injector.injectPlayerSync(player);
    }

    @Override
    public void ejectPlayerSync(Player player) {
        injector.ejectPlayerSync(player);
    }

    @Override
    public void injectPlayerAsync(Player player) {
        injector.injectPlayerAsync(player);
    }

    @Override
    public void ejectPlayerAsync(Player player) {
        injector.ejectPlayerAsync(player);
    }

    @Override
    public void sendPacket(Object channel, Object rawNMSPacket) {
        injector.sendPacket(channel, rawNMSPacket);
    }
}
