/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
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

package io.github.retrooper.packetevents.factory.nettystom;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.injector.ChannelInjector;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import com.github.retrooper.packetevents.manager.server.ServerManager;
import com.github.retrooper.packetevents.netty.NettyManager;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.settings.PacketEventsSettings;
import io.github.retrooper.packetevents.impl.netty.NettyManagerImpl;

import java.util.Locale;

/**
 * Standalone {@link PacketEventsAPI} implementation for a nettystom server.
 *
 * <p>nettystom does not have a plugin/mod container, so the generic plugin type is simply
 * an opaque identifier object supplied by the embedding server. The {@code LogManager} is
 * resolved automatically by the core (SLF4J/Adventure are present on a nettystom server).
 */
public class NettystomPacketEventsAPI extends PacketEventsAPI<Object> {

    private final Object plugin;
    private final String id;
    private final PacketEventsSettings settings;

    private final ProtocolManager protocolManager = new NettystomProtocolManager();
    private final ServerManager serverManager;
    private final PlayerManager playerManager = new NettystomPlayerManager();
    private final ChannelInjector injector = new NettystomChannelInjector();
    private final NettyManager nettyManager = new NettyManagerImpl();

    private boolean loaded;
    private boolean initialized;
    private boolean terminated;

    public NettystomPacketEventsAPI(Object plugin, String id, PacketEventsSettings settings,
                                    ServerManager serverManager) {
        this.plugin = plugin;
        this.id = id;
        this.settings = settings;
        this.serverManager = serverManager;
    }

    @Override
    public void load() {
        if (this.loaded) {
            return;
        }
        String identifier = this.id.toLowerCase(Locale.ROOT);
        PacketEvents.IDENTIFIER = "pe-" + identifier;
        PacketEvents.ENCODER_NAME = "pe-encoder-" + identifier;
        PacketEvents.DECODER_NAME = "pe-decoder-" + identifier;
        PacketEvents.CONNECTION_HANDLER_NAME = "pe-connection-handler-" + identifier;
        PacketEvents.SERVER_CHANNEL_HANDLER_NAME = "pe-connection-initializer-" + identifier;
        PacketEvents.TIMEOUT_HANDLER_NAME = "pe-timeout-handler-" + identifier;

        super.load();
        this.loaded = true;
    }

    @Override
    public boolean isLoaded() {
        return this.loaded;
    }

    @Override
    public void init() {
        this.load();
        if (this.initialized) {
            return;
        }
        if (this.settings.shouldCheckForUpdates()) {
            this.getUpdateChecker().handleUpdateCheck();
        }

        PacketType.Play.Client.load();
        PacketType.Play.Server.load();
        this.initialized = true;
    }

    @Override
    public boolean isInitialized() {
        return this.initialized;
    }

    @Override
    public void terminate() {
        if (!this.initialized) {
            return;
        }
        super.terminate();
        this.initialized = false;
        this.terminated = true;
    }

    @Override
    public boolean isTerminated() {
        return this.terminated;
    }

    @Override
    public Object getPlugin() {
        return this.plugin;
    }

    @Override
    public ProtocolManager getProtocolManager() {
        return this.protocolManager;
    }

    @Override
    public ServerManager getServerManager() {
        return this.serverManager;
    }

    @Override
    public PlayerManager getPlayerManager() {
        return this.playerManager;
    }

    @Override
    public ChannelInjector getInjector() {
        return this.injector;
    }

    @Override
    public NettyManager getNettyManager() {
        return this.nettyManager;
    }

    @Override
    public PacketEventsSettings getSettings() {
        return this.settings;
    }
}
