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
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import io.github.retrooper.packetevents.bungee.BungeePacketEventsBuilder;
import io.github.retrooper.packetevents.handlers.PacketDecoder;
import io.github.retrooper.packetevents.handlers.PacketEncoder;
import io.github.retrooper.packetevents.injector.ServerConnectionInitializer;
import io.netty.channel.Channel;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public final class PacketEventsPlugin extends Plugin {
    @Override
    public void onLoad() {
        PacketEvents.setAPI(BungeePacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
        PacketEvents.getAPI().getSettings().debug(true);
    }

    @Override
    public void onEnable() {
        //Register your listeners
        PacketEvents.getAPI().getEventManager().registerListener(new PacketListenerAbstract() {
            @Override
            public void onPacketReceive(PacketReceiveEvent event) {
                System.out.println("Receiving: " + event.getPacketType().getName());
            }

            @Override
            public void onPacketSend(PacketSendEvent event) {
                System.out.println("Sending: " + event.getPacketType().getName());
                Channel channel = (Channel) event.getChannel().rawChannel();
            }
        });
        PacketEvents.getAPI().init();
    }


    @Override
    public void onDisable() {
        PacketEvents.getAPI().terminate();
    }
}
