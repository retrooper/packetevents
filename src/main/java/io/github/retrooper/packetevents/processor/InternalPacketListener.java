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

package io.github.retrooper.packetevents.processor;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketSendEvent;
import io.github.retrooper.packetevents.protocol.ConnectionState;
import io.github.retrooper.packetevents.protocol.PacketType;
import io.github.retrooper.packetevents.utils.netty.buffer.ByteBufAbstract;
import io.github.retrooper.packetevents.wrapper.handshaking.client.WrapperHandshakingClientHandshake;
import io.github.retrooper.packetevents.wrapper.login.client.WrapperLoginClientLoginStart;

public class InternalPacketListener extends PacketListenerAbstract {
    public InternalPacketListener() {
        super(Priority.LOWEST);
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
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
                    //Cache client version
                    PacketEvents.get().getPlayerManager().clientVersions.put(event.getChannel().rawChannel(), handshake.getClientVersion());
                    //Transition into the LOGIN OR STATUS connection state
                    PacketEvents.get().getInjector().changeConnectionState(event.getChannel().rawChannel(), handshake.getNextConnectionState());
                    //System.out.println("NEXT CONNECTION STATE: " + handshake.getNextConnectionState());
                    //System.out.println("USER CONNECTED WITH CLIENT VERSION: " + handshake.getClientVersion().name());
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
}
