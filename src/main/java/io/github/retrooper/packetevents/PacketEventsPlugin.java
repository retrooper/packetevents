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
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketSendEvent;
import io.github.retrooper.packetevents.manager.player.ClientVersion;
import io.github.retrooper.packetevents.manager.player.Hand;
import io.github.retrooper.packetevents.protocol.ConnectionState;
import io.github.retrooper.packetevents.protocol.PacketType;
import io.github.retrooper.packetevents.utils.bytebuf.ByteBufAbstract;
import io.github.retrooper.packetevents.wrapper.game.client.WrapperGameClientAnimation;
import io.github.retrooper.packetevents.wrapper.game.client.WrapperGameClientChatMessage;
import io.github.retrooper.packetevents.wrapper.handshaking.client.WrapperHandshakingClientHandshake;
import io.github.retrooper.packetevents.wrapper.login.client.WrapperLoginClientLoginStart;
import io.github.retrooper.packetevents.wrapper.status.client.WrapperStatusClientPing;
import io.github.retrooper.packetevents.wrapper.status.server.WrapperStatusServerPong;
import org.bukkit.plugin.java.JavaPlugin;

public class PacketEventsPlugin extends JavaPlugin {
    @Override
    public void onLoad() {
        PacketEvents.get().load(this);
        //You can do something here as it is loading
    }

    @Override
    public void onEnable() {
        PacketEvents.get().init();
        //Internal listener
        PacketEvents.get().registerListener(new PacketListenerAbstract(PacketListenerAbstract.Priority.LOWEST) {
            @Override
            public void onPacketSend(PacketSendEvent event) {
                ByteBufAbstract byteBuf = event.getByteBuf();
                if (event.getPacketType() == PacketType.Login.Server.LOGIN_SUCCESS) {
                    //Transition into the GAME connection state
                    PacketEvents.get().getInjector().changeConnectionState(event.getChannel(), ConnectionState.GAME);
                    System.out.println("CHANGED STATE TO GAME");
                }
                else if (event.getPacketType() == PacketType.Status.Server.PONG) {
                    WrapperStatusServerPong pong = new WrapperStatusServerPong(ClientVersion.UNKNOWN, byteBuf);
                    long payload = pong.getTime();
                    System.out.println("PAYLOAD RESPONSE: " + payload);
                }
            }

            @Override
            public void onPacketReceive(PacketReceiveEvent event) {
                ByteBufAbstract byteBuf = event.getByteBuf();
                switch (event.getConnectionState()) {
                    case HANDSHAKING:
                        WrapperHandshakingClientHandshake handshake = new WrapperHandshakingClientHandshake(event.getClientVersion(), byteBuf);
                        //Cache client version
                        PacketEvents.get().getPlayerManager().clientVersions.put(event.getChannel(), handshake.getClientVersion());
                        //Transition into the next connection state
                        PacketEvents.get().getInjector().changeConnectionState(event.getChannel(), handshake.getNextConnectionState());
                        break;

                    case STATUS:
                        if (event.getPacketType() == PacketType.Status.Client.PING) {
                            WrapperStatusClientPing ping = new WrapperStatusClientPing(event.getClientVersion(), byteBuf);
                            long payload = ping.getTime();
                            System.out.println("PAYLOAD: " + payload);
                        }
                        break;

                    case LOGIN:
                        if (event.getPacketType() == PacketType.Login.Client.LOGIN_START) {
                            WrapperLoginClientLoginStart start = new WrapperLoginClientLoginStart(event.getClientVersion(), byteBuf);
                            //Map the player usernames with their netty channels
                            PacketEvents.get().getPlayerManager().channels.put(start.getUsername(), event.getChannel());
                        }
                        break;
                }
            }
        });

        //Sample listener
        PacketEvents.get().registerListener(new PacketListenerAbstract() {
            @Override
            public void onPacketReceive(PacketReceiveEvent event) {
                if (event.getPacketType() == PacketType.Game.Client.ANIMATION) {
                    WrapperGameClientAnimation animation = new WrapperGameClientAnimation(event.getClientVersion(), event.getByteBuf());
                    Hand hand = animation.getHand();
                    event.getPlayer().sendMessage("Nice hand: " + hand.name());
                }
                else if (event.getPacketType() == PacketType.Handshaking.Client.HANDSHAKE) {
                    WrapperHandshakingClientHandshake handshake = new WrapperHandshakingClientHandshake(event.getClientVersion(), event.getByteBuf());

                    ClientVersion clientVersion = handshake.getClientVersion();
                    System.out.println("USER JOINED WITH CLIENT VERSION: " + clientVersion.name());
                }
                else if (event.getPacketType() == PacketType.Game.Client.CHAT_MESSAGE) {
                    WrapperGameClientChatMessage chatMessage = new WrapperGameClientChatMessage(event.getClientVersion(), event.getByteBuf());
                    String msg = chatMessage.getMessage();
                }
            }
        });
    }

    @Override
    public void onDisable() {
        PacketEvents.get().terminate();
    }
}