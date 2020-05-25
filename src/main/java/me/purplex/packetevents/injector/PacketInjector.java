package me.purplex.packetevents.injector;


import io.netty.channel.*;
import me.purplex.packetevents.PacketEvents;
import me.purplex.packetevents.enums.ServerVersion;
import me.purplex.packetevents.injector.channels._1_10.Channel_1_10;
import me.purplex.packetevents.injector.channels._1_11.Channel_1_11;
import me.purplex.packetevents.injector.channels._1_12.Channel_1_12;
import me.purplex.packetevents.injector.channels._1_13.Channel_1_13;
import me.purplex.packetevents.injector.channels._1_13_2.Channel_1_13_2;
import me.purplex.packetevents.injector.channels._1_14.Channel_1_14;
import me.purplex.packetevents.injector.channels._1_15.Channel_1_15;
import me.purplex.packetevents.injector.channels._1_7_10.Channel_1_7_10;
import me.purplex.packetevents.injector.channels._1_8.Channel_1_8;
import me.purplex.packetevents.injector.channels._1_8_3.Channel_1_8_3;
import me.purplex.packetevents.injector.channels._1_8_8.Channel_1_8_8;
import me.purplex.packetevents.injector.channels._1_9.Channel_1_9;
import me.purplex.packetevents.injector.channels._1_9_4.Channel_1_9_4;
import me.purplex.packetevents.packetevent.impl.PacketReceiveEvent;
import me.purplex.packetevents.packetevent.impl.PacketSendEvent;
import org.bukkit.entity.Player;


public class PacketInjector {
    private final static ServerVersion version;

    static {
        version = ServerVersion.getVersion();
    }

    public void injectPlayer(Player player) {
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object packet) throws Exception {
                String packetName = packet.getClass().getSimpleName();
                PacketReceiveEvent e = new PacketReceiveEvent(player, packetName, packet);
                PacketEvents.getPacketManager().callEvent(e);
                if (e.isCancelled()) {
                    return;
                }
                super.channelRead(ctx, packet);

            }

            @Override
            public void write(ChannelHandlerContext ctx, Object packet, ChannelPromise promise) throws Exception {
                String packetName = packet.getClass().getSimpleName();
                PacketSendEvent e = new PacketSendEvent(player, packetName, packet);
                PacketEvents.getPacketManager().callEvent(e);
                if (e.isCancelled()) {
                    return;
                }
                super.write(ctx, packet, promise);
            }
        };
        Channel channel = getChannel(player);
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addBefore("packet_handler", player.getName(), channelDuplexHandler);
    }

    public void uninjectPlayer(Player player) {
        Channel channel = getChannel(player);
        channel.eventLoop().submit(new Runnable() {
            @Override
            public void run() {
                channel.pipeline().remove(player.getName());
            }
        });
    }

    public Channel getChannel(Player player) {
        Channel channel = null;
        if (version == ServerVersion.v_1_7_10) {
            channel = new Channel_1_7_10().getChannel(player);
        } else if (version == ServerVersion.v_1_8) {
            channel = new Channel_1_8().getChannel(player);
        } else if (version == ServerVersion.v_1_8_3) {
            channel = new Channel_1_8_3().getChannel(player);
        } else if (version == ServerVersion.v_1_8_8) {
            channel = new Channel_1_8_8().getChannel(player);
        } else if (version == ServerVersion.v_1_9) {
            channel = new Channel_1_9().getChannel(player);
        } else if (version == ServerVersion.v_1_9_4) {
            channel = new Channel_1_9_4().getChannel(player);
        } else if (version == ServerVersion.v_1_10) {
            channel = new Channel_1_10().getChannel(player);
        } else if (version == ServerVersion.v_1_11) {
            channel = new Channel_1_11().getChannel(player);
        } else if (version == ServerVersion.v_1_12) {
            channel = new Channel_1_12().getChannel(player);
        } else if (version == ServerVersion.v_1_13) {
            channel = new Channel_1_13().getChannel(player);
        } else if (version == ServerVersion.v_1_13_2) {
            channel = new Channel_1_13_2().getChannel(player);
        } else if (version == ServerVersion.v_1_14) {
            channel = new Channel_1_14().getChannel(player);
        } else if (version == ServerVersion.v_1_15) {
            channel = new Channel_1_15().getChannel(player);
        } else {
            String err = "Version unsupported, please contact purplex(creator) through his discord server (http://discord.gg/2uZY5A4) and tell him what version your server is running on! Make sure you are using spigot!";
            throw new IllegalStateException(err);
        }
        return channel;
    }
}
