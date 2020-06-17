package io.github.retrooper.packetevents.handler;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.annotations.Nullable;
import io.github.retrooper.packetevents.event.impl.PacketLoginEvent;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketSendEvent;
import io.github.retrooper.packetevents.packet.Packet;
import io.github.retrooper.packetevents.tinyprotocol.TinyProtocol7;
import net.minecraft.util.io.netty.channel.Channel;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public final class TinyProtocolHandler_1_7 {
    private final Plugin plugin;

    public TinyProtocolHandler_1_7(final Plugin plugin) {
        this.plugin = plugin;
    }

    @Nullable
    public TinyProtocol7 tinyProtocol;

    public void initTinyProtocol() {
        tinyProtocol = new TinyProtocol7(getPlugin()) {
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
                for(final String loginPacket : Packet.Login.LOGIN_PACKETS) {
                    if(packetName.equals(loginPacket)) {
                        PacketEvents.getEventManager().callEvent(new PacketLoginEvent(channel, packetName, packet));
                        return packet;
                    }
                }
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
