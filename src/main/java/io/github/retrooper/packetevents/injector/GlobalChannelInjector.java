package io.github.retrooper.packetevents.injector;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.PlayerEjectEvent;
import io.github.retrooper.packetevents.event.impl.PlayerInjectEvent;
import io.github.retrooper.packetevents.injector.legacy.early.EarlyChannelInjectorLegacy;
import io.github.retrooper.packetevents.injector.modern.early.EarlyChannelInjectorModern;
import io.github.retrooper.packetevents.injector.legacy.late.LateChannelInjectorLegacy;
import io.github.retrooper.packetevents.injector.modern.late.LateChannelInjectorModern;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import org.bukkit.entity.Player;

public class GlobalChannelInjector implements ChannelInjector {
    private ChannelInjector injector;

    public GlobalChannelInjector() {

    }

    public void load() {
        boolean legacy = NMSUtils.legacyNettyImportMode;
        if (!PacketEvents.get().getSettings().shouldUseCompatibilityInjector()) {
            injector = legacy ? new EarlyChannelInjectorLegacy() : new EarlyChannelInjectorModern();
        } else {
            injector = legacy ? new LateChannelInjectorLegacy() : new LateChannelInjectorModern();
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
                PacketEvents.get().getPlugin().getLogger().warning("PacketEvents failed to inject with the Early Injector. Reverting to the Compatibility/Late Injector... This is just a warning, but please report this!");
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
            injector.ejectPlayer(player);
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
