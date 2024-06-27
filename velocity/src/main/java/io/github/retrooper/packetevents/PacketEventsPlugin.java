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

package io.github.retrooper.packetevents;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import io.github.retrooper.packetevents.velocity.factory.VelocityPacketEventsBuilder;
import org.slf4j.Logger;

import java.nio.file.Path;

public class PacketEventsPlugin {
    private final ProxyServer server;
    private final Logger logger;
    private final PluginContainer pluginContainer;
    private final Path dataDirectory;

    @Inject
    public PacketEventsPlugin(final ProxyServer server,
                              final Logger logger,
                              final PluginContainer pluginContainer, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.pluginContainer = pluginContainer;
        this.dataDirectory = dataDirectory;
        logger.info("Plugin started");

    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        logger.info("Injecting packetevents...");
        PacketEvents.setAPI(VelocityPacketEventsBuilder.build(server, pluginContainer, logger, dataDirectory));
        PacketEvents.getAPI().load();
        // It should only be enabled in a development environment, not globally
        // PacketEvents.getAPI().getSettings().debug(true);
        PacketListenerAbstract listener = new PacketListenerAbstract() {
            @Override
            public void onPacketReceive(PacketReceiveEvent event) {
                //System.out.println("Incoming: " + event.getPacketType().getName());

                //Testing sending packets to users on proxies!
                /*if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
                    if (new WrapperPlayClientInteractEntity(event).getAction() == WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
                        event.getUser().sendMessage(Component.text("Test message").color(NamedTextColor.RED));
                        event.getUser().sendTitle(Component.text("Test title").color(NamedTextColor.GREEN),
                                Component.text("subtitle test").color(NamedTextColor.RED),
                                3, 3, 5);
                    }
                }*/
            }

            @Override
            public void onPacketSend(PacketSendEvent event) {
                //System.out.println("Outgoing: " + event.getPacketType().getName());
                /*if (event.getPacketType() == PacketType.Play.Server.SYSTEM_CHAT_MESSAGE) {
                    System.out.println("Before processing, pipe: " + ChannelHelper.pipelineHandlerNamesAsString(event.getChannel()));
                    WrapperPlayServerSystemChatMessage msg = new WrapperPlayServerSystemChatMessage(event);
                    System.out.println("After processing: " + msg.getMessage());
                }*/
            }
        };
        //PacketEvents.getAPI().getEventManager().registerListener(listener);
        PacketEvents.getAPI().init();
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        PacketEvents.getAPI().terminate();
    }
}
