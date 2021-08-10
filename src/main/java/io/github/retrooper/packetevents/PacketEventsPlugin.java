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

import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.PacketListenerPriority;
import io.github.retrooper.packetevents.event.impl.PacketDecodeEvent;
import io.github.retrooper.packetevents.packettype.PacketState;
import io.github.retrooper.packetevents.settings.PacketEventsSettings;
import io.github.retrooper.packetevents.utils.netty.bytebuf.ByteBufAbstract;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import io.github.retrooper.packetevents.wrapper.handshaking.client.WrapperHandshakingClientHandshake;
import io.github.retrooper.packetevents.wrapper.login.client.WrapperLoginClientLoginStart;
import org.bukkit.plugin.java.JavaPlugin;

public class PacketEventsPlugin extends JavaPlugin {
    @Override
    public void onLoad() {
        PacketEventsSettings settings = PacketEvents.create(this).getSettings();
        settings
                .fallbackServerVersion(ServerVersion.v_1_7_10)
                .compatInjector(false)
                .checkForUpdates(true)
                .bStats(true);
        PacketEvents.get().load();
        //You can do something here as it is loading
    }

    @Override
    public void onEnable() {
        PacketEvents.get().init();
        PacketEvents.get().registerListener(new PacketListenerAbstract(PacketListenerPriority.LOWEST) {
            @Override
            public void onPacketDecode(PacketDecodeEvent event) {
                ByteBufAbstract byteBuf = event.getByteBuf();
                if (event.getPlayer() == null && event.getState() == null) {
                    PacketEvents.get().getInjector().changePacketState(event.getChannel(), PacketState.HANDSHAKING);
                    WrapperHandshakingClientHandshake handshake = new WrapperHandshakingClientHandshake(byteBuf);
                    PacketEvents.get().getPlayerUtils().clientVersions.put(event.getSocketAddress(), handshake.getClientVersion());
                    System.err.println("PROTOCOL VERSION: " + handshake.getClientVersion());
                    System.err.println("SERVER ADDRESS: " + handshake.getServerAddress());
                    System.err.println("PORT: " + handshake.getServerPort());
                    System.err.println("NEXT STATE: " + handshake.getNextState());
                    PacketEvents.get().getInjector().changePacketState(event.getChannel(), handshake.getNextState());
                }
                else if (event.getState() == PacketState.LOGIN) {
                    if (event.getPacketID() == 0) {
                        WrapperLoginClientLoginStart start = new WrapperLoginClientLoginStart(byteBuf);
                        //Cache the channel
                        PacketEvents.get().getPlayerUtils().channels.put(start.getUsername(), event.getChannel());
                        PacketEvents.get().getInjector().changePacketState(event.getChannel(), PacketState.PLAY);
                    }
                }
                else if (event.getState() == PacketState.PLAY && event.getPlayer() != null) {
                    event.getPlayer().sendMessage("PACKET ID: "+ event.getPacketID());
                }
            }
        });
    }

    @Override
    public void onDisable() {
        PacketEvents.get().terminate();
    }
}