package io.github.retrooper.packetevents.injector;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.PlayerEjectEvent;
import io.github.retrooper.packetevents.event.impl.PlayerInjectEvent;
import io.github.retrooper.packetevents.injector.earlyinjector.EarlyInjector;
import io.github.retrooper.packetevents.injector.earlyinjector.legacy.EarlyChannelInjectorLegacy;
import io.github.retrooper.packetevents.injector.earlyinjector.modern.EarlyChannelInjector;
import io.github.retrooper.packetevents.injector.lateinjector.legacy.LateChannelInjectorLegacy;
import io.github.retrooper.packetevents.injector.lateinjector.modern.LateChannelInjector;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import org.bukkit.entity.Player;

import java.util.NoSuchElementException;

public class GlobalChannelInjector implements ChannelInjector {
    private ChannelInjector injector;

    public GlobalChannelInjector() {

    }

    public void load() {
        boolean legacy = NMSUtils.legacyNettyImportMode;
        if (!PacketEvents.get().getSettings().shouldUseCompatibilityInjector()) {
            injector = legacy ? new EarlyChannelInjectorLegacy() : new EarlyChannelInjector();
        } else {
            injector = legacy ? new LateChannelInjectorLegacy() : new LateChannelInjector();
        }
    }

    @Override
    public void inject() {
        try {
            //Try inject...
            injector.inject();
        } catch (Exception ex) {
            //Failed to inject! Let us revert to the compatibility injector and re-inject.
            if (injector instanceof EarlyInjector) {
                PacketEvents.get().getSettings().compatInjector(true);
                load();
                injector.inject();
                throw new IllegalStateException("PacketEvents failed to inject with the Early Injector. Reverting to the Compatibility/Late Injector... Please report this!", ex);
            }
        }
    }

    @Override
    public void eject() {
        injector.eject();
    }

    @Override
    public void injectPlayer(Player player) {
        PlayerInjectEvent injectEvent = new PlayerInjectEvent(player);
        PacketEvents.get().callEvent(injectEvent);
        if (!injectEvent.isCancelled()) {
            injector.injectPlayer(player);
        }
    }

    @Override
    public void ejectPlayer(Player player) {
        PlayerEjectEvent ejectEvent = new PlayerEjectEvent(player);
        PacketEvents.get().callEvent(ejectEvent);
        if (!ejectEvent.isCancelled()) {
            try {
                injector.ejectPlayer(player);
            } catch (NoSuchElementException ignored) {
                //We have already ejected them.
            }
        }
    }

    @Override
    public boolean hasInjected(Player player) {
        return injector.hasInjected(player);
    }

    @Override
    public void sendPacket(Object ch, Object rawNMSPacket) {
        injector.sendPacket(ch, rawNMSPacket);
    }
}
