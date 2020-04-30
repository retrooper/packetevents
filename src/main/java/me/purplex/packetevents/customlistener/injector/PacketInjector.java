package me.purplex.packetevents.customlistener.injector;


import io.netty.channel.*;
import me.purplex.packetevents.PacketEvents;
import me.purplex.packetevents.enums.ServerVersion;
import me.purplex.packetevents.events.packetevent.*;
import org.bukkit.entity.Player;


public class PacketInjector {
    private final static ServerVersion version = ServerVersion.getVersion();

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
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove(player.getName());
            return null;
        });
    }

    public Channel getChannel(Player player) {
        Channel channel = null;
        if (version == ServerVersion.v_1_8) {
            net.minecraft.server.v1_8_R1.NetworkManager networkManager = ((org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer) player).getHandle().playerConnection.networkManager;
            try {
                channel = (Channel) networkManager.getClass().getField("i").get(networkManager);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        } else if (version == ServerVersion.v_1_8_3) {
            net.minecraft.server.v1_8_R2.NetworkManager networkManager = ((org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer) player).getHandle().playerConnection.networkManager;
            channel = networkManager.k;
        } else if (version == ServerVersion.v_1_8_8) {
            net.minecraft.server.v1_8_R3.NetworkManager networkManager = ((org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer) player).getHandle().playerConnection.networkManager;
            channel = networkManager.channel;
        } else if (version == ServerVersion.v_1_9) {
            net.minecraft.server.v1_9_R1.NetworkManager networkManager = ((org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer) player).getHandle().playerConnection.networkManager;
            channel = networkManager.channel;
        } else if (version == ServerVersion.v_1_9_4) {
            net.minecraft.server.v1_9_R2.NetworkManager networkManager = ((org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer) player).getHandle().playerConnection.networkManager;
            channel = networkManager.channel;
        } else if (version == ServerVersion.v_1_10) {
            net.minecraft.server.v1_10_R1.NetworkManager networkManager = ((org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer) player).getHandle().playerConnection.networkManager;
            channel = networkManager.channel;
        } else if (version == ServerVersion.v_1_11) {
            net.minecraft.server.v1_11_R1.NetworkManager networkManager = ((org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer) player).getHandle().playerConnection.networkManager;
            channel = networkManager.channel;
        } else if (version == ServerVersion.v_1_12) {
            net.minecraft.server.v1_12_R1.NetworkManager networkManager = ((org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer) player).getHandle().playerConnection.networkManager;
            channel = networkManager.channel;
        } else if (version == ServerVersion.v_1_13) {
            net.minecraft.server.v1_13_R1.NetworkManager networkManager = ((org.bukkit.craftbukkit.v1_13_R1.entity.CraftPlayer) player).getHandle().playerConnection.networkManager;
            channel = networkManager.channel;
        } else if (version == ServerVersion.v_1_13_2) {
            net.minecraft.server.v1_13_R2.NetworkManager networkManager = ((org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer) player).getHandle().playerConnection.networkManager;
            channel = networkManager.channel;
        } else if (version == ServerVersion.v_1_14) {
            net.minecraft.server.v1_14_R1.NetworkManager networkManager = ((org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer) player).getHandle().playerConnection.networkManager;
            channel = networkManager.channel;
        } else if (version == ServerVersion.v_1_15) {
            net.minecraft.server.v1_15_R1.NetworkManager networkManager = ((org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer) player).getHandle().playerConnection.networkManager;
            channel = networkManager.channel;
        } else {
            System.err.println("Version unsupported, please contact purplex#0001 and tell him what version your server is running on! Make sure you are using paperspigot or spigot.");
        }
        return channel;
    }
}
