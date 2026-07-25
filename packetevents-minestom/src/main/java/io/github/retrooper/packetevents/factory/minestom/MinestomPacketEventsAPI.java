/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2026 retrooper and contributors
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

package io.github.retrooper.packetevents.factory.minestom;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.injector.ChannelInjector;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import com.github.retrooper.packetevents.manager.server.ServerManager;
import com.github.retrooper.packetevents.netty.NettyManager;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.settings.PacketEventsSettings;
import com.github.retrooper.packetevents.util.PEVersions;
import io.github.retrooper.packetevents.impl.netty.NettyManagerImpl;
import net.minestom.server.MinecraftServer;

/**
 * {@link PacketEventsAPI} implementation for a Minestom server, modeled on
 * {@code FabricPacketEventsAPI}. {@code T} is {@link Object} here (Minestom has no
 * "plugin"/mod-loader analog to expose); {@link #getPlugin()} returns Minestom's
 * {@code ServerProcess} as the closest equivalent.
 */
public class MinestomPacketEventsAPI extends PacketEventsAPI<Object> {

    private final PacketEventsSettings settings;

    private final ProtocolManager protocolManager = new MinestomProtocolManager();
    private final ServerManager serverManager;
    private final MinestomPlayerManager playerManager = new MinestomPlayerManager();
    private final MinestomChannelInjector injector = new MinestomChannelInjector();
    private final NettyManager nettyManager = new NettyManagerImpl();

    private boolean loaded;
    private boolean initialized;
    private boolean terminated;

    public MinestomPacketEventsAPI(String mcVersion) {
        this(mcVersion, new PacketEventsSettings());
    }

    public MinestomPacketEventsAPI(String mcVersion, PacketEventsSettings settings) {
        this.settings = settings;
        this.serverManager = new MinestomServerManager(mcVersion);
    }

    @Override
    public void load() {
        if (this.loaded) {
            return;
        }

        // Harmless to set even though Minestom has no Netty pipeline to name handlers
        // in; kept for parity with other platforms in case shared code inspects them.
        PacketEvents.IDENTIFIER = "pe-minestom";
        PacketEvents.ENCODER_NAME = "pe-encoder-minestom";
        PacketEvents.DECODER_NAME = "pe-decoder-minestom";
        PacketEvents.CONNECTION_HANDLER_NAME = "pe-connection-handler-minestom";
        PacketEvents.SERVER_CHANNEL_HANDLER_NAME = "pe-connection-initializer-minestom";

        super.load();
        this.loaded = true;

        this.getLogManager().info("Loaded packetevents v" + PEVersions.RAW);
    }

    @Override
    public boolean isLoaded() {
        return this.loaded;
    }

    @Override
    public void init() {
        // load if we haven't loaded already
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
        return MinecraftServer.process();
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
    public PacketEventsSettings getSettings() {
        return this.settings;
    }

    @Override
    public NettyManager getNettyManager() {
        return this.nettyManager;
    }
}
