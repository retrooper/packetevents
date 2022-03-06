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

package io.github.retrooper.packetevents;

import com.github.retrooper.packetevents.PacketEvents;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.ProxyServer;
import io.github.retrooper.packetevents.velocity.factory.VelocityPacketEventsBuilder;

import java.util.logging.Logger;

@Plugin(id = "packetevents", name = "PacketEvents", version = "2.0.0")
public class PacketEventsPlugin {
    private final ProxyServer server;
    private final Logger logger;
    public PacketEventsPlugin(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
        logger.info("Plugin started?");
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        logger.info("Injecting packetevents...");
        PluginContainer plugin = server.getPluginManager().getPlugin("packetevents").orElse(null);
        PacketEvents.setAPI(VelocityPacketEventsBuilder.build(server, plugin));
        PacketEvents.getAPI().load();
        PacketEvents.getAPI().getSettings().debug(true);
        PacketEvents.getAPI().init();
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        PacketEvents.getAPI().terminate();
    }
}
