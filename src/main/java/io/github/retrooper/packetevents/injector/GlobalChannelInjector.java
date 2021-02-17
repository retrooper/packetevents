package io.github.retrooper.packetevents.injector;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.PlayerInjectEvent;
import io.github.retrooper.packetevents.injector.earlyinjector.legacy.EarlyChannelInjectorLegacy;
import io.github.retrooper.packetevents.injector.earlyinjector.modern.EarlyChannelInjector;
import io.github.retrooper.packetevents.injector.lateinjector.LateInjector;
import io.github.retrooper.packetevents.injector.lateinjector.legacy.LateChannelInjectorLegacy;
import io.github.retrooper.packetevents.injector.lateinjector.modern.LateChannelInjector;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import org.bukkit.entity.Player;

public class GlobalChannelInjector implements ChannelInjector {
    private final ChannelInjector injector;

    public GlobalChannelInjector() {
        boolean legacy = NMSUtils.legacyNettyImportMode;
        if (PacketEvents.get().getSettings().shouldUseCompatibilityInjector()) {
            injector = legacy ? new EarlyChannelInjectorLegacy() : new EarlyChannelInjector();
        } else {
            injector = legacy ? new LateChannelInjectorLegacy() : new LateChannelInjector();
        }
    }

    @Override
    public void inject() {
        //TODO support cancelling inject event with early injector.
        injector.inject();
    }

    @Override
    public void eject() {
        injector.eject();
    }

    @Override
    public void injectPlayer(Player player) {
        if (injector instanceof LateInjector) {
            PlayerInjectEvent injectEvent = new PlayerInjectEvent(player);
            PacketEvents.get().callEvent(injectEvent);
            if (!injectEvent.isCancelled()) {
                injector.injectPlayer(player);
            }
        } else {
            injector.injectPlayer(player);
        }
    }

    @Override
    public void ejectPlayer(Player player) {
        injector.ejectPlayer(player);
    }

    @Override
    public void sendPacket(Object ch, Object rawNMSPacket) {
        injector.sendPacket(ch, rawNMSPacket);
    }
}
