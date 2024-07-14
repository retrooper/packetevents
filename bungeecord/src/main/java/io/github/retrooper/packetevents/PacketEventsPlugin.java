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
import com.github.retrooper.packetevents.event.*;
import com.github.retrooper.packetevents.protocol.chat.ChatTypes;
import com.github.retrooper.packetevents.protocol.chat.message.ChatMessageLegacy;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.TimeStampMode;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChatMessage;
import io.github.retrooper.packetevents.bungee.factory.BungeePacketEventsBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.plugin.Plugin;

public final class PacketEventsPlugin extends Plugin {
    @Override
    public void onLoad() {
        PacketEvents.setAPI(BungeePacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {
        // Register your listeners
        PacketEvents.getAPI().getSettings().debug(false).checkForUpdates(true).timeStampMode(TimeStampMode.MILLIS);
        PacketEvents.getAPI().init();
        PacketListenerCommon listener = new PacketListenerAbstract(PacketListenerPriority.HIGH) {
            @Override
            public void onPacketReceive(PacketReceiveEvent event) {
                //System.out.println("Pipe: " + ChannelHelper.pipelineHandlerNamesAsString(event.getChannel()));
                //System.out.println("In type: " + event.getPacketType().getName());

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
                //System.out.println("Pipe: " + ChannelHelper.pipelineHandlerNamesAsString(event.getChannel()));
                //System.out.println("Out type: " + event.getPacketType().getName());
            }
        };
        //PacketEvents.getAPI().getEventManager().registerListener(listener);
    }

    @Override
    public void onDisable() {
        PacketEvents.getAPI().terminate();
    }
}
