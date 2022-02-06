package io.github.retrooper.packetevents;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.injector.ChannelInjector;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.netty.channel.ChannelAbstract;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.reflection.ReflectionObject;
import com.github.retrooper.packetevents.wrapper.handshaking.client.WrapperHandshakingClientHandshake;
import io.github.retrooper.packetevents.factory.netty.BuildData;
import io.github.retrooper.packetevents.factory.netty.NettyPacketEventsBuilder;
import io.github.retrooper.packetevents.handler.PacketDecoder;
import io.github.retrooper.packetevents.handler.PacketEncoder;
import io.github.retrooper.packetevents.manager.player.PlayerManagerImpl;
import io.github.retrooper.packetevents.manager.server.ServerManagerImpl;
import io.netty.channel.Channel;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.util.ActionResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleMod implements ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("modid");

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        //TODO Mod idea, every 30 seconds or so, on a new port, establish a new connection to the server, get a server list response and
        //that is your ping
        BuildData data = new BuildData("fabric");
        ChannelInjector injector = new ChannelInjector() {
            @Override
            public @Nullable ConnectionState getConnectionState(ChannelAbstract ch) {
                Channel channel = (Channel) ch.rawChannel();
                PacketDecoder decoder = (PacketDecoder) channel.pipeline().get(PacketEvents.DECODER_NAME);
                return decoder.user.getConnectionState();
            }

            @Override
            public void changeConnectionState(ChannelAbstract ch, @Nullable ConnectionState packetState) {
                Channel channel = (Channel) ch.rawChannel();
                PacketDecoder decoder = (PacketDecoder) channel.pipeline().get(PacketEvents.DECODER_NAME);
                decoder.user.setConnectionState(packetState);
            }

            @Override
            public void inject() {

            }

            @Override
            public void eject() {

            }

            @Override
            public void injectPlayer(Object player, @Nullable ConnectionState connectionState) {

            }

            @Override
            public void updateUser(ChannelAbstract ch, User user) {
                Channel channel = (Channel) ch.rawChannel();
                PacketDecoder decoder = (PacketDecoder) channel.pipeline().get(PacketEvents.DECODER_NAME);
                decoder.user = user;
                PacketEncoder encoder = (PacketEncoder) channel.pipeline().get(PacketEvents.ENCODER_NAME);
                encoder.user = user;
            }

            @Override
            public void ejectPlayer(Object player) {

            }

            @Override
            public boolean hasInjected(Object player) {
                return false;
            }
        };

        ServerManagerImpl serverManager = new ServerManagerImpl() {
            @Override
            public ServerVersion getVersion() {
                return ServerVersion.getLatest();
            }
        };

        PlayerManagerImpl playerManager = new PlayerManagerImpl() {
            @Override
            public int getPing(@NotNull Object player) {
                return 0;
            }

            @Override
            public ChannelAbstract getChannel(@NotNull Object player) {
                ClientConnection connection = ((ClientPlayerEntity) player).networkHandler.getConnection();
                ReflectionObject reflectConnection = new ReflectionObject(connection);
                Channel channel = reflectConnection.readObject(0, Channel.class);
                return PacketEvents.getAPI().getNettyManager().wrapChannel(channel);
            }
        };

        PacketEvents.setAPI(NettyPacketEventsBuilder.build(data, injector,
                serverManager, playerManager));

        PacketEvents.getAPI().getSettings().debug(true).bStats(true);
        PacketEvents.getAPI().load();
        PacketEvents.getAPI().getEventManager().registerListener(new PacketListenerAbstract() {
            @Override
            public void onPacketReceive(PacketReceiveEvent event) {
                if (event.getPacketType() == PacketType.Handshaking.Client.HANDSHAKE) {
                    WrapperHandshakingClientHandshake handshake = new WrapperHandshakingClientHandshake(event);
                    ClientVersion version = handshake.getClientVersion();
                    String address = handshake.getServerAddress();
                    int port = handshake.getServerPort();
                    System.out.println("Sent handshaking packet: " + version + " " + address + ":" + port);
                }
                System.out.println("Packet type: " + event.getPacketType().getName());
            }

            @Override
            public void onPacketSend(PacketSendEvent event) {
                super.onPacketSend(event);
            }
        });
        PacketEvents.getAPI().init();


        LOGGER.info("Hello Fabric world!");
        AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) ->
        {
            LOGGER.info("ez");
            return ActionResult.PASS;
        });
    }
}
