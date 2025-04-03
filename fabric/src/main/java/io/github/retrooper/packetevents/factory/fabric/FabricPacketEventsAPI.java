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

package io.github.retrooper.packetevents.factory.fabric;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.injector.ChannelInjector;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import com.github.retrooper.packetevents.manager.registry.RegistryManager;
import com.github.retrooper.packetevents.manager.server.ServerManager;
import com.github.retrooper.packetevents.netty.NettyManager;
import com.github.retrooper.packetevents.protocol.PacketSide;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.settings.PacketEventsSettings;
import com.github.retrooper.packetevents.util.LogManager;
import io.github.retrooper.packetevents.PacketEventsMod;
import io.github.retrooper.packetevents.impl.netty.NettyManagerImpl;
import io.github.retrooper.packetevents.manager.FabricLogger;
import io.github.retrooper.packetevents.manager.FabricProtocolManager;
import io.github.retrooper.packetevents.manager.FabricServerManager;
import io.github.retrooper.packetevents.manager.InternalFabricPacketListener;
import io.github.retrooper.packetevents.util.viaversion.ViaVersionUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;

import java.util.Locale;
import java.util.logging.Logger;

public class FabricPacketEventsAPI extends PacketEventsAPI<ModInitializer> {
    private final String modId;
    private final EnvType environment;
    private final PacketEventsSettings settings;

    private final ProtocolManager protocolManager;
    private final ServerManager serverManager;
    private final ChannelInjector injector;
    private final NettyManager nettyManager = new NettyManagerImpl();
    private final LogManager logManager = new FabricLogger(Logger.getLogger("PacketEvents"));

    private boolean loaded;
    private boolean initialized;
    private boolean terminated;

    private static FabricPacketEventsAPI serverFabricAPI;
    private static FabricPacketEventsAPI clientFabricAPI;

    public FabricPacketEventsAPI(String modId, EnvType environment, PacketEventsSettings settings) {
        this.modId = modId;
        this.environment = environment;
        this.settings = settings;
        this.protocolManager = new FabricProtocolManager(this, environment);
        this.serverManager = this.constructServerManager();
        this.injector = new FabricChannelInjector(this, environment);
    }

    protected ServerManager constructServerManager() {
        return new FabricServerManager();
    }

    @Override
    public void load() {
        if (this.loaded) {
            return;
        }

        String id = (this.environment.name() + "_" + this.modId).toLowerCase(Locale.ROOT);
        PacketEvents.IDENTIFIER = "pe-" + id;
        PacketEvents.ENCODER_NAME = "pe-encoder-" + id;
        PacketEvents.DECODER_NAME = "pe-decoder-" + id;
        PacketEvents.CONNECTION_HANDLER_NAME = "pe-connection-handler-" + id;
        PacketEvents.SERVER_CHANNEL_HANDLER_NAME = "pe-connection-initializer-" + id;

        WrappedBlockState.ensureLoad();

        // register internal packet listener (should be the first listener)
        // this listener doesn't do any modifications to the packets, just reads data
        this.getEventManager().registerListener(new InternalFabricPacketListener());
        this.loaded = true;
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

        //TODO Cross-platform metrics?

        PacketType.Play.Client.load();
        PacketType.Play.Server.load();

        // Let people override this, at their own risk
        if (!"true".equalsIgnoreCase(System.getenv("PE_IGNORE_INCOMPATIBILITY"))) {
            checkCompatibility();
        }

        this.initialized = true;
    }

    private void checkCompatibility() {
        ViaVersionUtil.checkIfViaIsPresent();
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
        this.getEventManager().unregisterAllListeners();
        this.initialized = false;
        this.terminated = true;
    }

    @Override
    public boolean isTerminated() {
        return this.terminated;
    }

    // Returning ModInitializer instance makes getClass().getClassLoader() return KnotClassLoader
    // Which allows us to correctly check for existence of Via, Geyser, etc...
    @Override
    public ModInitializer getPlugin() {
        return PacketEventsMod.INSTANCE;
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
    public LogManager getLogManager() {
        return this.logManager;
    }

    @Override
    public PlayerManager getPlayerManager() {
        return this.environment == EnvType.SERVER ? FabricPacketEventsAPIManagerFactory.getLazyPlayerManagerHolder().get() : FabricPacketEventsAPIManagerFactory.getClientLazyPlayerManagerHolder().get();
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

    @Override
    public RegistryManager getRegistryManager() {
        return FabricPacketEventsAPIManagerFactory.getLazyRegistryManagerHolder().get();
    }

    public static FabricPacketEventsAPI getAPI(PacketSide pipelineSide) {
        return pipelineSide == PacketSide.CLIENT ? clientFabricAPI : serverFabricAPI;
    }

    public static void setClientAPI(FabricPacketEventsAPI fabricPacketEventsAPI) {
        clientFabricAPI = fabricPacketEventsAPI;
    }

    public static void setServerAPI(FabricPacketEventsAPI fabricPacketEventsAPI) {
        serverFabricAPI = fabricPacketEventsAPI;
    }

    public static FabricPacketEventsAPI getClientAPI() {
        return clientFabricAPI;
    }

    public static FabricPacketEventsAPI getServerAPI() {
        return serverFabricAPI;
    }

    public FabricPacketEventsAPI(String modId, EnvType environment) {
        this(modId, environment, new PacketEventsSettings());
    }
}
