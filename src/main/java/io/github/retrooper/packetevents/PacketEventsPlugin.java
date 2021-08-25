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
import io.github.retrooper.packetevents.protocol.ConnectionState;
import io.github.retrooper.packetevents.protocol.PacketType;
import io.github.retrooper.packetevents.utils.dependencies.protocolsupport.ProtocolSupportVersionLookupUtils;
import io.github.retrooper.packetevents.utils.netty.buffer.ByteBufAbstract;
import io.github.retrooper.packetevents.utils.netty.buffer.ByteBufUtil;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;
import io.github.retrooper.packetevents.wrapper.game.client.WrapperGameClientInteractEntity;
import io.github.retrooper.packetevents.wrapper.handshaking.client.WrapperHandshakingClientHandshake;
import io.github.retrooper.packetevents.wrapper.login.client.WrapperLoginClientLoginStart;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.SocketAddress;
import java.util.Arrays;

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
        PacketEvents.get().getEventManager().registerListener(new PacketListenerAbstract(PacketListenerAbstract.Priority.LOWEST) {
            @Override
            public void onPacketSend(PacketSendEvent event) {
                ByteBufAbstract byteBuf = event.getByteBuf();
                if (event.getPacketType() == PacketType.Login.Server.LOGIN_SUCCESS) {
                    //Transition into the GAME connection state
                    PacketEvents.get().getInjector().changeConnectionState(event.getChannel().rawChannel(), ConnectionState.GAME);
                }
            }

            @Override
            public void onPacketReceive(PacketReceiveEvent event) {
                ByteBufAbstract byteBuf = event.getByteBuf();
                switch (event.getConnectionState()) {
                    case HANDSHAKING:
                        if (event.getPacketType() == PacketType.Handshaking.Client.HANDSHAKE) {
                            WrapperHandshakingClientHandshake handshake = new WrapperHandshakingClientHandshake(event.getClientVersion(), byteBuf);
                            ClientVersion clientVersion = handshake.getClientVersion();
                            //We weren't able to be before protocolsupport in the pipeline YET. After this packet we will be.
                            //ProtocolSupport modifies the packet, so we are forced to use their API to confirm the version
                            if (ProtocolSupportVersionLookupUtils.isAvailable()) {
                                int protocolVersion = ProtocolSupportVersionLookupUtils.getProtocolVersion(event.getChannel().remoteAddress());
                                clientVersion = ClientVersion.getClientVersion(protocolVersion);
                            }
                            //Cache client version
                            PacketEvents.get().getPlayerManager().clientVersions.put(event.getChannel().rawChannel(), clientVersion);
                            //Transition into the next connection state
                            PacketEvents.get().getInjector().changeConnectionState(event.getChannel().rawChannel(), handshake.getNextConnectionState());
                            System.out.println("NEXT CONNECTION STATE: " + handshake.getNextConnectionState());
                            System.out.println("USER CONNECTED WITH CLIENT VERSION: " + clientVersion.name());
                        }
                        break;
                    case LOGIN:
                        if (event.getPacketType() == PacketType.Login.Client.LOGIN_START) {
                            WrapperLoginClientLoginStart start = new WrapperLoginClientLoginStart(event.getClientVersion(), byteBuf);
                            //Map the player usernames with their netty channels
                            PacketEvents.get().getPlayerManager().channels.put(start.getUsername(), event.getChannel().rawChannel());
                        }
                        break;
                }
            }
        });

        PacketEvents.get().getEventManager().registerListener(new PacketListenerAbstract() {
            @Override
            public void onPacketReceive(PacketReceiveEvent event) {
                if (event.getPacketType() == PacketType.Game.Client.INTERACT_ENTITY) {
                    ByteBufAbstract bb = ByteBufUtil.buffer();
                    PacketWrapper wrapper = PacketWrapper.createUniversalPacketWrapper(bb);
                    event.getPlayer().sendMessage("setting slot to 7");
                    wrapper.writeVarInt(PacketType.Game.Server.HELD_ITEM_CHANGE.getID());
                    bb.writeByte(7);
                    PacketEvents.get().getPlayerManager().sendPacket(event.getChannel(), bb);
                    event.getPlayer().sendMessage("sent");

                    WrapperGameClientInteractEntity interactEntity = new WrapperGameClientInteractEntity(event.getClientVersion(), event.getByteBuf());
                    int entityID = interactEntity.getEntityID();
                    Entity entity = PacketEvents.get().getServerManager().getEntityByID(entityID);
                    if (entity != null) {
                        event.getPlayer().sendMessage("entity name: " + entity.getName());
                    }
                    event.getPlayer().sendMessage("type: " + interactEntity.getType().name());
                    System.out.println("HANDLERS: " + Arrays.toString(event.getChannel().pipeline().names().toArray(new String[0])));
                }
            }
        });
    }

    @Override
    public void onDisable() {
        PacketEvents.get().terminate();
    }
}