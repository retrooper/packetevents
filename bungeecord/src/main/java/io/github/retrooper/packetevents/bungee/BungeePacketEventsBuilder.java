/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.retrooper.packetevents.bungee;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.injector.ChannelInjector;
import com.github.retrooper.packetevents.injector.InternalPacketListener;
import com.github.retrooper.packetevents.manager.server.ServerManager;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.netty.NettyManager;
import com.github.retrooper.packetevents.netty.channel.ChannelAbstract;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.settings.PacketEventsSettings;
import io.github.retrooper.packetevents.manager.netty.NettyManagerImpl;
import io.github.retrooper.packetevents.manager.player.PlayerManagerImpl;
import io.github.retrooper.packetevents.manager.server.ServerManagerImpl;
import io.netty.channel.Channel;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BungeePacketEventsBuilder {
    /*
    private static PacketEventsAPI<Plugin> INSTANCE;

    public static void clearBuildCache() {
        INSTANCE = null;
    }

    public static PacketEventsAPI<Plugin> build(Plugin plugin) {
        if (INSTANCE == null) {
            INSTANCE = buildNoCache(plugin);
        }
        return INSTANCE;
    }

    public static PacketEventsAPI<Plugin> build(Plugin plugin, PacketEventsSettings settings) {
        if (INSTANCE == null) {
            INSTANCE = buildNoCache(plugin, settings);
        }
        return INSTANCE;
    }


    public static PacketEventsAPI<Plugin> buildNoCache(Plugin plugin) {
        return buildNoCache(plugin, new PacketEventsSettings());
    }


    public static PacketEventsAPI<Plugin> buildNoCache(Plugin plugin, PacketEventsSettings inSettings) {
        return new PacketEventsAPI<>() {
            private final PacketEventsSettings settings = inSettings;
            private final ServerManager serverManager = new ServerManagerImpl() {
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

            private final PlayerManagerImpl playerManager = new PlayerManagerImpl() {
                @Override
                public int getPing(@NotNull Object player) {
                    return 0;
                }

                @Override
                public ChannelAbstract getChannel(@NotNull Object player) {
                    //TODO ProxiedPlayer
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
                    final String id = "";
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
            public MinecraftClient getPlugin() {
                return MinecraftClient.getInstance();
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
    }*/
}
