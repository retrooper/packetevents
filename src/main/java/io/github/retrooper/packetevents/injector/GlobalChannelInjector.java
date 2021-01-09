package io.github.retrooper.packetevents.injector;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.injector.earlyinjector.EarlyChannelInjector7;
import io.github.retrooper.packetevents.injector.earlyinjector.EarlyChannelInjector8;
import io.github.retrooper.packetevents.injector.lateinjector.LateChannelInjector7;
import io.github.retrooper.packetevents.injector.lateinjector.LateChannelInjector8;
import io.github.retrooper.packetevents.injector.tinyprotocol.TinyProtocol;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import org.bukkit.entity.Player;

public class GlobalChannelInjector implements ChannelInjector {
    private ChannelInjector injector;
    private TinyProtocol tiny;

    public GlobalChannelInjector() {
        boolean legacy = NMSUtils.legacyNettyImportMode;
        //Early injector
        if (PacketEvents.get().getSettings().shouldInjectEarly()) {
            injector = legacy ? new EarlyChannelInjector7() : new EarlyChannelInjector8();
            tiny = new TinyProtocol(PacketEvents.get().getPlugin());
        }
        //Late injector
        else {
            injector = legacy ? new LateChannelInjector7() : new LateChannelInjector8();
        }
    }

    @Override
    public void prepare() {
        if (!PacketEvents.get().getSettings().shouldInjectEarly()) {
            injector.prepare();
        }
    }

    @Override
    public void cleanup() {
        if (PacketEvents.get().getSettings().shouldInjectEarly()) {
            tiny.unregisterChannelHandler();
        } else {
            injector.cleanup();
        }
    }

    @Override
    public void cleanupAsync() {
        injector.cleanupAsync();
    }

    @Override
    public void injectPlayerSync(Player player) {
        if (PacketEvents.get().getSettings().shouldInjectEarly()) {
            tiny.injectPlayer(player);
        } else {
            injector.injectPlayerSync(player);
        }
    }

    @Override
    public void ejectPlayerSync(Player player) {
        if (PacketEvents.get().getSettings().shouldInjectEarly()) {
            tiny.ejectPlayer(player);
        } else {
            injector.ejectPlayerSync(player);
        }
    }

    @Override
    public void injectPlayerAsync(Player player) {
        if (PacketEvents.get().getSettings().shouldInjectEarly()) {
            tiny.injectPlayerAsync(player);
        } else {
            injector.injectPlayerAsync(player);
        }
    }

    @Override
    public void ejectPlayerAsync(Player player) {
        if (PacketEvents.get().getSettings().shouldInjectEarly()) {
            tiny.ejectPlayerAsync(player);
        } else {
            injector.ejectPlayerAsync(player);
        }
    }

    @Override
    public void sendPacket(Object channel, Object rawNMSPacket) {
        if (PacketEvents.get().getSettings().shouldInjectEarly()) {
            tiny.sendPacket(channel, rawNMSPacket);
        } else {
            injector.sendPacket(channel, rawNMSPacket);
        }
    }
}
