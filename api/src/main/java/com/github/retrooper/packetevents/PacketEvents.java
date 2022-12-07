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

package com.github.retrooper.packetevents;

import com.github.retrooper.packetevents.injector.ChannelInjector;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import com.github.retrooper.packetevents.manager.server.ServerManager;
import com.github.retrooper.packetevents.netty.NettyManager;
import com.github.retrooper.packetevents.settings.PacketEventsSettings;
import com.github.retrooper.packetevents.util.LogManager;

public final class PacketEvents {
    private static PacketEventsAPI<?> API;

    //Put these variable names anywhere else, they are really only for the injectors
    public static String IDENTIFIER, ENCODER_NAME, DECODER_NAME, CONNECTION_HANDLER_NAME, SERVER_CHANNEL_HANDLER_NAME,
            TIMEOUT_HANDLER_NAME;

    public static PacketEventsAPI<?> getAPI() {
        return API;
    }

    public static void setAPI(PacketEventsAPI<?> api) {
        API = api;
    }

    public static Builder builder() {
        return new Builder();
    }

    public abstract static class Context<T> {
        private final Object pluginInstance;
        private final ProtocolManager protocolManager;
        private final ServerManager serverManager;
        private final PlayerManager playerManager;
        private final NettyManager nettyManager;
        private final ChannelInjector injector;
        private final LogManager logManager;
        protected boolean lateBind = false;

        public Context(Object pluginInstance, ProtocolManager protocolManager, ServerManager serverManager, PlayerManager playerManager,
                       NettyManager nettyManager, ChannelInjector injector, LogManager logManager) {
            this.pluginInstance = pluginInstance;
            this.protocolManager = protocolManager;
            this.serverManager = serverManager;
            this.playerManager = playerManager;
            this.nettyManager = nettyManager;
            this.injector = injector;
            this.logManager = logManager;
        }

        protected abstract PacketEventsAPI<T> build();

        public Object getPluginInstance() {
            return pluginInstance;
        }

        public ProtocolManager getProtocolManager() {
            return protocolManager;
        }

        public ServerManager getServerManager() {
            return serverManager;
        }

        public PlayerManager getPlayerManager() {
            return playerManager;
        }

        public NettyManager getNettyManager() {
            return nettyManager;
        }

        public ChannelInjector getInjector() {
            return injector;
        }

        public LogManager getLogManager() {
            return logManager;
        }
    }

    public static class Builder {
        private Context<?> context = null;
        private PacketEventsSettings settings = new PacketEventsSettings();
        public Builder context(Context<?> context) {
            this.context = context;
            return this;
        }

        public Builder settings(PacketEventsSettings settings) {
            this.settings = settings;
            return this;
        }

        public PacketEventsAPI<?> build() {
            if (context == null) {
                throw new IllegalStateException("Failed to build a PacketEvents instance. Please specify a PacketEvents context in the PacketEvents builder to allow PacketEvents to load the correct implementation.");
            }
            PacketEventsAPI<?> api = context.build();
            api.setSettings(settings);
            //The implementation may have already set the API.
            if (getAPI() == null) {
                setAPI(api);
            }
            return api;
        }
    }
}
