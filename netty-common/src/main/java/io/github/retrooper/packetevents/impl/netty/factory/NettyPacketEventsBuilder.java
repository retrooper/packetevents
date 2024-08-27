/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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

package io.github.retrooper.packetevents.impl.netty.factory;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.injector.ChannelInjector;
import com.github.retrooper.packetevents.manager.InternalPacketListener;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import com.github.retrooper.packetevents.manager.server.ServerManager;
import com.github.retrooper.packetevents.netty.NettyManager;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.settings.PacketEventsSettings;
import io.github.retrooper.packetevents.impl.netty.BuildData;
import io.github.retrooper.packetevents.impl.netty.NettyManagerImpl;
import io.github.retrooper.packetevents.impl.netty.manager.player.PlayerManagerAbstract;
import io.github.retrooper.packetevents.impl.netty.manager.server.ServerManagerAbstract;

public class NettyPacketEventsBuilder {
    private static PacketEventsAPI<BuildData> INSTANCE;

    public static void clearBuildCache() {
        INSTANCE = null;
    }

    public static PacketEventsAPI<BuildData> build(BuildData data, ChannelInjector injector,
                                                   ProtocolManager protocolManager,
                                                   ServerManagerAbstract serverManager,
                                                   PlayerManagerAbstract playerManager) {
        if (INSTANCE == null) {
            INSTANCE = buildNoCache(data, injector, protocolManager, serverManager, playerManager);
        }
        return INSTANCE;
    }

    public static PacketEventsAPI<BuildData> build(BuildData data, ChannelInjector injector,
                                                   ProtocolManager protocolManager,
                                                   ServerManagerAbstract serverManager,
                                                   PlayerManagerAbstract playerManager, PacketEventsSettings settings) {
        if (INSTANCE == null) {
            INSTANCE = buildNoCache(data, injector, protocolManager,
                    serverManager, playerManager, settings);
        }
        return INSTANCE;
    }


    public static PacketEventsAPI<BuildData> buildNoCache(BuildData data, ChannelInjector injector,
                                                          ProtocolManager protocolManager,
                                                          ServerManagerAbstract serverManager,
                                                          PlayerManagerAbstract playerManager) {
        return buildNoCache(data, injector, protocolManager,
                serverManager, playerManager, new PacketEventsSettings());
    }

    public static PacketEventsAPI<BuildData> buildNoCache(BuildData data, ChannelInjector injector,
                                                          ProtocolManager protocolManager,
                                                          ServerManagerAbstract serverManager,
                                                          PlayerManagerAbstract playerManager,
                                                          PacketEventsSettings inSettings) {
        return new PacketEventsAPI<BuildData>() {
            private final PacketEventsSettings settings = inSettings;
            private final NettyManager nettyManager = new NettyManagerImpl();
            private boolean loaded;
            private boolean initialized;
            private boolean terminated;

            @Override
            public void load() {
                if (!loaded) {
                    //Resolve server version and cache
                    PacketEvents.IDENTIFIER = "pe-" + data.getName().toLowerCase();
                    PacketEvents.ENCODER_NAME = "pe-encoder-" + data.getName().toLowerCase();
                    PacketEvents.DECODER_NAME = "pe-decoder-" + data.getName().toLowerCase();
                    PacketEvents.CONNECTION_HANDLER_NAME = "pe-connection-handler-" + data.getName().toLowerCase();
                    PacketEvents.SERVER_CHANNEL_HANDLER_NAME = "pe-connection-initializer-" + data.getName().toLowerCase();
                    PacketEvents.TIMEOUT_HANDLER_NAME = "pe-timeout-handler-" + data.getName().toLowerCase();
                    injector.inject();

                    loaded = true;

                    //Register internal packet listener (should be the first listener)
                    //This listener doesn't do any modifications to the packets, just reads data
                    getEventManager().registerListener(new InternalPacketListener());
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

                    //TODO Cross-platform metrics?

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
                    //Uninject the injector if needed(depends on the injector implementation)
                    injector.uninject();
                    //Unregister all our listeners
                    getEventManager().unregisterAllListeners();
                    initialized = false;
                    terminated = true;
                }
            }

            @Override
            public boolean isTerminated() {
                return terminated;
            }

            @Override
            public BuildData getPlugin() {
                return data;
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
            public PacketEventsSettings getSettings() {
                return settings;
            }

            @Override
            public NettyManager getNettyManager() {
                return nettyManager;
            }

            @Override
            public ChannelInjector getInjector() {
                return injector;
            }
        };
    }
}
