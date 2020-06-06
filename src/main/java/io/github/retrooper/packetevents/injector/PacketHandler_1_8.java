package io.github.retrooper.packetevents.injector;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketSendEvent;
import io.github.retrooper.packetevents.tinyprotocol.TinyProtocol8;
import io.netty.channel.Channel;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

class PacketHandler_1_8 {
    private final Plugin plugin;

    public PacketHandler_1_8(final Plugin plugin) {
        this.plugin = plugin;
    }
    private TinyProtocol8 tinyProtocol;

    public void initTinyProtocol() {
        this.tinyProtocol = new TinyProtocol8(getPlugin()) {
            @Override
            public Object onPacketOutAsync(Player receiver, Channel channel, Object packet) {
                final String packetName = packet.getClass().getSimpleName();
                final PacketSendEvent packetSendEvent = new PacketSendEvent(receiver, packetName, packet);
                PacketEvents.getEventManager().callEvent(packetSendEvent);
                if (!packetSendEvent.isCancelled()) {
                    return packet;
                }
                return null;
            }

            @Override
            public Object onPacketInAsync(Player sender, Channel channel, Object packet) {
                final String packetName = packet.getClass().getSimpleName();
                final PacketReceiveEvent packetReceiveEvent = new PacketReceiveEvent(sender, packetName, packet);
                PacketEvents.getEventManager().callEvent(packetReceiveEvent);
                if (!packetReceiveEvent.isCancelled()) {
                    return packet;
                }
                return null;
            }
        };
    }

    public Plugin getPlugin() {
        return plugin;
    }
}
