package io.github.retrooper.packetevents.handler;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.enums.ServerVersion;
import io.github.retrooper.packetevents.event.impl.PacketLoginEvent;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketSendEvent;
import io.github.retrooper.packetevents.packet.Packet;
import io.github.retrooper.packetevents.tinyprotocol.TinyProtocol8;
import io.netty.channel.Channel;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PacketHandler_1_8 {
    private static final ServerVersion version = PacketEvents.getServerVersion();
    private final Plugin plugin;

    public PacketHandler_1_8(final Plugin plugin) {
        this.plugin = plugin;
    }


    /*
     * PacketHandshakingInSetProtocol
     * PacketLoginInStart
     * PacketLoginInEncryptionBegin
     */
    public void initTinyProtocol() {
        new TinyProtocol8(getPlugin()) {
            @Override
            public Object onPacketOutAsync(Player receiver, Channel channel, Object packet) {
                if(receiver == null) {
                    System.out.println("player is null n");
                    System.out.println("packetname is " + packet.getClass().getSimpleName());
                }
                if(channel == null){
                    System.out.println("CHANNEL IS NULL");
                }
                if(packet == null) {
                    System.out.println("PACKET IS NULL");
                }
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
                        PacketEvents.getEventManager().callEvent(new PacketLoginEvent(packetName, packet));
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

    public static String getNetworkManagersFieldName() {
        //1.8
        if(version.equals(ServerVersion.v_1_8)) {
            return "g";
        }
        //1.8.3->1.12
        else if(version.isLowerThan(ServerVersion.v_1_13)) {
            return "h";
        }
        //1.13->1.14
        else if(version.isLowerThan(ServerVersion.v_1_15)) {
            return "g";
        }
        //1.15
        else  {
            return "listeningChannels";
        }
    }

    public Plugin getPlugin() {
        return plugin;
    }
}
