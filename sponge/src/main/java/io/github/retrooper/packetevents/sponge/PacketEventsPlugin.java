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

package io.github.retrooper.packetevents.sponge;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.*;
import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.TimeStampMode;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWindowItems;
import com.google.inject.Inject;
import io.github.retrooper.packetevents.sponge.factory.SpongePacketEventsBuilder;
import io.github.retrooper.packetevents.sponge.util.SpongeConversionUtil;
import org.bstats.charts.SimplePie;
import org.bstats.sponge.Metrics;
import org.spongepowered.api.Server;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.lifecycle.StartingEngineEvent;
import org.spongepowered.api.event.lifecycle.StoppingEngineEvent;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

@Plugin("packetevents")
public class PacketEventsPlugin {

    private final PluginContainer pluginContainer;
    private final Metrics metrics;

    @Inject
    public PacketEventsPlugin(PluginContainer pluginContainer, Metrics.Factory metricsFactory) {
        this.pluginContainer = pluginContainer;
        this.metrics = metricsFactory.make(11327);
    }

    @Listener(order = Order.EARLY)
    public void onServerStart(final StartingEngineEvent<Server> event) {
        PacketEvents.setAPI(SpongePacketEventsBuilder.build(pluginContainer));
        PacketEvents.getAPI().load();

        // Register your listeners
        PacketEvents.getAPI().getSettings().debug(false).downsampleColors(false).checkForUpdates(true).timeStampMode(TimeStampMode.MILLIS).reEncodeByDefault(true);
        PacketEvents.getAPI().init();

        //Just to have an idea of which versions of packetevents people use
        metrics.addCustomChart(new SimplePie("packetevents_version", () -> PacketEvents.getAPI().getVersion().toStringWithoutSnapshot()));

        SimplePacketListenerAbstract listener = new SimplePacketListenerAbstract(PacketListenerPriority.HIGH) {

            // Testing ItemStack conversion, can be removed in future
            @Override
            public void onPacketPlaySend(PacketPlaySendEvent event) {
                if (event.getPacketType() == PacketType.Play.Server.WINDOW_ITEMS) {
                    WrapperPlayServerWindowItems items = new WrapperPlayServerWindowItems(event);
                    for (ItemStack item : items.getItems()) {
                        org.spongepowered.api.item.inventory.ItemStack sponge = SpongeConversionUtil.toSpongeItemStack(item);
                        System.out.println(sponge.type().key(RegistryTypes.ITEM_TYPE).formatted());
                        System.out.println(SpongeConversionUtil.fromSpongeItemStack(sponge).getType().getName().toString());
                    }
                }
            }

            @Override
            public void onUserConnect(UserConnectEvent event) {
                PacketEvents.getAPI().getLogManager().debug("User: (host-name) " + event.getUser().getAddress().getHostString() + " connected...");
            }

            @Override
            public void onUserLogin(UserLoginEvent event) {
                PacketEvents.getAPI().getLogManager().debug("You logged in! User name: " + event.getUser().getProfile().getName());
            }

            @Override
            public void onUserDisconnect(UserDisconnectEvent event) {
                PacketEvents.getAPI().getLogManager().debug("User: (host-name) " + event.getUser().getAddress().getHostString() + " disconnected...");
            }
        };
//        PacketEvents.getAPI().getEventManager().registerListener(listener);
    }

    @Listener(order = Order.LATE)
    public void onStopping(StoppingEngineEvent<Server> event) {
        PacketEvents.getAPI().terminate();
    }
}
