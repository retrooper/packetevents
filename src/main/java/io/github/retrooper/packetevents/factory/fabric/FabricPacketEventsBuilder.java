package io.github.retrooper.packetevents.factory.fabric;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.injector.ChannelInjector;
import com.github.retrooper.packetevents.injector.InternalPacketListener;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import com.github.retrooper.packetevents.manager.server.ServerManager;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.netty.NettyManager;
import com.github.retrooper.packetevents.netty.channel.ChannelAbstract;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.ProtocolVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.settings.PacketEventsSettings;
import com.github.retrooper.packetevents.util.reflection.ReflectionObject;
import io.github.retrooper.packetevents.handler.PacketDecoder;
import io.github.retrooper.packetevents.handler.PacketEncoder;
import io.github.retrooper.packetevents.impl.manager.netty.NettyManagerImpl;
import io.github.retrooper.packetevents.impl.manager.player.PlayerManagerAbstract;
import io.github.retrooper.packetevents.impl.manager.protocol.ProtocolManagerAbstract;
import io.github.retrooper.packetevents.impl.manager.server.ServerManagerAbstract;
import io.netty.channel.Channel;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.Connection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FabricPacketEventsBuilder {
    private static PacketEventsAPI<Minecraft> INSTANCE;

    public static void clearBuildCache() {
        INSTANCE = null;
    }

    public static PacketEventsAPI<Minecraft> build(String modId) {
        if (INSTANCE == null) {
            INSTANCE = buildNoCache(modId);
        }
        return INSTANCE;
    }

    public static PacketEventsAPI<Minecraft> build(String modId, PacketEventsSettings settings) {
        if (INSTANCE == null) {
            INSTANCE = buildNoCache(modId, settings);
        }
        return INSTANCE;
    }


    public static PacketEventsAPI<Minecraft> buildNoCache(String modId) {
        return buildNoCache(modId, new PacketEventsSettings());
    }

    public static PacketEventsAPI<Minecraft> buildNoCache(String modId, PacketEventsSettings inSettings) {
        return new PacketEventsAPI<>() {
            private final PacketEventsSettings settings = inSettings;
            //TODO Implement platform version
            private final ProtocolManager protocolManager = new ProtocolManagerAbstract() {
                @Override
                public ProtocolVersion getPlatformVersion() {
                    return ProtocolVersion.UNKNOWN;
                }
            };
            private final ServerManager serverManager = new ServerManagerAbstract() {
                private static ServerVersion VERSION;

                @Override
                public ServerVersion getVersion() {
                    //TODO Not perfect, as this is on the client! Might be inaccurate by a few patch versions.
                    if (VERSION == null) {
                        int targetPV = SharedConstants.getProtocolVersion();
                        for (ServerVersion version : ServerVersion.reversedValues()) {
                            if (version.getProtocolVersion() == targetPV) {
                                VERSION = version;
                            }
                        }
                    }
                    return VERSION;
                }
            };

            private final PlayerManagerAbstract playerManager = new PlayerManagerAbstract() {
                @Override
                public int getPing(@NotNull Object player) {
                    return 0;
                }

                @Override
                public ChannelAbstract getChannel(@NotNull Object player) {
                    Connection connection = ((LocalPlayer) player).connection.getConnection();
                    ReflectionObject reflectConnection = new ReflectionObject(connection);
                    Channel channel = reflectConnection.readObject(0, Channel.class);
                    return PacketEvents.getAPI().getNettyManager().wrapChannel(channel);
                }
            };

            private final ChannelInjector injector = new ChannelInjector() {
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
            private final NettyManager nettyManager = new NettyManagerImpl();
            private boolean loaded;
            private boolean initialized;

            @Override
            public void load() {
                if (!loaded) {
                    final String id = modId.toLowerCase();
                    //Resolve server version and cache
                    PacketEvents.IDENTIFIER = "pe-" + id;
                    PacketEvents.ENCODER_NAME = "pe-encoder-" + id;
                    PacketEvents.DECODER_NAME = "pe-decoder-" + id;
                    PacketEvents.CONNECTION_NAME = "pe-connection-handler-" + id;
                    PacketEvents.SERVER_CHANNEL_HANDLER_NAME = "pe-connection-initializer-" + id;

                    injector.inject();

                    loaded = true;

                    //Register internal packet listener (should be the first listener)
                    //This listener doesn't do any modifications to the packets, just reads data
                    getEventManager().registerListener(new InternalPacketListener(),
                            PacketListenerPriority.LOWEST, true);
                }
            }

            @Override
            public boolean isLoaded() {
                return loaded;
            }

            @Override
            public void init() {
                //Load if we haven't loaded already
                load();
                if (!initialized) {
                    if (settings.shouldCheckForUpdates()) {
                        getUpdateChecker().handleUpdateCheck();
                    }

                    if (settings.isbStatsEnabled()) {
                        //TODO Cross-platform metrics?
                    }

                    PacketType.Play.Client.load();
                    PacketType.Play.Server.load();
                    initialized = true;
                }
            }

            @Override
            public boolean isInitialized() {
                return initialized;
            }

            @Override
            public void terminate() {
                if (initialized) {
                    //Eject the injector if needed(depends on the injector implementation)
                    injector.eject();
                    //Unregister all our listeners
                    getEventManager().unregisterAllListeners();
                    initialized = false;
                }
            }

            @Override
            public Minecraft getPlugin() {
                return Minecraft.getInstance();
            }

            @Override
            public ProtocolManager getProtocolManager() {
                return protocolManager;
            }

            @Override
            public ServerManager getServerManager() {
                return serverManager;
            }

            @Override
            public PlayerManager getPlayerManager() {
                return playerManager;
            }

            @Override
            public ChannelInjector getInjector() {
                return injector;
            }

            @Override
            public PacketEventsSettings getSettings() {
                return settings;
            }

            @Override
            public NettyManager getNettyManager() {
                return nettyManager;
            }
        };
    }
}
